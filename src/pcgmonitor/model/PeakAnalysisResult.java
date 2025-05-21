package pcgmonitor.model;

import java.util.Collections;
import java.util.List;

public class PeakAnalysisResult {
    private final List<Integer> peakIndices;
    private final double heartRateBPM;
    private final List<Double> rrIntervals;

    public PeakAnalysisResult(List<Integer> peakIndices, double heartRateBPM, List<Double> rrIntervals) {
        this.peakIndices = List.copyOf(peakIndices);
        this.heartRateBPM = heartRateBPM;
        this.rrIntervals = List.copyOf(rrIntervals);
    }

    public List<Integer> getPeakIndices() { return Collections.unmodifiableList(peakIndices); }
    public double getHeartRateBPM() { return heartRateBPM; }
    public List<Double> getRRIntervals() { return Collections.unmodifiableList(rrIntervals); }
    public int getPeakCount() { return peakIndices.size(); }
}
