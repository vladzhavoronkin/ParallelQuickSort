import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ParallelQSort {
    ForkJoinPool forkJoinPool;

    ParallelQSort(ForkJoinPool forkJoinPool){
        this.forkJoinPool = forkJoinPool;
    }

    public void pFor(int left, int right, Consumer<Integer> function) {
        if (left < right) {
            try {
                ForkJoinTask<Void> forkJoinTask = ForkJoinPool.commonPool().
                        submit(new ParallelSortAction(left, right, function));
                forkJoinTask.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    void blockedFor (int left, int right, Consumer<Integer> function, int block){
        pFor(left, (right - left) / block + 1, function);
    }

//    public void blockedFor(int left, int right, Consumer<Integer> function, int block) {
//        int blockAmount = (right - left) / block;
//        int rest = (right - left) % block;
//        pFor(0, blockAmount, b -> {
//            for (int i = 0; i < (b == blockAmount ? rest : block); i++) {
//                function.accept(b * block + i);
//            }
//        });
//    }

    private int[] map(int[] array, Function<Integer, Integer> function, int block) {
        int[] result = new int[array.length];
        Consumer<Integer> func = i -> {
            for (int j = 0; j < block; j++) {
                int index = i * block + j;
                if (index > array.length - 1) return;
                result[index] = function.apply(array[i]);
            }
        };
        blockedFor(0, array.length - 1, func, block);
        System.out.println(Arrays.toString(result));
        return result;
    }

    private int reduceSerial(int[] array, BiFunction<Integer, Integer, Integer> biFunction) {
        int result = 0;
        for (int i = 0; i < array.length; i++) {
            result = biFunction.apply(array[i], result);
        }
        return result;
    }

    private int[] scanSerial(int[] array, BiFunction<Integer, Integer, Integer> biFunction) {
        int result[] = new int[array.length];
        result[0] = array[0];
        for (int i = 1; i < array.length; i++) {
            result[i] = biFunction.apply(result[i - 1], array[i]);
        }
        return result;
    }

    private int[] scan(int[] array, BiFunction<Integer, Integer, Integer> biFunction, int block) {
        if (array.length < block) return scanSerial(array, biFunction);
        int blockAmount = array.length / block;
        int[] sums = new int[blockAmount + 1];
        sums[0] = 0;
        blockedFor(0,
                array.length - 1,
                i -> {
                    sums[i + 1] = reduceSerial(Arrays.copyOfRange(array, i * block, (i + 1) * block),
                            Integer::sum);
                },
                block);
        System.out.println(Arrays.toString(sums));
        int[] sums1 = scanSerial(sums, biFunction);
        int[] result = new int[array.length + 1];
        blockedFor(0, array.length + 1,
                i -> {
                    if (i % block == 0) result[i] = sums1[(i / block)];
                    else result[i] = biFunction.apply(result[i - 1], array[i - 1]);
                }, block);
        System.out.println(Arrays.toString(result));
        return result;
    }

    private int[] filterSerial(int[] array, Predicate<Integer> predicate) {
        int[] result = new int[array.length + 1];
        int index = 0;
        for (int i = 0; i < array.length; i++) {
            if (predicate.test(array[i])) {
                result[index] = array[i];
                index++;
            }
        }
        return result;
    }

    public int[] filter(int[] array, Predicate<Integer> predicate, int block) {
        if (array.length < block) return filterSerial(array, predicate);
        int[] flags = map(array, (x) -> predicate.test(x) ? 1 : 0, block);
        int[] sums = scan(flags, Integer::sum, block);
        int[] result = new int[sums[array.length]];
        blockedFor(0, array.length - 1, i -> {
            if (predicate.test(array[i]))
                result[sums[i]] = array[i];
        }, block);
//        for (int i = 0; i < sums.length; i++) {
//            System.out.println(sums[i]);
//        }
        return result;
    }

    private static class ParallelSortAction extends RecursiveAction {
        private final int bubbleBlock = 16;

        int left;
        int right;
        Consumer<Integer> function;

        ParallelSortAction(int left, int right, Consumer<Integer> function) {
            this.left = left;
            this.right = right;
            this.function = function;
        }

        @Override
        protected void compute() {
            if (left == right - 1) {
                function.accept(left);
                return;
            }

            // Находим средний элемент
            int middle = (left + right) / 2;

            // Рекусивное вызов левой / правой подчасти
            invokeAll(new ParallelQSort.ParallelSortAction(left, middle, function),
                    new ParallelQSort.ParallelSortAction(middle, right, function));
        }
    }
}
