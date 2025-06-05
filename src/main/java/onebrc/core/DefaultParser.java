package onebrc.core;

import onebrc.api.Parser;
import onebrc.api.Stats;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class DefaultParser implements Parser {
    @Override
    public Future<List<Stats>> parse(int rowStart, int rowEnd) {
        // stub: return completed future with empty list
        return CompletableFuture.completedFuture(List.of());
    }
}
