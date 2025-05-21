package pcgmonitor.model;

import java.util.ArrayList;
import java.util.List;

public class SignalProcessor {
    public static List<Double> normalize(List<Double> data) {
        double min = data.stream().min(Double::compareTo).orElse(0.0);
        double max = data.stream().max(Double::compareTo).orElse(1.0);
        double range = max - min;
        List<Double> normalized = new ArrayList<>();
        for (double d : data) {
            normalized.add((d - min) / range);
        }
        return normalized;
    }

    public static List<Double> movingAverage(List<Double> data, int windowSize) {
        List<Double> smoothed = new ArrayList<>();
        int halfWindow = windowSize / 2;
        for (int i = 0; i < data.size(); i++) {
            double sum = 0.0;
            int count = 0;
            for (int j = i - halfWindow; j <= i + halfWindow; j++) {
                if (j >= 0 && j < data.size()) {
                    sum += data.get(j);
                    count++;
                }
            }
            smoothed.add(sum / count);
        }
        return smoothed;
    }

    public static List<Integer> detectPeaks(List<Double> data, double threshold) {
        List<Integer> peaks = new ArrayList<>();
        for (int i = 1; i < data.size() - 1; i++) {
            if (data.get(i) > data.get(i - 1) && data.get(i) > data.get(i + 1) && data.get(i) > threshold) {
                peaks.add(i);
            }
        }
        return peaks;
    }

    public static double calculateHeartRate(List<Integer> peakIndices, double samplingRate) {
        if (peakIndices.size() < 2) return 0.0;
        double totalInterval = 0.0;
        for (int i = 1; i < peakIndices.size(); i++) {
            totalInterval += (peakIndices.get(i) - peakIndices.get(i - 1));
        }
        double averageInterval = totalInterval / (peakIndices.size() - 1);
        return (samplingRate * 60.0) / averageInterval;
    }

    public static PeakAnalysisResult analyzePeaks(List<Double> signal, double samplingRate, double threshold) {
        List<Integer> peaks = detectPeaks(signal, threshold);
        double bpm = calculateHeartRate(peaks, samplingRate);
        List<Double> rrIntervals = new ArrayList<>();
        for (int i = 1; i < peaks.size(); i++) {
            int diff = peaks.get(i) - peaks.get(i - 1);
            rrIntervals.add((diff * 1000.0) / samplingRate);
        }
        return new PeakAnalysisResult(peaks, bpm, rrIntervals);
    }
}
