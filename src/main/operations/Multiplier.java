package main.operations;

import java.math.BigInteger;

import main.Action;

/**
 * Class representing a multiplication operation.
 */
public class Multiplier extends Action {

    /**
     * Constructs a Multiplier instance with the specified ID, memory requirement, and values.
     *
     * @param id      The ID of the multiplication operation.
     * @param memory  The memory requirement for the operation.
     * @param values  The array of values to be multiplied.
     */
    public Multiplier(String id, double memory, int[] values) {
        super(id, memory, values);
    }

    /**
     * Performs the multiplication operation on the array of values and sets the result.
     */
    @Override
    public void operation() {
        int result = 1;
        for (int i = 0; i < values.length; i++) {
            result = result * values[i];
        }
        setResult(BigInteger.valueOf(result));
        System.out.println("Operation performed for " + getId() + ". Result: " + result);
    }
}
