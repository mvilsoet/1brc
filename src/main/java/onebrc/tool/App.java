package onebrc.tool;

import onebrc.api.*;
import onebrc.core.*;
import onebrc.telemetry.JfrTelemetry;
import onebrc.telemetry.NoOpTelemetry;
import onebrc.telemetry.Telemetry;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    public static void main(String[] args) {
        String filename = args[0];
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        boolean useJfr = Boolean.parseBoolean(System.getProperty("onebrc.jfr", "true"));
        Telemetry telemetry = useJfr ? new JfrTelemetry() : new NoOpTelemetry();
        Reader reader = new DefaultReader(filename, executor, telemetry);

        List<Stats> stats = reader.read();

        executor.shutdown();
        ReportGenerator reportGenerator = new DefaultReportGenerator();
        reportGenerator.generate(stats);
    }
}
