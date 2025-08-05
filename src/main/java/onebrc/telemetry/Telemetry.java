package onebrc.telemetry;

public interface Telemetry {
    void recordMergeChunk(int partialSize);
}