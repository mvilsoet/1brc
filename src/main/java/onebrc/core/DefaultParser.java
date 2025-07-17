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

        lines.filter(line -> !line.trim().isEmpty())  // trim() leading+trailing whitespace, skip empty lines
                .forEach(line -> {
                    String[] parts = line.split(";");
                    if (parts.length < 2) return;  // skip malformed line where ; is missing, can't recover that data
                    String stationName = parts[0];

                    double value;  // more malformed safety. "try" to get value with correct dtype
                    try {
                        value = Double.parseDouble(parts[1]);
                    } catch (NumberFormatException e) {
                        throw new RuntimeException("Malformed number: " + e.getMessage());
                    }
                    // check if station has been seen by this DefaultParser before, update map
                    Stats stat = partialStats.get(stationName);
                    if (stat == null) {  //
                        partialStats.put(stationName, new Stats(stationName, value));
                    } else {
                        stat.add(value);
                    }
                });

        return partialStats;
    }
}
