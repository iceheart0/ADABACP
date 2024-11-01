import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MergeSort {

    public static void sortSequential(int[] array, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            sortSequential(array, left, mid);
            sortSequential(array, mid + 1, right);
            merge(array, left, mid, right);
        }
    }

    public static void sortParallel(int[] array, int threads) {
        ForkJoinPool pool = new ForkJoinPool(threads);
        pool.invoke(new ParallelMergeSort(array, 0, array.length - 1));
        pool.shutdown();
    }

    private static class ParallelMergeSort extends RecursiveAction {
        private final int[] array;
        private final int left;
        private final int right;

        public ParallelMergeSort(int[] array, int left, int right) {
            this.array = array;
            this.left = left;
            this.right = right;
        }

        @Override
        protected void compute() {
            if (right - left < 1000) {
                MergeSort.sortSequential(array, left, right);
            } else {
                int mid = (left + right) / 2;
                ParallelMergeSort leftTask = new ParallelMergeSort(array, left, mid);
                ParallelMergeSort rightTask = new ParallelMergeSort(array, mid + 1, right);
                invokeAll(leftTask, rightTask);
                merge(array, left, mid, right);
            }
        }
    }

    private static void merge(int[] array, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        System.arraycopy(array, left, leftArray, 0, n1);
        System.arraycopy(array, mid + 1, rightArray, 0, n2);

        int i = 0, j = 0;
        int k = left;
        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                array[k++] = leftArray[i++];
            } else {
                array[k++] = rightArray[j++];
            }
        }
        while (i < n1) array[k++] = leftArray[i++];
        while (j < n2) array[k++] = rightArray[j++];
    }
}