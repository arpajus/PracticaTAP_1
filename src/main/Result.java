package main;

import java.math.BigInteger;

public class Result {
    private String id;
    private int[] input;
    private BigInteger result;

    public Result(String id, int[] input, BigInteger result) {
        this.id = id;
        this.input = input;
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int[] getInput() {
        return input;
    }

    public void setInput(int[] input) {
        this.input = input;
    }

    public BigInteger getResult() {
        return result;
    }

    public void setResult(BigInteger result) {
        this.result = result;
    }
}