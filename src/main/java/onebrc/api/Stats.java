package onebrc.api;

public class Stats {
    private final double min;
    private final double max;
    private final double avg;

    public Stats(double min, double max, double avg) {
        this.min = min;
        this.max = max;
        this.avg = avg;
    }

    public double getMin() { return min; }
    public double getMax() { return max; }
    public double getAvg() { return avg; }
}
