package main.operations;

import java.math.BigInteger;

import main.Action;

/**
 * Class representing an addition operation.
 */
public class Adder extends Action {

    /**
     * Constructs an Adder instance with the specified ID, memory requirement, and values.
     *
     * @param id      The ID of the addition operation.
     * @param memory  The memory requirement for the operation.
     * @param values  The array of values to be added.
     */
    public Adder(String id, double memory, int[] values) {
        super(id, memory, values);
    }

    /**
     * Performs the addition operation on the array of values and sets the result.
     */
    @Override
    public void operation() {
        int result = 0;
        for (int i = 0; i < values.length; i++) {
            result = result + values[i];
        }
        setResult(BigInteger.valueOf(result));
        System.out.println("Operation performed for " + getId() + ". Result: " + result);
    }
}
