package org.group8.distributions;

/** A continuous generator provides a double value according to the distribution it relies on.
 */
public interface ContinuousGenerator extends Seedable, SampleGenerator {
    double sample();

    @Override
    default double sampleAsDouble() {
        return sample();  // Simply return the result of the `sample()` method.
    }
}
