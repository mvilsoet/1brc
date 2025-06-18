package onebrc.core;

import onebrc.api.ReportGenerator;
import onebrc.api.Stats;
import java.util.List;

public class DefaultReportGenerator implements ReportGenerator {
    @Override
    public void generate(List<Stats> stats) {
        // Basic print, probably should override this
        int total_count = 0;
        for (Stats s : stats) {
            total_count = total_count + s.count;
            System.out.printf("%s: min=%.2f max=%.2f avg=%.2f%n", s.name, s.min, s.max, s.avg());
        }
        System.out.printf("Total rows: %d%n", total_count);
    }
}
