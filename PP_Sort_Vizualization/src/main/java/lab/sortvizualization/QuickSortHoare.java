package lab.sortvizualization;

public class QuickSortHoare implements SortingStrategy {
    @Override
    public void sort(int[] array, Visualizer visualizer) throws InterruptedException {
        quickSort(array, 0, array.length - 1, visualizer);
    }

    private void quickSort(int[] arr, int low, int high, Visualizer visualizer) throws InterruptedException {
        if (low < high) {
            int pivotIndex = partition(arr, low, high, visualizer);
            quickSort(arr, low, pivotIndex, visualizer);
            quickSort(arr, pivotIndex + 1, high, visualizer);
        }
    }

    private int partition(int[] arr, int low, int high, Visualizer visualizer) throws InterruptedException {
        int pivot = arr[low];
        int i = low - 1;
        int j = high + 1;

        while (true) {
            do { i++; } while (arr[i] < pivot);
            do { j--; } while (arr[j] > pivot);

            if (i >= j) return j;

            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;

            visualizer.onUpdate();
        }
    }
}