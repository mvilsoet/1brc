package onebrc.api;

import java.util.concurrent.Future;
import java.util.List;

public interface Parser {
    Future<List<Stats>> parse(int rowStart, int rowEnd);
}
