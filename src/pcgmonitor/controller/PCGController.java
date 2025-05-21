package pcgmonitor.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;

import pcgmonitor.config.AppConfig;
import pcgmonitor.model.PCGData;
import pcgmonitor.model.PeakAnalysisResult;
import pcgmonitor.model.SignalProcessor;
import pcgmonitor.utils.CSVReader;
import pcgmonitor.utils.FileWriterUtil;
import pcgmonitor.utils.WAVReader;
import pcgmonitor.view.PCGView;

public class PCGController {

    private PCGView view;
    private PCGData currentData;
    private PeakAnalysisResult lastResult;
    private List<Double> lastSmoothedSignal;

    public PCGController(PCGView view) {
        this.view = view;
        this.view.setLoadButtonListener(new LoadFileListener());
        this.view.setAnalyzeButtonListener(new AnalyzeListener());
        this.view.setSaveButtonListener(new SaveResultsListener());
    }

    private class LoadFileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    currentData = WAVReader.read(selectedFile, AppConfig.DEFAULT_SAMPLING_RATE);
                    view.showMessage("Dosya yüklendi: " + selectedFile.getName());
                } catch (Exception ex) {
                    view.showMessage("Dosya okunamadı: " + ex.getMessage());
                    ex.printStackTrace();

                }
            }
        }
    }

    private class AnalyzeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentData == null) {
                view.showMessage("Lütfen önce bir dosya yükleyin.");
                return;
            }
            lastSmoothedSignal = SignalProcessor.movingAverage(currentData.getSignalData(), AppConfig.DEFAULT_MOVING_AVERAGE_WINDOW);
            lastResult = SignalProcessor.analyzePeaks(lastSmoothedSignal, AppConfig.DEFAULT_SAMPLING_RATE, AppConfig.DEFAULT_PEAK_THRESHOLD);
            view.showMessage("Tepe sayısı: " + lastResult.getPeakCount());
            view.showMessage("Tahmini Kalp Atış Hızı: " + String.format("%.2f", lastResult.getHeartRateBPM()) + " BPM");
            view.showChart(lastSmoothedSignal, currentData.getSamplingRate());
        }
    }

    private class SaveResultsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (lastResult == null || lastSmoothedSignal == null) {
                view.showMessage("Önce analiz yapmalısınız.");
                return;
            }
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Kaydetme Konumu Seç");
            fileChooser.setSelectedFile(new File("analysis_summary.txt"));
            int option = fileChooser.showSaveDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    FileWriterUtil.writeAnalysisSummary(lastResult, file.getAbsolutePath());
                    FileWriterUtil.exportListAsCSV(lastSmoothedSignal, file.getParent() + "/filtered_signal.csv");
                    view.showMessage("Sonuçlar kaydedildi: " + file.getAbsolutePath());
                } catch (Exception ex) {
                    view.showMessage("Kaydetme sırasında hata oluştu: " + ex.getMessage());
                }
            }
        }
    }
}
