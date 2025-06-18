package onebrc.tool;

import org.junit.jupiter.api.Test;

public class AppTest {
    @Test
    public void testPipeline() {
        App.main(new String[] {"src/test/resources/measurements.txt"});
    }
}
