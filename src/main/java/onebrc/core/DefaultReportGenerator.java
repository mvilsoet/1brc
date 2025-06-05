package onebrc.core;

import onebrc.api.ReportGenerator;
import onebrc.api.Stats;

import java.util.List;

public class DefaultReportGenerator implements ReportGenerator {
    @Override
    public void generate(List<Stats> stats) {
        // stub: print size
        System.out.println("Report has " + stats.size() + " entries.");
    }
}
