package onebrc.core;

import onebrc.tool.Stats;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AccuracyTest {

    @Test
    void testDefaultParserAccuracy() throws Exception {
        Path measurements = Path.of("src/test/resources/measurements_tiny.txt");

        try (Stream<String> lines = Files.lines(measurements)) {
            DefaultParser parser = new DefaultParser(lines);
            Map<String, Stats> statsMap = parser.call();

            // StationA: values 10.0, 20.0, 3.0 → count=3, sum=33.0, avg=11.0, min=3.0, max=20.0
            Stats a = statsMap.get("StationA");
            assertNotNull(a, "StationA should be present");
            assertEquals(3, a.count,               "StationA count");
            assertEquals(33.0, a.sum, 1e-9,       "StationA sum");
            assertEquals(11.0, a.avg(), 1e-9,      "StationA average");
            assertEquals(3.0, a.min,  1e-9,        "StationA min");
            assertEquals(20.0, a.max, 1e-9,        "StationA max");

            // StationB: values 5.5, 4.5 → count=2, sum=10.0, avg=5.0, min=4.5, max=5.5
            Stats b = statsMap.get("StationB");
            assertNotNull(b, "StationB should be present");
            assertEquals(2, b.count,               "StationB count");
            assertEquals(10.0, b.sum, 1e-9,       "StationB sum");
            assertEquals(5.0, b.avg(), 1e-9,       "StationB average");
            assertEquals(4.5, b.min,  1e-9,        "StationB min");
            assertEquals(5.5, b.max,  1e-9,        "StationB max");

            // StationC: single value 7.25 → count=1, sum=7.25, avg=7.25, min=7.25, max=7.25
            Stats c = statsMap.get("StationC");
            assertNotNull(c, "StationC should be present");
            assertEquals(1, c.count,               "StationC count");
            assertEquals(7.25, c.sum, 1e-9,        "StationC sum");
            assertEquals(7.25, c.avg(), 1e-9,      "StationC average");
            assertEquals(7.25, c.min,  1e-9,       "StationC min");
            assertEquals(7.25, c.max,  1e-9,       "StationC max");

            assertEquals(3, statsMap.size(),       "Expected exactly 3 stations");
        }
    }
}
