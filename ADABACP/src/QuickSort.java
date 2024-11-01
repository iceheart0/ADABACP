import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class QuickSort {

    public static void sortSequential(int[] array, int low, int high) {
        if (low < high) {
            int pi = partition(array, low, high);
            sortSequential(array, low, pi - 1);
            sortSequential(array, pi + 1, high);
        }
    }

    public static void sortParallel(int[] array, int threads) {
        ForkJoinPool pool = new ForkJoinPool(threads);
        pool.invoke(new ParallelQuickSort(array, 0, array.length - 1));
        pool.shutdown();
    }

    private static class ParallelQuickSort extends RecursiveAction {
        private final int[] array;
        private final int low;
        private final int high;

        public ParallelQuickSort(int[] array, int low, int high) {
            this.array = array;
            this.low = low;
            this.high = high;
        }

        @Override
        protected void compute() {
            if (high - low < 1000) {
                QuickSort.sortSequential(array, low, high);
            } else {
                int pi = partition(array, low, high);
                ParallelQuickSort leftTask = new ParallelQuickSort(array, low, pi - 1);
                ParallelQuickSort rightTask = new ParallelQuickSort(array, pi + 1, high);
                invokeAll(leftTask, rightTask);
            }
        }
    }

    private static int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (array[j] <= pivot) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        return i + 1;
    }
}