package main;

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
        setResult(result);
        System.out.println("Operation performed for " + getId() + ". Result: " + result);
    }
}
