import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class CountingSort {

    // Método para ordenação sequencial
    public static void sortSequential(int[] array, int maxValue) {
        int[] count = new int[maxValue + 1];

        // Contagem das ocorrências
        for (int value : array) {
            count[value]++;
        }

        // Cálculo do prefixo
        for (int i = 1; i <= maxValue; i++) {
            count[i] += count[i - 1];
        }

        // Construção do array ordenado
        int[] sorted = new int[array.length];
        for (int i = array.length - 1; i >= 0; i--) {
            sorted[--count[array[i]]] = array[i];
        }

        // Copia os valores ordenados de volta ao array original
        System.arraycopy(sorted, 0, array, 0, array.length);
    }

    // Método para ordenação paralela
    public static void sortParallel(int[] array, int maxValue, int threads) {
        int[] count = new int[maxValue + 1];
        ForkJoinPool pool = new ForkJoinPool(threads);
        pool.invoke(new ParallelCount(array, count, 0, array.length, maxValue));
        pool.shutdown();

        // Calcula o prefixo do array count
        for (int i = 1; i <= maxValue; i++) {
            count[i] += count[i - 1];
        }

        // Constrói o array ordenado
        int[] sorted = new int[array.length];
        for (int i = array.length - 1; i >= 0; i--) {
            sorted[--count[array[i]]] = array[i];
        }

        // Copia os valores ordenados de volta ao array original
        System.arraycopy(sorted, 0, array, 0, array.length);
    }

    // Classe interna para executar a contagem em paralelo
    private static class ParallelCount extends RecursiveAction {
        private int[] array;
        private int[] count;
        private int start;
        private int end;
        private int maxValue;

        public ParallelCount(int[] array, int[] count, int start, int end, int maxValue) {
            this.array = array;
            this.count = count;
            this.start = start;
            this.end = end;
            this.maxValue = maxValue;
        }

        @Override
        protected void compute() {
            // Definindo um limite para o processamento sequencial
            if (end - start < 1000) {
                sequentialCount(array, count, start, end);
            } else {
                // Divide o array em duas partes e processa recursivamente
                int mid = (start + end) / 2;
                ParallelCount leftTask = new ParallelCount(array, count, start, mid, maxValue);
                ParallelCount rightTask = new ParallelCount(array, count, mid, end, maxValue);
                invokeAll(leftTask, rightTask);
            }
        }

        // Método auxiliar para realizar a contagem sequencial em um intervalo
        private void sequentialCount(int[] array, int[] count, int start, int end) {
            for (int i = start; i < end; i++) {
                synchronized (count) {  // Sincroniza o acesso ao array count
                    count[array[i]]++;
                }
            }
        }
    }
}