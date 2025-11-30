package lab.sortvizualization;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SortingTests {

    static Stream<Arguments> provideStrategies() {
        return Stream.of(
                Arguments.of(new BubbleSort(), "Bubble Sort"),
                Arguments.of(new SelectionSort(), "Selection Sort"),
                Arguments.of(new InsertionSort(), "Insertion Sort"),
                Arguments.of(new MergeSort(), "Merge Sort"),
                Arguments.of(new HeapSort(), "Heap Sort"),
                Arguments.of(new QuickSortHoare(), "Quick Sort (Hoare)")
        );
    }

    @ParameterizedTest(name = "{index} => Проверяем {1}")
    @MethodSource("provideStrategies")
    void testStandardSort(SortingStrategy strategy, String name) throws InterruptedException {
        int[] input = {5, 1, 4, 2, 8};
        int[] expected = {1, 2, 4, 5, 8};

        strategy.sort(input, () -> {
        });

        assertArrayEquals(expected, input, "Алгоритм " + name + " отсортировал неправильно");
    }

    @ParameterizedTest(name = "{index} => Random Array {1}")
    @MethodSource("provideStrategies")
    void testRandomArray(SortingStrategy strategy, String name) throws InterruptedException {
        int[] input = new Random().ints(100, 0, 1000).toArray();
        int[] expected = Arrays.copyOf(input, input.length);
        Arrays.sort(expected);

        strategy.sort(input, () -> {
        });

        assertArrayEquals(expected, input, "Ошибка на случайном массиве в " + name);
    }

    @ParameterizedTest
    @MethodSource("provideStrategies")
    void testVisualizerCalled(SortingStrategy strategy, String name) throws InterruptedException {
        int[] input = {3, 2, 1};

        AtomicInteger updateCount = new AtomicInteger(0);

        strategy.sort(input, updateCount::incrementAndGet);

        assertTrue(updateCount.get() > 0,
                "Алгоритм " + name + " ни разу не вызвал визуализацию (onUpdate)!");
    }

    @ParameterizedTest
    @MethodSource("provideStrategies")
    void testEdgeCases(SortingStrategy strategy, String name) throws InterruptedException {
        int[] empty = {};
        strategy.sort(empty, () -> {
        });
        assertArrayEquals(new int[]{}, empty);

        int[] single = {42};
        strategy.sort(single, () -> {
        });
        assertArrayEquals(new int[]{42}, single);

        int[] duplicates = {5, 1, 5, 1};
        strategy.sort(duplicates, () -> {
        });
        assertArrayEquals(new int[]{1, 1, 5, 5}, duplicates);
    }
}