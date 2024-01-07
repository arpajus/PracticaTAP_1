package main;
import java.math.BigInteger;

import java.util.Arrays;

import main.interfaces.InterfaceAction;
import main.interfaces.InterfaceInvoker;

public abstract class Action implements InterfaceAction{
    // nose si aparte de id tiene que haber un codigo, no tengo claro si id y codigo
    // son diferentes
    private String id;
    private double memory; // memory -> MB
    protected int values[];
    private BigInteger result;
    private InterfaceInvoker invoker;

    public Action(String id, double memory, int[] values) {
        this.id = id;
        this.memory = memory;
        //copy of values or refference?
        this.values = values;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getMemory() {
        return memory;
    }

    public void setMemory(double memory) {
        this.memory = memory;
    }

    public int[] getValues() {
        return values;
    }

    public void setValues(int[] values) {
        this.values = values;
    }

    public BigInteger getResult() {
        return result;
    }

    public void setResult(BigInteger result) {
        this.result = result;
    }

    public void setInvoker(Invoker invoker) {
        this.invoker = invoker;
    }

    public InterfaceInvoker getInvoker() {
        return invoker;
    }

    @Override
    public String toString() {
        return "Action: [id=" + id + ", memory=" + memory + ", values=" + Arrays.toString(values) + "]";
    }

    public abstract void operation();
}