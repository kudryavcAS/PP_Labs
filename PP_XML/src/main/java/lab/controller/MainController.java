package lab.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import lab.model.Book;
import lab.service.LibraryService;

import java.util.Optional;

public class MainController {

    @FXML private TextField searchField;
    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, Integer> colId;
    @FXML private TableColumn<Book, String> colTitle;
    @FXML private TableColumn<Book, String> colAuthor;
    @FXML private TableColumn<Book, Integer> colYear;
    @FXML private TableColumn<Book, Double> colPrice;
    @FXML private TableColumn<Book, String> colCategory;
    @FXML private TableColumn<Book, Integer> colTotal;
    @FXML private TableColumn<Book, Integer> colAvailable;

    private final LibraryService service = new LibraryService();
    private ObservableList<Book> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Привязка колонок к свойствам модели
        colId.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());
        colTitle.setCellValueFactory(cell -> cell.getValue().titleProperty());
        colAuthor.setCellValueFactory(cell -> cell.getValue().authorProperty());
        colYear.setCellValueFactory(cell -> cell.getValue().yearProperty().asObject());
        colPrice.setCellValueFactory(cell -> cell.getValue().priceProperty().asObject());
        colCategory.setCellValueFactory(cell -> cell.getValue().categoryProperty());
        colTotal.setCellValueFactory(cell -> cell.getValue().totalProperty().asObject());
        colAvailable.setCellValueFactory(cell -> cell.getValue().availableProperty().asObject());

        onRefresh(); // Загрузить данные при старте
    }

    @FXML
    void onRefresh() {
        try {
            masterData.setAll(service.loadBooks());
            bookTable.setItems(masterData);
        } catch (Exception e) {
            showAlert("Ошибка", "Ошибка валидации или загрузки XML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void onAdd() {
        // Создаем диалог для ввода данных новой книги
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Добавить книгу");
        dialog.setHeaderText("Введите данные книги");

        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField title = new TextField(); title.setPromptText("Название");
        TextField author = new TextField(); author.setPromptText("Автор");
        TextField year = new TextField(); year.setPromptText("Год");
        TextField price = new TextField(); price.setPromptText("Цена");
        TextField category = new TextField(); category.setPromptText("Категория");
        TextField total = new TextField(); total.setPromptText("Всего штук");

        grid.add(new Label("Название:"), 0, 0); grid.add(title, 1, 0);
        grid.add(new Label("Автор:"), 0, 1); grid.add(author, 1, 1);
        grid.add(new Label("Год:"), 0, 2); grid.add(year, 1, 2);
        grid.add(new Label("Цена:"), 0, 3); grid.add(price, 1, 3);
        grid.add(new Label("Категория:"), 0, 4); grid.add(category, 1, 4);
        grid.add(new Label("Всего:"), 0, 5); grid.add(total, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                try {
                    int newId = masterData.stream().mapToInt(Book::getId).max().orElse(0) + 1;
                    return new Book(
                            newId,
                            title.getText(),
                            author.getText(),
                            Integer.parseInt(year.getText()),
                            Double.parseDouble(price.getText()),
                            category.getText(),
                            Integer.parseInt(total.getText()),
                            Integer.parseInt(total.getText()) // Изначально доступно столько же, сколько всего
                    );
                } catch (NumberFormatException e) {
                    showAlert("Ошибка", "Некорректный формат чисел");
                    return null;
                }
            }
            return null;
        });

        Optional<Book> result = dialog.showAndWait();
        result.ifPresent(book -> {
            masterData.add(book);
            service.saveBooks(masterData);
        });
    }

    @FXML
    void onEdit() {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Внимание", "Выберите книгу для редактирования цены");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(String.valueOf(selected.getPrice()));
        dialog.setTitle("Изменение цены");
        dialog.setHeaderText("Новая цена для: " + selected.getTitle());
        dialog.setContentText("Цена:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(priceStr -> {
            try {
                double newPrice = Double.parseDouble(priceStr);
                selected.setPrice(newPrice);
                service.saveBooks(masterData);
                bookTable.refresh();
            } catch (NumberFormatException e) {
                showAlert("Ошибка", "Некорректная цена");
            }
        });
    }

    @FXML
    void onCheckout() {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Внимание", "Выберите книгу для выдачи");
            return;
        }

        if (selected.getAvailable() > 0) {
            selected.setAvailable(selected.getAvailable() - 1);
            service.saveBooks(masterData);
            bookTable.refresh(); // Обновить UI
            showAlert("Успех", "Книга выдана читателю.");
        } else {
            showAlert("Ошибка", "Нет доступных экземпляров.");
        }
    }

    @FXML
    void onSearch() {
        String filter = searchField.getText().toLowerCase();

        FilteredList<Book> filteredData = new FilteredList<>(masterData, b -> true);

        filteredData.setPredicate(book -> {
            if (filter == null || filter.isEmpty()) return true;

            return book.getAuthor().toLowerCase().contains(filter) ||
                    String.valueOf(book.getYear()).contains(filter) ||
                    book.getCategory().toLowerCase().contains(filter);
        });

        bookTable.setItems(filteredData);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}