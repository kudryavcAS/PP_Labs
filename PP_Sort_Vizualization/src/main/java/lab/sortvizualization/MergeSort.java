package lab.sortvizualization;

public class MergeSort implements SortingStrategy {
    @Override
    public void sort(int[] array, Visualizer visualizer) throws InterruptedException {
        sort(array, 0, array.length - 1, visualizer);
    }

    private void sort(int[] arr, int l, int r, Visualizer visualizer) throws InterruptedException {
        if (l < r) {
            int m = l + (r - l) / 2;

            sort(arr, l, m, visualizer);
            sort(arr, m + 1, r, visualizer);

            merge(arr, l, m, r, visualizer);
        }
    }

    private void merge(int[] arr, int l, int m, int r, Visualizer visualizer) throws InterruptedException {
        // Размеры подмассивов
        int n1 = m - l + 1;
        int n2 = r - m;

        // Временные массивы
        int[] L = new int[n1];
        int[] R = new int[n2];

        // Копируем данные
        System.arraycopy(arr, l, L, 0, n1);
        System.arraycopy(arr, m + 1, R, 0, n2);

        int i = 0, j = 0;
        int k = l;

        // Слияние
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            // Визуализируем изменение основного массива
            visualizer.onUpdate();
            k++;
        }

        // Докопируем остатки
        while (i < n1) {
            arr[k] = L[i];
            i++;
            visualizer.onUpdate();
            k++;
        }

        while (j < n2) {
            arr[k] = R[j];
            j++;
            visualizer.onUpdate();
            k++;
        }
    }
}