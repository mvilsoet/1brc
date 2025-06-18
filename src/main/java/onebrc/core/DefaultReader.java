package onebrc.core;

import onebrc.api.Reader;
import onebrc.api.Stats;
import java.nio.file.*;
import java.util.*;
import java.io.*;

public class DefaultReader implements Reader {
    private final String filename;

    public DefaultReader(String filename) {
        this.filename = filename;
    }

    @Override
    public List<Stats> read() {
        Map<String, Stats> statsMap = new HashMap<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(";");
                String name = parts[0];
                double value = Double.parseDouble(parts[1]);
                statsMap.computeIfAbsent(name, n -> new Stats(n, value)).add(value);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + filename, e);
        }
        return new ArrayList<>(statsMap.values());
    }
}
