package pcgmonitor.utils;

import pcgmonitor.model.PeakAnalysisResult;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileWriterUtil {
    public static void writeAnalysisSummary(PeakAnalysisResult result, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Tepe Sayısı: " + result.getPeakCount() + "\n");
            writer.write("Kalp Atış Hızı (BPM): " + String.format("%.2f", result.getHeartRateBPM()) + "\n");
            writer.write("RR Aralıkları (ms):\n");
            for (double rr : result.getRRIntervals()) {
                writer.write(String.format("%.2f", rr) + "\n");
            }
        }
    }

    public static void exportListAsCSV(List<Double> data, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("index,value\n");
            for (int i = 0; i < data.size(); i++) {
                writer.write(i + "," + data.get(i) + "\n");
            }
        }
    }
}
