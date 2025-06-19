package onebrc.tool;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AppTest {
    @Test
    public void testPipeline() {
        assertDoesNotThrow(() -> App.main(new String[] {"src/test/resources/measurements10M.txt"}));
    }
}
