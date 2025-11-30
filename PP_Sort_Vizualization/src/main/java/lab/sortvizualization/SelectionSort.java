package lab.sortvizualization;

public class SelectionSort implements SortingStrategy {
    @Override
    public void sort(int[] array, Visualizer visualizer) throws InterruptedException {
        int n = array.length;

        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (array[j] < array[minIdx]) {
                    minIdx = j;
                }
            }

            if (minIdx != i) {
                int temp = array[minIdx];
                array[minIdx] = array[i];
                array[i] = temp;

                visualizer.onUpdate();
            }
        }
    }
}