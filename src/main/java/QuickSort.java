import java.util.Random;

public class QuickSort {
    public void sort(int[] a) {
        sort(a, 0, a.length - 1);
    }

    private int partition(int start, int end, int[] arr)
    {
        int i = start, j = end;
        int pivote = new Random().nextInt(j - i) + i;
        int t = arr[j];
        arr[j] = arr[pivote];
        arr[pivote] = t;
        j--;
        while (i <= j) {
            if (arr[i] <= arr[end]) {
                i++;
                continue;
            }
            if (arr[j] >= arr[end]) {
                j--;
                continue;
            }
            t = arr[j];
            arr[j] = arr[i];
            arr[i] = t;
            j--;
            i++;
        }
        t = arr[j + 1];
        arr[j + 1] = arr[end];
        arr[end] = t;
        return j + 1;
    }

    private void sort(int[] a, int lo, int hi) {
        if(hi <= lo) return;

        // Находим средний элемент
        int j = partition(lo, hi, a);

        // Рекусивное вызов левой / правой подчасти
        sort(a, lo, j - 1);
        sort(a, j + 1, hi);
    }
}