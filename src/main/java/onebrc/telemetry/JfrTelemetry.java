package onebrc.telemetry;

import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Category;
import jdk.jfr.Name;

public class JfrTelemetry implements Telemetry {
    @Name("onebrc.telemetry.MergeChunk")
    @Label("MergeChunk")
    @Category("onebrc.metrics")
    public static class MergeChunkEvent extends Event {
        @Label("Partial Size")
        int partialSize;
    }

    @Override
    public void recordMergeChunk(int partialSize) {
        MergeChunkEvent evt = new MergeChunkEvent();
        evt.partialSize = partialSize;
        evt.commit();
    }
}
