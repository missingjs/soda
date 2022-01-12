package soda.unittest.validate;

public class DoubleFeature implements ObjectFeature<Double> {
    @Override
    public long hash(Double obj) {
        return obj.hashCode();
    }

    @Override
    public boolean isEqual(Double a, Double b) {
        return Math.abs(a - b) < 1e-6;
    }
}
