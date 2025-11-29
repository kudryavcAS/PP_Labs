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
    private static final int ARRAY_SIZE = 50;
    private static final int DELAY_MS = 20;

    @FXML
    public void initialize() {
        // 1. ЗАПОЛНЯЕМ ПО-РУССКИ
        algorithmChoice.getItems().addAll(
                "Пузырьковая сортировка",
                "Сортировка выбором",
                "Сортировка вставками",
                "Сортировка слиянием",
                "Пирамидальная сортировка",
                "Быстрая сортировка (Хоара)"
        );
        algorithmChoice.getSelectionModel().selectFirst();

        onResetClick();
    }

    @FXML
    protected void onResetClick() {
        array = new int[ARRAY_SIZE];
        Random random = new Random();
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = random.nextInt(10, 400);
        }
        drawArray();
    }

    @FXML
    protected void onSortClick() {
        String selected = algorithmChoice.getValue();
        SortingStrategy strategy = getStrategyByName(selected);

        if (strategy == null) return;

        btnSort.setDisable(true);
        btnReset.setDisable(true);

        new Thread(() -> {
            try {
                strategy.sort(array, () -> {
                    Platform.runLater(this::drawArray);
                    try {
                        Thread.sleep(DELAY_MS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                Platform.runLater(() -> {
                    btnSort.setDisable(false);
                    btnReset.setDisable(false);
                });
            }
        }).start();
    }

    // 2. ИЗМЕНЯЕМ ПРОВЕРКУ СТРОК (SWITCH)
    private SortingStrategy getStrategyByName(String name) {
        return switch (name) {
            case "Пузырьковая сортировка" -> new BubbleSort();
            case "Сортировка вставками" -> new InsertionSort();
            case "Сортировка выбором" -> new SelectionSort();
            case "Сортировка слиянием" -> new MergeSort();
            case "Пирамидальная сортировка" -> new HeapSort();
            case "Быстрая сортировка (Хоара)" -> new QuickSortHoare();
            default -> null;
        };
    }

    private void drawArray() {
        visualPane.getChildren().clear();

        double paneWidth = visualPane.getWidth();
        double paneHeight = visualPane.getHeight();
        double barWidth = paneWidth / ARRAY_SIZE;

        for (int i = 0; i < array.length; i++) {
            int value = array[i];

            Rectangle rect = new Rectangle();
            rect.setX(i * barWidth);
            rect.setY(paneHeight - value);
            rect.setWidth(barWidth - 2);
            rect.setHeight(value);
            rect.setFill(Color.CORNFLOWERBLUE);

            visualPane.getChildren().add(rect);
        }
    }
}