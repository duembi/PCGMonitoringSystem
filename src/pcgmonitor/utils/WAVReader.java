package pcgmonitor.utils;

import pcgmonitor.model.PCGData;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WAVReader {

    public static PCGData readPCGData(File wavFile) throws UnsupportedAudioFileException, IOException {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(wavFile);
        AudioFormat format = audioStream.getFormat();

        float sampleRate = format.getSampleRate();
        int bytesPerFrame = format.getFrameSize();

        if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {
            throw new IOException("Belirlenemeyen çerçeve boyutu.");
        }

        byte[] audioBytes = audioStream.readAllBytes();
        List<Double> amplitudes = new ArrayList<>();
        List<Double> time = new ArrayList<>();

        int channels = format.getChannels();
        boolean isBigEndian = format.isBigEndian();
        boolean isSigned = format.getEncoding() == AudioFormat.Encoding.PCM_SIGNED;
        int sampleSizeInBits = format.getSampleSizeInBits();
        int bytesPerSample = sampleSizeInBits / 8;

        for (int i = 0, frameIndex = 0; i < audioBytes.length; i += bytesPerFrame, frameIndex++) {
            int value = 0;
            for (int byteIndex = 0; byteIndex < bytesPerSample; byteIndex++) {
                int shift = isBigEndian ? ((bytesPerSample - 1 - byteIndex) * 8) : (byteIndex * 8);
                int byteVal = audioBytes[i + byteIndex] & 0xFF;
                value += byteVal << shift;
            }
            if (isSigned && (value & (1 << (sampleSizeInBits - 1))) != 0) {
                value -= (1 << sampleSizeInBits);
            }
            amplitudes.add((double) value);
            time.add((double)frameIndex / sampleRate);
        }

        return new PCGData(amplitudes, time, sampleRate);
    }

    public static boolean isWavFile(File file) {
        return file.getName().toLowerCase().endsWith(".wav");
    }

    public static boolean isCsvFile(File file) {
        return file.getName().toLowerCase().endsWith(".csv");
    }

    public static PCGData read(File selectedFile, double defaultSamplingRate) throws Exception {
        if (isWavFile(selectedFile)) {
            return readPCGData(selectedFile);
        } else if (isCsvFile(selectedFile)) {
            return CSVReader.readPCGData(selectedFile.getAbsolutePath(), defaultSamplingRate);
        } else {
            throw new IOException("Desteklenmeyen dosya türü: " + selectedFile.getName());
        }
    }
}
