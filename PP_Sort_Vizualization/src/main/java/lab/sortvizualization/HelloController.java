package lab.sortvizualization;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class HelloController {

    @FXML private ComboBox<String> algorithmChoice;
    @FXML private Pane visualPane;
    @FXML private Button btnSort;
    @FXML private Button btnReset;

    private int[] array;
    private static final int ARRAY_SIZE = 50; // Количество столбиков
    private static final int DELAY_MS = 20;   // Скорость анимации (чем меньше, тем быстрее)

    @FXML
    public void initialize() {
        // Заполняем выпадающий список
        algorithmChoice.getItems().addAll(
                "Bubble Sort",
                "Selection Sort",
                "Insertion Sort",
                "Merge Sort",
                "Heap Sort",
                "Quick Sort (Hoare)"
        );
        algorithmChoice.getSelectionModel().selectFirst();

        // Генерируем первый массив при старте
        onResetClick();
    }

    @FXML
    protected void onResetClick() {
        // Создаем случайный массив
        array = new int[ARRAY_SIZE];
        Random random = new Random();
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = random.nextInt(10, 400); // Высота столбиков
        }
        drawArray(); // Рисуем
    }

    @FXML
    protected void onSortClick() {
        String selected = algorithmChoice.getValue();
        SortingStrategy strategy = getStrategyByName(selected);

        if (strategy == null) return;

        // Блокируем кнопки во время сортировки
        btnSort.setDisable(true);
        btnReset.setDisable(true);

        // ЗАПУСКАЕМ В ОТДЕЛЬНОМ ПОТОКЕ
        new Thread(() -> {
            try {
                strategy.sort(array, () -> {
                    // Этот код вызывается из стратегии при каждом swap

                    // 1. Обновляем UI (обязательно через Platform.runLater)
                    Platform.runLater(this::drawArray);

                    // 2. Делаем паузу, чтобы глаз успел заметить
                    try {
                        Thread.sleep(DELAY_MS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // Когда закончили - разблокируем кнопки
                Platform.runLater(() -> {
                    btnSort.setDisable(false);
                    btnReset.setDisable(false);
                });
            }
        }).start();
    }

    // Фабричный метод выбора стратегии
    private SortingStrategy getStrategyByName(String name) {
        return switch (name) {
            case "Bubble Sort" -> new BubbleSort();
            case "Quick Sort (Hoare)" -> new QuickSortHoare();
            // Сюда добавить остальные классы:
            // case "Selection Sort" -> new SelectionSortStrategy();
            // ...
            default -> null;
        };
    }

    // Метод отрисовки
    private void drawArray() {
        visualPane.getChildren().clear(); // Очищаем старое

        double paneWidth = visualPane.getWidth();
        double paneHeight = visualPane.getHeight();
        double barWidth = paneWidth / ARRAY_SIZE;

        for (int i = 0; i < array.length; i++) {
            int value = array[i];

            // Создаем прямоугольник
            Rectangle rect = new Rectangle();
            rect.setX(i * barWidth);       // Позиция по X
            rect.setY(paneHeight - value); // Позиция по Y (рисуем снизу)
            rect.setWidth(barWidth - 2);   // -2 для маленького зазора между столбиками
            rect.setHeight(value);
            rect.setFill(Color.CORNFLOWERBLUE); // Красивый цвет

            visualPane.getChildren().add(rect);
        }
    }
}