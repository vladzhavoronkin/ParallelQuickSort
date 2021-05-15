import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.*;

class ParallelQuickSortTest {

    int[] createArray() {
        int[] array = new int[1_000_000];
        for (int i = 0; i < array.length; i++) {
            array[i] = (int) (Math.random() * 1000);
        }
        return array;
    }

    int[] array = createArray();

    @Test
    void quickSort() {
        QuickSort quickSort = new QuickSort();
        int[] array1 = array.clone();
        long start = System.currentTimeMillis();
        quickSort.sort(array);
        System.out.print(System.currentTimeMillis() - start);
        System.out.println(" Seq");
        Arrays.sort(array1);
        assertArrayEquals(array1, array);
    }

    @Test
    void quickSParallel1() {
        ParallelQuickSort quickSort = new ParallelQuickSort(new ForkJoinPool(1));
        int[] array1 = array.clone();
//        ForkJoinPool forkJoinPool = new ForkJoinPool(1);
        long start = System.currentTimeMillis();
//        forkJoinPool.invoke(new ParallelQuickSort(0, array.length - 1, array));
        quickSort.sort(array);
        System.out.print(System.currentTimeMillis() - start);
        System.out.println(" 1 thread");
        Arrays.sort(array1);
        assertArrayEquals(array1, array);
    }

    @Test
    void quickSParallel2() {
        ParallelQuickSort quickSort = new ParallelQuickSort(new ForkJoinPool(2));
        int[] array1 = array.clone();
        long start = System.currentTimeMillis();
        quickSort.sort(array);
        System.out.print(System.currentTimeMillis() - start);
        System.out.println(" 2 thread");
        Arrays.sort(array1);
        assertArrayEquals(array1, array);
    }

    @Test
    void quickSParallel3() {
        ParallelQuickSort quickSort = new ParallelQuickSort(new ForkJoinPool(3));
        int[] array1 = array.clone();
        long start = System.currentTimeMillis();
        quickSort.sort(array);
        System.out.print(System.currentTimeMillis() - start);
        System.out.println(" 3 thread");
        Arrays.sort(array1);
        assertArrayEquals(array1, array);
    }

    @Test
    void quickSParallel4() {
        ParallelQuickSort quickSort = new ParallelQuickSort(new ForkJoinPool(4));
        int[] array1 = array.clone();
        long start = System.currentTimeMillis();
        quickSort.sort(array);
        System.out.print(System.currentTimeMillis() - start);
        System.out.println(" 4 thread");
        Arrays.sort(array1);
        assertArrayEquals(array1, array);
    }
}