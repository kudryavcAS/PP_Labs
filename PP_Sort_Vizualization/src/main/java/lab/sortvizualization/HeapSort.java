package lab.sortvizualization;

public class HeapSort implements SortingStrategy {
    @Override
    public void sort(int[] array, Visualizer visualizer) throws InterruptedException {
        int n = array.length;

        // Построение кучи (перегруппировка массива)
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(array, n, i, visualizer);

        // Один за другим извлекаем элементы из кучи
        for (int i = n - 1; i > 0; i--) {
            // Перемещаем текущий корень в конец
            int temp = array[0];
            array[0] = array[i];
            array[i] = temp;

            visualizer.onUpdate(); // Показываем обмен

            // Вызываем процедуру heapify на уменьшенной куче
            heapify(array, i, 0, visualizer);
        }
    }

    void heapify(int[] arr, int n, int i, Visualizer visualizer) throws InterruptedException {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        if (l < n && arr[l] > arr[largest])
            largest = l;

        if (r < n && arr[r] > arr[largest])
            largest = r;

        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            visualizer.onUpdate(); // Показываем перестройку кучи

            heapify(arr, n, largest, visualizer);
        }
    }
}