package onebrc.api;

public class Stats {
    public String name;
    public double min, max, sum;
    public int count;

    public Stats(String name, double value) {
        this.name = name;
        this.min = this.max = this.sum = value;
        this.count = 1;
    }

    public void add(double value) {
        min = Math.min(min, value);
        max = Math.max(max, value);
        sum += value;
        count++;
    }

    public double avg() {
        return sum / count;
    }
}
