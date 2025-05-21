package pcgmonitor.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class PCGView extends JFrame {

    private JButton loadButton;
    private JButton analyzeButton;
    private JButton saveButton;
    private JTextArea outputArea;
    private JPanel topPanel;

    public PCGView() {
        setTitle("Phonocardiogram Monitoring System");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        loadButton = new JButton("Dosya Yükle");
        analyzeButton = new JButton("Analiz Et");
        saveButton = new JButton("Sonuçları Kaydet");

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);

        topPanel = new JPanel();
        topPanel.add(loadButton);
        topPanel.add(analyzeButton);
        topPanel.add(saveButton);

        JScrollPane scrollPane = new JScrollPane(outputArea);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void showMessage(String msg) {
        outputArea.append(msg + "\n");
    }

    public void setLoadButtonListener(java.awt.event.ActionListener listener) {
        loadButton.addActionListener(listener);
    }

    public void setAnalyzeButtonListener(java.awt.event.ActionListener listener) {
        analyzeButton.addActionListener(listener);
    }

    public void setSaveButtonListener(java.awt.event.ActionListener listener) {
        saveButton.addActionListener(listener);
    }

    public void showChart(List<Double> data, double samplingRate) {
        XYSeries series = new XYSeries("PCG Signal");
        for (int i = 0; i < data.size(); i++) {
            series.add(i / samplingRate, data.get(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
            "Phonocardiogram Signal",
            "Time (s)",
            "Amplitude",
            dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(700, 400));

        getContentPane().removeAll();
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(chartPanel, BorderLayout.CENTER);
        revalidate();
    }
}
