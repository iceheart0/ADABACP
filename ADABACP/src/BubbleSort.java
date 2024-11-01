import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class BubbleSort {

    public static void sortSequential(int[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

    public static void sortParallel(int[] array, int threads) {
        ForkJoinPool pool = new ForkJoinPool(threads);
        pool.invoke(new ParallelBubbleSort(array, 0, array.length - 1));
        pool.shutdown();
    }

    private static class ParallelBubbleSort extends RecursiveAction {
        private final int[] array;
        private final int left;
        private final int right;

        public ParallelBubbleSort(int[] array, int left, int right) {
            this.array = array;
            this.left = left;
            this.right = right;
        }

        @Override
        protected void compute() {
            if (right - left < 1000) {
                BubbleSort.sortSequential(array);
            } else {
                int mid = (left + right) / 2;
                ParallelBubbleSort leftTask = new ParallelBubbleSort(array, left, mid);
                ParallelBubbleSort rightTask = new ParallelBubbleSort(array, mid + 1, right);
                invokeAll(leftTask, rightTask);
            }
        }
    }
}