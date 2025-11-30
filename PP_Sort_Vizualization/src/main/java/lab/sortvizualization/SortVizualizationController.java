package lab.sortvizualization;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class SortVizualizationController {

    @FXML
    private ComboBox<String> algorithmChoice;
    @FXML
    private Pane visualPane;
    @FXML
    private Button btnSort;
    @FXML
    private Button btnReset;
    @FXML
    private Button btnPause;

    private int[] array;
    private static final int ARRAY_SIZE = 50;
    private static final int DELAY_MS = 20;

    private Thread sortingThread;
    private final Object pauseLock = new Object();
    private volatile boolean isPaused = false;

    @FXML
    public void initialize() {
        algorithmChoice.getItems().addAll(
                "Пузырьковая сортировка",
                "Сортировка выбором",
                "Сортировка вставками",
                "Сортировка слиянием",
                "Пирамидальная сортировка",
                "Быстрая сортировка (Хоара)"
        );
        algorithmChoice.getSelectionModel().selectFirst();

        visualPane.widthProperty().addListener((_, _, _) -> drawArray());
        visualPane.heightProperty().addListener((_, _, _) -> drawArray());

        onResetClick();
    }

    @FXML
    protected void onResetClick() {
        stopCurrentSorting();

        array = new int[ARRAY_SIZE];
        Random random = new Random();
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = random.nextInt(10, 400);
        }
        drawArray();

        setButtonsState(false);
        isPaused = false;
        btnPause.setText("Пауза");
    }

    @FXML
    protected void onPauseClick() {
        synchronized (pauseLock) {
            if (isPaused) {
                isPaused = false;
                btnPause.setText("Пауза");
                pauseLock.notifyAll();
            } else {
                isPaused = true;
                btnPause.setText("Продолжить");
            }
        }
    }

    @FXML
    protected void onSortClick() {
        stopCurrentSorting();

        String selected = algorithmChoice.getValue();
        SortingStrategy strategy = getStrategyByName(selected);
        if (strategy == null) return;

        setButtonsState(true);
        isPaused = false;
        btnPause.setText("Пауза");

        Runnable task = () -> {
            try {
                strategy.sort(array, () -> {
                    checkPause();

                    Platform.runLater(this::drawArray);

                    try {
                        Thread.sleep(DELAY_MS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    if (Thread.currentThread().isInterrupted()) {
                        throw new RuntimeException("Stop");
                    }
                });
            } catch (Exception _) {
            } finally {
                Platform.runLater(() -> setButtonsState(false));
            }
        };

        sortingThread = new Thread(task);
        sortingThread.setDaemon(true);
        sortingThread.start();
    }

    private void checkPause() {
        synchronized (pauseLock) {
            while (isPaused) {
                try {
                    pauseLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    private void stopCurrentSorting() {
        if (sortingThread != null && sortingThread.isAlive()) {
            sortingThread.interrupt();
            try {
                sortingThread.join(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void setButtonsState(boolean isRunning) {
        btnSort.setDisable(isRunning);
        btnReset.setDisable(false);
        btnPause.setDisable(!isRunning);
    }

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
        if (paneWidth <= 0 || paneHeight <= 0) return;

        double barWidth = paneWidth / ARRAY_SIZE;
        double scaleFactor = paneHeight / 450.0;

        for (int i = 0; i < array.length; i++) {
            double scaledHeight = array[i] * scaleFactor;
            Rectangle rect = new Rectangle();
            rect.setX(i * barWidth);
            rect.setY(paneHeight - scaledHeight);
            rect.setWidth(barWidth - 1);
            rect.setHeight(scaledHeight);
            rect.setFill(Color.CORNFLOWERBLUE);
            visualPane.getChildren().add(rect);
        }
    }
}