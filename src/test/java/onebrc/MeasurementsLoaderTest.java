package onebrc;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class MeasurementsLoaderTest {
    @Test
    public void validFileLoads() throws IOException, MalformedMeasurementException {
        MeasurementsLoader loader = new MeasurementsLoader("src/test/resources/measurements.txt");
        List<String> l = loader.getFirstTen();
        assertEquals(10, l.size());
    }

    @Test
    public void badFileThrows() {
        assertThrows(
                MalformedMeasurementException.class,
                () -> new MeasurementsLoader("src/test/resources/measurements_bad.txt")
        );
    }

}
