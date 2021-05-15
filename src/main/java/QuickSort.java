import java.util.Random;

public class QuickSort {

    public void sort(int[] a) {
        sort(a, 0, a.length - 1);
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

    private void sort(int[] array, int left, int right) {
        if(right <= left) return;
        int middle = divide(left, right, array);
        sort(array, left, middle - 1);
        sort(array, middle + 1, right);
    }
}