package pcgmonitor.view;

import pcgmonitor.utils.LiveAudioReader;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LivePCGMonitor extends JFrame {

    private XYSeries series;
    private LiveAudioReader reader;
    private ScheduledExecutorService scheduler;
    private int maxSampleCount = 500;
    private int timeIndex = 0;

    public LivePCGMonitor() throws LineUnavailableException {
        setTitle("Live PCG Monitor");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        reader = new LiveAudioReader();
        reader.start();

        series = new XYSeries("Live PCG");
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Live PCG Signal",
                "Time (frames)",
                "Amplitude",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(750, 400));

        JButton stopButton = new JButton("Durdur");
        stopButton.addActionListener(e -> stopMonitoring());

        JPanel controlPanel = new JPanel();
        controlPanel.add(stopButton);

        getContentPane().add(chartPanel, BorderLayout.CENTER);
        getContentPane().add(controlPanel, BorderLayout.SOUTH);

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::updateChart, 0, 100, TimeUnit.MILLISECONDS);
    }

    private void updateChart() {
        if (!reader.isRunning()) return;
        List<Double> chunk = reader.readChunk();
        SwingUtilities.invokeLater(() -> {
            for (Double sample : chunk) {
                series.add(timeIndex++, sample);
                if (series.getItemCount() > maxSampleCount) {
                    series.remove(0);
                }
            }
        });
    }

    private void stopMonitoring() {
        reader.stop();
        scheduler.shutdown();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new LivePCGMonitor().setVisible(true);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Mikrofon erisimi saglanamadi: " + e.getMessage());
            }
        });
    }
}
