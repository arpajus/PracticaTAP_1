package main;

import java.math.BigInteger;

public class Adder extends Action {

    public Adder(String id, double memory, int[] values) {
        super(id, memory, values);
    }

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
