package org.group8.distributions;

/** A continuous generator provides a long value according to the distribution it relies on.
 */
public interface DiscreteGenerator extends Seedable, SampleGenerator {
    long sample();

    @Override
    default double sampleAsDouble() {
        return (double) sample();  // Convert the long result to double.
    }
}
