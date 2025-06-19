package onebrc.tool;

import onebrc.api.*;
import onebrc.core.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    public static void main(String[] args) {
        String filename = args[0];
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        Reader reader = new DefaultReader(filename, executor);
        List<Stats> stats = reader.read();

        executor.shutdown();

        ReportGenerator reportGenerator = new DefaultReportGenerator();
        reportGenerator.generate(stats);
    }
}
