import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelQuickSort {

    private final ForkJoinPool forkJoinPool;

    public ParallelQuickSort(ForkJoinPool forkJoinPool) {
        this.forkJoinPool = forkJoinPool;
    }

    public void sort(int[] array){
        forkJoinPool.invoke(new ParallelQuickSortTask(0, array.length - 1, array));
    }

    public static class ParallelQuickSortTask extends RecursiveTask<Integer> {
//    private static final int BLOCK = 100;

        private final int left;
        private final int right;
        private final int[] array;

        public ParallelQuickSortTask(int left, int right, int[] array) {
            this.array = array;
            this.left = left;
            this.right = right;
        }

        private int divide(int left, int right, int[] array) {
            int l = left;
            int r = right;
            int index = new Random().nextInt(r - l) + l;
            int tmp = array[r];
            array[r] = array[index];
            array[index] = tmp;
            r--;
            while (l <= r) {
                if (array[l] <= array[right]) {
                    l++;
                    continue;
                }
                if (array[r] >= array[right]) {
                    r--;
                    continue;
                }
                tmp = array[r];
                array[r] = array[l];
                array[l] = tmp;
                r--;
                l++;
            }
            tmp = array[r + 1];
            array[r + 1] = array[right];
            array[right] = tmp;
            return r + 1;
        }

        @Override
        protected Integer compute() {
            if (left >= right) return null;
            // if(right - left > BLOCK) {
            int middle = divide(left, right, array);
            ParallelQuickSortTask left = new ParallelQuickSortTask(this.left, middle - 1, array);
            ParallelQuickSortTask right = new ParallelQuickSortTask(middle + 1, this.right, array);
            left.fork();
            right.compute();
            left.join();
            return null;
//        } else{
//            new QuickSort().sort(array);
//            return null;
//        }
        }
    }
}