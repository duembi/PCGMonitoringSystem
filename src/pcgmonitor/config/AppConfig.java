package pcgmonitor.config;

public final class AppConfig {
    private AppConfig() {}
    public static final double DEFAULT_SAMPLING_RATE = 1000.0;
    public static final int DEFAULT_MOVING_AVERAGE_WINDOW = 5;
    public static final double DEFAULT_PEAK_THRESHOLD = 0.3;
}
