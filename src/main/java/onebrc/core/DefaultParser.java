package onebrc.core;

import onebrc.api.Parser;
import onebrc.tool.Stats;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

public class DefaultParser implements Parser, Callable<Map<String, Stats>> {
    private final Stream<String> lines;

    public DefaultParser(Stream<String> lines) {
        this.lines = lines;
    }

    @Override
    public Map<String, Stats> call() {
        Map<String, Stats> partialStats = new HashMap<>();

        lines.filter(line -> !line.trim().isEmpty())
                .forEach(line -> {
                    String[] parts = line.split(";");
                    if (parts.length < 2) return;
                    String name = parts[0];
                    double value;
                    try {
                        value = Double.parseDouble(parts[1]);
                    } catch (NumberFormatException e) {
                        throw new RuntimeException("Malformed number: " + e.getMessage());
                    }

                    Stats stat = partialStats.get(name);
                    if (stat == null) {
                        partialStats.put(name, new Stats(name, value));
                    } else {
                        stat.add(value);
                    }
                });

        return partialStats;
    }
}
