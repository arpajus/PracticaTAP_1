package main;

import java.math.BigInteger;

import java.util.Arrays;
import java.util.Map;

import main.interfaces.InterfaceAction;
import main.interfaces.InterfaceInvoker;

public abstract class Action implements InterfaceAction {
    private String id;
    private double memory; // memory -> MB
    private String text;
    protected int values[]; // input type
    private BigInteger result;
    private Map<String, Integer> resultText; // input type
    private InterfaceInvoker invoker;

    public Action(String id, double memory, int[] values) {
        this.id = id;
        this.memory = memory;
        this.values = values;
    }

    public Action(String id, double memory, String text) {
        this.id = id;
        this.memory = memory;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public Map<String, Integer> getResultText() {
        return resultText;
    }

    public void setResultText(Map<String, Integer> result) {
        this.resultText = result;
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