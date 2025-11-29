package lab.sortvizualization;

public class InsertionSort implements SortingStrategy {
    @Override
    public void sort(int[] array, Visualizer visualizer) throws InterruptedException {
        int n = array.length;

        for (int i = 1; i < n; ++i) {
            int key = array[i];
            int j = i - 1;

            // Сдвигаем элементы, которые больше key, на одну позицию вправо
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j = j - 1;

                // Визуализируем каждое смещение
                visualizer.onUpdate();
            }
            array[j + 1] = key;

            // Финальная вставка тоже обновляет вид
            visualizer.onUpdate();
        }
    }
}