package lab.sortvizualization;

public interface SortingStrategy {
    void sort(int[] array, Visualizer visualizer) throws InterruptedException;

    interface Visualizer {
        void onUpdate();
    }
}