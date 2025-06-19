package onebrc.api;

import java.util.Map;
import java.util.concurrent.Callable;

public interface Parser extends Callable<Map<String, Stats>> {
    // call() is inherited
}