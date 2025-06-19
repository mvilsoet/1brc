package onebrc.core;

import onebrc.api.Reader;
import onebrc.api.Stats;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
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
                    // Submit parser task for this chunk
                    DefaultParser parser = new DefaultParser(new ArrayList<>(chunk));
                    futures.add(executor.submit(parser));
                    chunk.clear();
                }
            }
            if (!chunk.isEmpty()) {
                DefaultParser parser = new DefaultParser(new ArrayList<>(chunk));
                futures.add(executor.submit(parser));
            }

            // Wait for all tasks and reduce results
            for (Future<Map<String, Stats>> future : futures) {
                Map<String, Stats> partial = future.get();
                partial.forEach((name, partialStat) ->
                        combinedStats.merge(name, partialStat, (existing, incoming) -> {
                            existing.merge(incoming);
                            return existing;
                        })
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file concurrently: " + filename, e);
        }

        return new ArrayList<>(combinedStats.values());
    }
}
