package main;

import java.math.BigInteger;

public class Factorial extends Action {

    int result = 0; // BigInteger??

    public Factorial(String id, double memory, int[] values) {
        super(id, memory, values);
    }

    @Override
    public void operation() {
        // Factorial of the first number of the array
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= values[0]; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }

        
        // trick to check cache access
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setResult(result);
        System.out.println("Operation performed for " + getId() + ". Result: " + result);
    }
}
