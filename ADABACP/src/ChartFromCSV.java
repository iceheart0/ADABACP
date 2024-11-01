import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ChartFromCSV extends JFrame {

    public ChartFromCSV(String title) {
        super(title);

        // Configura o painel do gráfico
        //CategoryDataset dataset = createDataset("bubbleResults.csv");
        CategoryDataset dataset = createDataset("results.csv");
        JFreeChart chart = ChartFactory.createBarChart(
                "Desempenho dos Algoritmos de Ordenação",
                "Algoritmos",
                "Tempo (ms)",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setContentPane(chartPanel);
    }

    private CategoryDataset createDataset(String filename) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String algorithm = data[0];
                int dataSize = Integer.parseInt(data[1]);
                int numThreads = Integer.parseInt(data[2]);

                // Adiciona cada tempo de execução no dataset
                for (int i = 3; i < data.length; i++) {
                    int time = Integer.parseInt(data[i]);
                    dataset.addValue(time, algorithm + " (Threads: " + numThreads + ")", "Tamanho: " + dataSize);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataset;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChartFromCSV example = new ChartFromCSV("Gráficos de Desempenho");
            example.setSize(800, 600);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setLocationRelativeTo(null);
            example.setVisible(true);
        });
    }
}