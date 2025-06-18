package onebrc.tool;

import onebrc.api.*;
import onebrc.core.*;

import java.util.List;

public class App {
    public static void main(String[] args) {
        String filename = args[0];
        Reader reader = new DefaultReader(filename);
        List<Stats> stats = reader.read();

        ReportGenerator reportGenerator = new DefaultReportGenerator();
        reportGenerator.generate(stats);
    }
}
