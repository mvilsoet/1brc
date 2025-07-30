package onebrc.tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class CreateMeasurements {

    private static final Path OUTPUT_DIR = Path.of("src/test/resources");
    private static final Path MEASUREMENT_FILE = OUTPUT_DIR.resolve("measurements.txt");
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newWorkStealingPool();
    private static final String STATIONS_RESOURCE = "stations.csv";

    private record WeatherStation(String id, double meanTemperature) {
        double measurement() {
            double m = ThreadLocalRandom.current().nextGaussian(meanTemperature, 10);
            return Math.round(m * 10.0) / 10.0;
        }
    }

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        if (args.length != 1) {
            System.out.println("Usage: create_measurements <number of records>");
            System.exit(1);
        }

        int size;
        try {
            size = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid value for <number of records>");
            System.exit(1);
            return;
        }

        List<WeatherStation> stations = loadStations(STATIONS_RESOURCE);
        if (stations.isEmpty()) {
            System.err.println("No stations loaded from resource: " + STATIONS_RESOURCE);
            EXECUTOR_SERVICE.shutdownNow();
            System.exit(1);
        }

        Files.createDirectories(OUTPUT_DIR);

        // Clean up old files
        try {
            Files.deleteIfExists(MEASUREMENT_FILE);
            int numParts = Math.max(1, (int) Math.ceil(size / 10_000_000.0));
            for (int i = 0; i < numParts; i++) {
                Files.deleteIfExists(OUTPUT_DIR.resolve("measurements-part-" + i + ".txt"));
            }
        } catch (IOException ignored) {
        }

        int chunkSize = (size / 10_000_000) == 0 ? size : 10_000_000;
        int numberOfFutures = Math.max(1, size / chunkSize);
        CompletableFuture<?>[] futures = new CompletableFuture[numberOfFutures];

        // Generate part files in parallel, with interruption check
        for (int n = 0; n < numberOfFutures; n++) {
            final int partIndex = n;
            futures[n] = CompletableFuture.runAsync(() -> {
                Path partFile = OUTPUT_DIR.resolve("measurements-part-" + partIndex + ".txt");
                try (BufferedWriter bw = Files.newBufferedWriter(partFile,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING)) {

                    StringBuilder builder = new StringBuilder();
                    int batchSize = 10_000;

                    int startIndex = partIndex * chunkSize;
                    int endIndex = Math.min((partIndex + 1) * chunkSize - 1, size - 1);

                    for (int i = startIndex; i <= endIndex; i++) {
                        if (Thread.currentThread().isInterrupted()) {
                            System.err.printf("Part %d interrupted at record %d%n", partIndex, i);
                            return;
                        }
                        WeatherStation ws = stations.get(
                                ThreadLocalRandom.current().nextInt(stations.size())
                        );
                        builder.append(ws.id())
                                .append(';')
                                .append(ws.measurement())
                                .append('\n');

                        if ((i - startIndex + 1) % batchSize == 0) {
                            bw.write(builder.toString());
                            builder.setLength(0);
                        }
                    }
                    if (builder.length() > 0) {
                        bw.write(builder.toString());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, EXECUTOR_SERVICE);
        }

        // Wait for generation
        for (CompletableFuture<?> future : futures) {
            try {
                future.join();
            } catch (Exception e) {
                // ignore; interruptions handled above
            }
        }

        // Concatenate into single file
        try (BufferedWriter out = Files.newBufferedWriter(MEASUREMENT_FILE,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            for (int i = 0; i < futures.length; i++) {
                Path part = OUTPUT_DIR.resolve("measurements-part-" + i + ".txt");
                Files.lines(part).forEach(line -> {
                    try {
                        out.write(line);
                        out.newLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }

        // Cleanup part files
        for (int i = 0; i < futures.length; i++) {
            Files.deleteIfExists(OUTPUT_DIR.resolve("measurements-part-" + i + ".txt"));
        }

        // Shutdown executor to prevent lingering threads
        EXECUTOR_SERVICE.shutdownNow();

        System.out.printf("Created file %s with %,d measurements in %d ms%n", MEASUREMENT_FILE, size,
                System.currentTimeMillis() - start);
    }

    private static List<WeatherStation> loadStations(String resourceName) {
        try (InputStream is = CreateMeasurements.class.getClassLoader()
                .getResourceAsStream(resourceName)) {
            if (is == null) {
                throw new IllegalArgumentException("Resource not found: " + resourceName);
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                return br.lines()
                        .filter(line -> !line.isBlank())
                        .map(line -> {
                            String[] parts = line.split(",");
                            String id = parts[0].trim();
                            double mean = Double.parseDouble(parts[1].trim());
                            return new WeatherStation(id, mean);
                        })
                        .collect(Collectors.toList());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load stations", e);
        }
    }
}