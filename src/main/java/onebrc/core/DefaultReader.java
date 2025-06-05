package onebrc.core;

import onebrc.api.Reader;
import onebrc.api.Stats;

import java.util.ArrayList;
import java.util.List;

public class DefaultReader implements Reader {
    @Override
    public List<Stats> read() {
        // stub: return empty list or dummy Stats
        return new ArrayList<>();
    }
}
