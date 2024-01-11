package main.operations;

import java.math.BigInteger;

import main.Action;

/**
 * Class representing a factorial operation.
 */
public class Factorial extends Action {

    /**
     * Constructs a Factorial instance with the specified ID, memory requirement, and values.
     *
     * @param id      The ID of the factorial operation.
     * @param memory  The memory requirement for the operation.
     * @param values  The array of values for which the factorial operation is performed.
     */
    public Factorial(String id, double memory, int[] values) {
        super(id, memory, values);
    }

    /**
     * Performs the factorial operation on the first number in the array and sets the result.
     */
    @Override
    public void operation() {
        // Factorial of the first number of the array
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= values[0]; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }

        // Trick to simulate cache access delay
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setResult(result);
        System.out.println("Operation performed for " + getId() + ". Result: " + result);
    }
}
