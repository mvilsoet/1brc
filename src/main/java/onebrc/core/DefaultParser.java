package onebrc.core;

import onebrc.api.Parser;
import onebrc.api.Stats;

import java.util.concurrent.*;

public class DefaultParser implements Parser {
    @Override
    public Future<Stats> parse(int rowStart, int rowEnd) {
        return CompletableFuture.completedFuture(null);
    }
}
