package onebrc.api;

import java.util.concurrent.Future;

public interface Parser {
    Future<Stats> parse(int rowStart, int rowEnd);
}
