package onebrc.telemetry;

public class NoOpTelemetry implements Telemetry {
    @Override public void recordMergeChunk(int partialSize) { /* no‐op */ }
}