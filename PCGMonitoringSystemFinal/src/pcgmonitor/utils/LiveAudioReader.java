package pcgmonitor.utils;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.List;

public class LiveAudioReader {

    private TargetDataLine microphone;
    private AudioFormat format;
    private boolean running;
    private int bufferSize = 2048;

    public LiveAudioReader() throws LineUnavailableException {
        format = new AudioFormat(44100, 16, 1, true, false); // 44.1kHz, 16-bit, mono, signed, little-endian
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            throw new LineUnavailableException("Microphone line not supported");
        }
        microphone = (TargetDataLine) AudioSystem.getLine(info);
        microphone.open(format);
    }

    public void start() {
        running = true;
        microphone.start();
    }

    public void stop() {
        running = false;
        microphone.stop();
        microphone.close();
    }

    public List<Double> readChunk() {
        byte[] buffer = new byte[bufferSize];
        int bytesRead = microphone.read(buffer, 0, buffer.length);
        List<Double> samples = new ArrayList<>();

        for (int i = 0; i < bytesRead; i += 2) {
            int low = buffer[i] & 0xFF;
            int high = buffer[i + 1];
            int value = (high << 8) | low;
            if (value > 32767) value -= 65536;
            samples.add((double) value);
        }
        return samples;
    }

    public boolean isRunning() {
        return running;
    }

    public float getSamplingRate() {
        return format.getSampleRate();
    }
}
