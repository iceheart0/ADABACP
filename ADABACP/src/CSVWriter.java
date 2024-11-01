import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWriter {
    public static void writeResultsToCSV(String filename, List<Long> times, int dataSize, String algorithm, int numThreads) {
        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.append(algorithm)
                    .append(',')
                    .append(String.valueOf(dataSize))
                    .append(',')
                    .append(String.valueOf(numThreads))
                    .append(',');
            for (Long time : times) writer.append(String.valueOf(time)).append(',');
            writer.append('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}