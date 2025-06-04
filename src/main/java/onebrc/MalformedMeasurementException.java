package onebrc;

/**
 * Thrown when a measurements file contains a line that does not match the expected format.
 */
public class MalformedMeasurementException extends Exception {
    public MalformedMeasurementException(String message) {
        super(message);
    }
}
