package main.decorator;

import main.Action;
import java.math.BigInteger;

public class ActionResult {
    private String id;
    private int[] input;
    private BigInteger result;
    private Class<? extends Action> actionType; // reflection

    public ActionResult(Action action) {
        this.id = action.getId();
        this.input = action.getValues();
        this.result = action.getResult();
        this.actionType = action.getClass();
    }

    public Class<? extends Action> getActionType() {
        return actionType;
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