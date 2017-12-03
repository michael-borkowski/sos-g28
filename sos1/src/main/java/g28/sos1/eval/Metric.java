package g28.sos1.eval;

import java.util.LinkedList;
import java.util.List;

public class Metric {
    private final long q2, min, max, q1, q3, count;
    private final double mean, sd;

    public Metric(List<Long> values) {
        if (values.isEmpty()) throw new IllegalStateException("no values");

        double _mean = 0, _sd = 0;
        long _min, _max;

        List<Long> sortedValues = new LinkedList<>(values);
        sortedValues.sort(Long::compareTo);

        this.count = sortedValues.size();
        this.q1 = sortedValues.get(sortedValues.size() / 4);
        this.q2 = sortedValues.get(sortedValues.size() * 2 / 4);
        this.q3 = sortedValues.get(sortedValues.size() * 3 / 4);

        _min = _max = sortedValues.get(0);

        for (long value : sortedValues) {
            _mean += ((double) value / sortedValues.size());
            _min = Math.min(_min, value);
            _max = Math.max(_max, value);
        }

        for (long value : sortedValues) {
            _sd += Math.pow((double) value - _mean, 2);
        }

        _sd = Math.sqrt(_sd);

        this.min = _min;
        this.max = _max;
        this.mean = _mean;
        this.sd = _sd;
    }

    public long getMedian() {
        return q2;
    }

    public long getQ1() {
        return q1;
    }

    public long getQ3() {
        return q3;
    }

    public long getMin() {
        return min;
    }

    public long getMax() {
        return max;
    }

    public long getCount() {
        return count;
    }

    public double getMean() {
        return mean;
    }

    public double getStandardDeviation() {
        return sd;
    }

    @Override
    public String toString() {
        return String.format("%5d / %5d / %5d / %5d / %5d avg %6.1f (%6.1f) n = %d", min, q1, q2, q3, max, mean, sd, count);
    }
}
