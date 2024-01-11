package main;

import java.math.BigInteger;

import java.util.Arrays;
import java.util.Map;

import main.interfaces.InterfaceAction;
import main.interfaces.InterfaceInvoker;

/**
 * The abstract base class representing an action to be performed by an invoker.
 * Any concrete class extending Action must implement the operation method.
 */
public abstract class Action implements InterfaceAction {

    /** Unique identifier for the action. */
    private String id;

    /** Memory required for the action in megabytes (MB). */
    private double memory;

    /** Textual input for the action (if applicable). */
    private String text;

    /** Array of values representing the input type for the action. */
    protected int values[];

    /** Result of the action as a BigInteger. */
    private BigInteger result;

    /** Result of the action as a map of strings to integers (if applicable). */
    private Map<String, Integer> resultText;

    /** Invoker associated with the action. */
    private InterfaceInvoker invoker;

    /**
     * Constructs an Action with the specified ID, memory, and input values.
     *
     * @param id     Unique identifier for the action.
     * @param memory Memory required for the action in megabytes (MB).
     * @param values Array of values representing the input type for the action.
     */
    public Action(String id, double memory, int[] values) {
        this.id = id;
        this.memory = memory;
        this.values = values;
    }

    /**
     * Constructs an Action with the specified ID, memory, and textual input.
     *
     * @param id     Unique identifier for the action.
     * @param memory Memory required for the action in megabytes (MB).
     * @param text   Textual input for the action.
     */
    public Action(String id, double memory, String text) {
        this.id = id;
        this.memory = memory;
        this.text = text;
    }

    /**
     * Gets the textual input for the action.
     *
     * @return The textual input for the action.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the textual input for the action.
     *
     * @param text The textual input for the action.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets the unique identifier for the action.
     *
     * @return The unique identifier for the action.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the action.
     *
     * @param id The unique identifier for the action.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the memory required for the action in megabytes (MB).
     *
     * @return The memory required for the action in megabytes (MB).
     */
    public double getMemory() {
        return memory;
    }

    /**
     * Sets the memory required for the action in megabytes (MB).
     *
     * @param memory The memory required for the action in megabytes (MB).
     */
    public void setMemory(double memory) {
        this.memory = memory;
    }

    /**
     * Gets the array of values representing the input type for the action.
     *
     * @return The array of values representing the input type for the action.
     */
    public int[] getValues() {
        return values;
    }

    /**
     * Sets the array of values representing the input type for the action.
     *
     * @param values The array of values representing the input type for the action.
     */
    public void setValues(int[] values) {
        this.values = values;
    }

    /**
     * Gets the result of the action as a BigInteger.
     *
     * @return The result of the action as a BigInteger.
     */
    public BigInteger getResult() {
        return result;
    }

    /**
     * Sets the result of the action as a BigInteger.
     *
     * @param result The result of the action as a BigInteger.
     */
    public void setResult(BigInteger result) {
        this.result = result;
    }

    /**
     * Gets the result of the action as a map of strings to integers (text result).
     *
     * @return The result of the action as a map of strings to integers (text
     *         result).
     */
    public Map<String, Integer> getResultText() {
        return resultText;
    }

    /**
     * Sets the result of the action as a map of strings to integers (text result).
     *
     * @param result The result of the action as a map of strings to integers (text
     *               result).
     */
    public void setResultText(Map<String, Integer> result) {
        this.resultText = result;
    }

    /**
     * Sets the invoker associated with the action.
     *
     * @param invoker The invoker associated with the action.
     */
    public void setInvoker(Invoker invoker) {
        this.invoker = invoker;
    }

    /**
     * Gets the invoker associated with the action.
     *
     * @return The invoker associated with the action.
     */
    public InterfaceInvoker getInvoker() {
        return invoker;
    }

    /**
     * Returns a string representation of the action.
     *
     * @return A string representation of the action.
     */
    @Override
    public String toString() {
        return "Action: [id=" + id + ", memory=" + memory + ", values=" + Arrays.toString(values) + "]";
    }

    /**
     * Abstract method to be implemented by concrete subclasses.
     * Represents the actual operation to be performed by the action.
     */
    public abstract void operation();
}
