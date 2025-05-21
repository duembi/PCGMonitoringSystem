package pcgmonitor.model;

import java.util.Collections;
import java.util.List;

public class PCGData {
    private final List<Double> signalData;
    private final List<Double> timeStamps;
    private final double samplingRate;

    public PCGData(List<Double> signalData, List<Double> timeStamps, double samplingRate) {
        if (signalData == null || timeStamps == null || signalData.size() != timeStamps.size()) {
            throw new IllegalArgumentException("Veri listeleri hatalı.");
        }
        this.signalData = List.copyOf(signalData);
        this.timeStamps = List.copyOf(timeStamps);
        this.samplingRate = samplingRate;
    }

    public List<Double> getSignalData() { return Collections.unmodifiableList(signalData); }
    public List<Double> getTimeStamps() { return Collections.unmodifiableList(timeStamps); }
    public double getSamplingRate() { return samplingRate; }
    public int getLength() { return signalData.size(); }
    public double getDurationInSeconds() { return signalData.size() / samplingRate; }
}
