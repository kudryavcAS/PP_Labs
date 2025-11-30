package lab.sortvizualization;

import java.util.Random;

public class BogoSort implements SortingStrategy {
    private final Random random = new Random();

    @Override
    public void sort(int[] array, Visualizer visualizer) throws InterruptedException {
        while (!isSorted(array)) {
            shuffle(array);

            visualizer.onUpdate();
        }
    }

    private boolean isSorted(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                return false;
            }
        }
        return true;
    }

    private void shuffle(int[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);

            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
}