package onebrc.core;

import onebrc.api.Reader;
import onebrc.tool.Stats;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class DefaultReader implements Reader {
    private final String filename;
    private final ExecutorService executor;
    private static final int CHUNK_SIZE = 250000;

    public DefaultReader(String filename, ExecutorService executor) {
        this.filename = filename;
        this.executor = executor;
    }

    @Override
    public List<Stats> read() {
        Map<String, Stats> combinedStats = new ConcurrentHashMap<>();
        List<Future<Map<String, Stats>>> futures = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            List<String> chunk = new ArrayList<>(CHUNK_SIZE);
            String line;

            while ((line = reader.readLine()) != null) {
                chunk.add(line);
                if (chunk.size() == CHUNK_SIZE) {
                    submitChunk(new ArrayList<>(chunk), futures);
                    chunk.clear();
                }
            }

            if (!chunk.isEmpty()) {
                submitChunk(new ArrayList<>(chunk), futures);
            }

            for (Future<Map<String, Stats>> future : futures) {
                Map<String, Stats> partial = future.get();
                reduceChunk(combinedStats, partial);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file concurrently: " + filename, e);
        }

        return new ArrayList<>(combinedStats.values());
    }

    private void submitChunk(List<String> chunk, List<Future<Map<String, Stats>>> futures) {
        DefaultParser parser = new DefaultParser(chunk.stream());
        futures.add(executor.submit(parser));
    }

    private void reduceChunk(Map<String, Stats> combinedStats, Map<String, Stats> partial) {
        partial.forEach((name, partialStat) ->
                combinedStats.merge(name, partialStat, (existing, incoming) -> {
                    existing.merge(incoming);
                    return existing;
                })
        );
    }
}
