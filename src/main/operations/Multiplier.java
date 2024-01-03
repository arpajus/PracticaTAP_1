package main.operations;

import java.math.BigInteger;

import main.Action;

public class Multiplier extends Action {

    public Multiplier(String id, double memory, int[] values) {
        super(id, memory, values);
    }

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
