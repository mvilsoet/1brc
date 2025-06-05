package onebrc.tool;

import onebrc.core.DefaultReader;
import onebrc.core.DefaultParser;
import onebrc.core.DefaultReportGenerator;
import onebrc.api.Stats;

import java.util.List;
import java.util.concurrent.Future;

public class App {
    public static void main(String[] args) throws Exception {
        DefaultReader reader = new DefaultReader();
        DefaultReportGenerator generator = new DefaultReportGenerator();

        // Suppose reader.read() returns a List<Stats> of size 1_000_000_000
        List<Stats> allStats = reader.read();

        int chunkSize = 100_000;
        for (int offset = 0; offset < 1_000_000_000; offset += chunkSize) {
            DefaultParser parser = new DefaultParser();
            Future<List<Stats>> futureChunk = parser.parse(offset, offset + chunkSize);
            List<Stats> chunk = futureChunk.get();
            generator.generate(chunk);
        }
    }
}
