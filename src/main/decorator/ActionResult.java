package main.decorator;

import main.Action;
import java.math.BigInteger;

/**
 * Represents the result of an action performed by an {@link Action}.
 * This class encapsulates information such as the action's ID, input values,
 * result, and the type of action performed.
 */
public class ActionResult {
    private String id;
    private int[] input;
    private BigInteger result;
    private Class<? extends Action> actionType; // reflection

    /**
     * Constructs an {@code ActionResult} based on the provided {@link Action}.
     *
     * @param action The action for which the result is being represented.
     */
    public ActionResult(Action action) {
        this.id = action.getId();
        this.input = action.getValues();
        this.result = action.getResult();
        this.actionType = action.getClass();
    }

    /**
     * Gets the type of action performed.
     *
     * @return The class representing the type of action.
     */
    public Class<? extends Action> getActionType() {
        return actionType;
    }

    /**
     * Gets the ID associated with the action result.
     *
     * @return The ID of the action result.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID associated with the action result.
     *
     * @param id The new ID to be set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the input values used in the action.
     *
     * @return An array representing the input values.
     */
    public int[] getInput() {
        return input;
    }

    /**
     * Sets the input values used in the action.
     *
     * @param input An array representing the new input values.
     */
    public void setInput(int[] input) {
        this.input = input;
    }

    /**
     * Gets the result produced by the action.
     *
     * @return The result of the action.
     */
    public BigInteger getResult() {
        return result;
    }

    /**
     * Sets the result produced by the action.
     *
     * @param result The new result to be set.
     */
    public void setResult(BigInteger result) {
        this.result = result;
    }
}
