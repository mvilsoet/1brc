package onebrc.api;

import onebrc.tool.Stats;
import java.util.List;

public interface ReportGenerator {
    void generate(List<Stats> stats);
}
