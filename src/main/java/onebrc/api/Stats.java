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

    public void merge(Stats other) {
        if (!this.name.equals(other.name)) {
            throw new IllegalArgumentException("Cannot merge Stats of different stations");
        }
        this.min = Math.min(this.min, other.min);
        this.max = Math.max(this.max, other.max);
        this.sum += other.sum;
        this.count += other.count;
    }

}
