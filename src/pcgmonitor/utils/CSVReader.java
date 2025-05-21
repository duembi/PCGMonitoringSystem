package pcgmonitor.utils;

import pcgmonitor.model.PCGData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    public static PCGData readPCGData(String filePath, double samplingRate) throws IOException {
        List<Double> timeList = new ArrayList<>();
        List<Double> signalList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean skipHeader = true;
            while ((line = br.readLine()) != null) {
                if (skipHeader) { skipHeader = false; continue; }
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    double time = Double.parseDouble(parts[0].trim());
                    double signal = Double.parseDouble(parts[1].trim());
                    timeList.add(time);
                    signalList.add(signal);
                }
            }
        }
        return new PCGData(signalList, timeList, samplingRate);
    }
}
