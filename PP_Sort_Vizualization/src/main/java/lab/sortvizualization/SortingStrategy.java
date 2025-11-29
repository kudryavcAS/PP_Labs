package lab.sortvizualization;

import java.util.concurrent.atomic.AtomicBoolean;

public interface SortingStrategy {
    void sort(int[] array, Visualizer visualizer) throws InterruptedException;

    interface Visualizer {
        void onUpdate();
    }
}