package lab.sortvizualization;

public class HeapSort implements SortingStrategy {
    @Override
    public void sort(int[] array, Visualizer visualizer) throws InterruptedException {
        int n = array.length;

        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(array, n, i, visualizer);

        for (int i = n - 1; i > 0; i--) {
            int temp = array[0];
            array[0] = array[i];
            array[i] = temp;

            visualizer.onUpdate();

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

            visualizer.onUpdate();
            visualizer.onUpdate();

            heapify(arr, n, largest, visualizer);
        }
    }
}