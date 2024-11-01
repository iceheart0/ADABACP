import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class Benchmark {

    // Função para medir o tempo com pool de threads específico (simulação de núcleos disponíveis)
    public static long measureTimeWithPool(Runnable algorithm, int maxThreads) {
        ForkJoinPool pool = new ForkJoinPool(maxThreads);
        long startTime = System.nanoTime();
        pool.submit(algorithm).join();
        pool.shutdown();
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
    }

    public static void main(String[] args) {
        int[] dataSizes = {10000, 50000, 100000};  // Tamanhos de dados para testar
        int[] threadCounts = {1, 2, 4, 8};  // Números de threads para simular núcleos de processamento

        for (int size : dataSizes) {
            int[] data = generateRandomArray(size);

            // Lista para armazenar os tempos de execução
            List<Long> times;

            // ===== BUBBLE SORT =====
            times = new ArrayList<>();
            for (int t = 0; t < 5; t++) {  // Execução sequencial com 5 amostras
                times.add(measureTimeWithPool(() -> BubbleSort.sortSequential(data.clone()), 1));
            }
            CSVWriter.writeResultsToCSV("bubbleResults.csv", times, size, "BubbleSort", 0);

            for (int threads : threadCounts) {
                times = new ArrayList<>();
                for (int t = 0; t < 5; t++) {  // Execução paralela com 5 amostras
                    times.add(measureTimeWithPool(() -> BubbleSort.sortParallel(data.clone(), threads), threads));
                }
                CSVWriter.writeResultsToCSV("bubbleResults.csv", times, size, "BubbleSort", threads);
            }

            // ===== QUICK SORT =====
            times = new ArrayList<>();
            for (int t = 0; t < 5; t++) {  // Execução sequencial com 5 amostras
                times.add(measureTimeWithPool(() -> QuickSort.sortSequential(data.clone(), 0, data.length - 1), 1));
            }
            CSVWriter.writeResultsToCSV("results.csv", times, size, "QuickSort", 0);

            for (int threads : threadCounts) {
                times = new ArrayList<>();
                for (int t = 0; t < 5; t++) {  // Execução paralela com 5 amostras
                    times.add(measureTimeWithPool(() -> QuickSort.sortParallel(data.clone(), threads), threads));
                }
                CSVWriter.writeResultsToCSV("results.csv", times, size, "QuickSort", threads);
            }

            // ===== MERGE SORT =====
            times = new ArrayList<>();
            for (int t = 0; t < 5; t++) {  // Execução sequencial com 5 amostras
                times.add(measureTimeWithPool(() -> MergeSort.sortSequential(data.clone(), 0, data.length - 1), 1));
            }
            CSVWriter.writeResultsToCSV("results.csv", times, size, "MergeSort", 0);

            for (int threads : threadCounts) {
                times = new ArrayList<>();
                for (int t = 0; t < 5; t++) {  // Execução paralela com 5 amostras
                    times.add(measureTimeWithPool(() -> MergeSort.sortParallel(data.clone(), threads), threads));
                }
                CSVWriter.writeResultsToCSV("results.csv", times, size, "MergeSort", threads);
            }

            // ===== COUNTING SORT =====
            int maxValue = size;  // Definimos o valor máximo igual ao tamanho dos dados para simplificar
            times = new ArrayList<>();
            for (int t = 0; t < 5; t++) {  // Execução sequencial com 5 amostras
                times.add(measureTimeWithPool(() -> CountingSort.sortSequential(data.clone(), maxValue), 1));
            }
            CSVWriter.writeResultsToCSV("results.csv", times, size, "CountingSort", 0);

            for (int threads : threadCounts) {
                times = new ArrayList<>();
                for (int t = 0; t < 5; t++) {  // Execução paralela com 5 amostras
                    times.add(measureTimeWithPool(() -> CountingSort.sortParallel(data.clone(), maxValue, threads), threads));
                }
                CSVWriter.writeResultsToCSV("results.csv", times, size, "CountingSort", threads);
            }
        }
    }

    // Método auxiliar para gerar um array aleatório
    private static int[] generateRandomArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) array[i] = (int)(Math.random() * size);
        return array;
    }
}