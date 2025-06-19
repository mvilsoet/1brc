package onebrc.core;

import onebrc.api.Parser;
import onebrc.api.Stats;

import java.util.*;

public class DefaultParser implements Parser {
    private final List<String> lines;

    public DefaultParser(List<String> lines) {
        this.lines = lines;
    }

    @Override
    public Map<String, Stats> call() {
        Map<String, Stats> partialStats = new HashMap<>();

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(";");
            if (parts.length < 2) continue;

            String name = parts[0];
            double value;
            try {
                value = Double.parseDouble(parts[1]);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Malformed number: " + e.getMessage());
            }

            Stats stat = partialStats.get(name);
            if (stat == null) {
                partialStats.put(name, new Stats(name, value)); // count=1 here
            } else {
                stat.add(value);
            }
        }
        return partialStats;
    }

}
