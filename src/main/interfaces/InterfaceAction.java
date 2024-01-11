package main.interfaces;

import java.math.BigInteger;

/**
 * Interface representing an action to be performed by an invoker.
 */
public interface InterfaceAction {

    /**
     * Sets the ID associated with the action.
     *
     * @param id The ID to be set.
     */
    void setId(String id);

    /**
     * Gets the amount of memory required for the action.
     *
     * @return The required memory.
     */
    double getMemory();

    /**
     * Sets the amount of memory required for the action.
     *
     * @param memory The required memory to be set.
     */
    void setMemory(double memory);

    /**
     * Gets the input values associated with the action.
     *
     * @return The input values.
     */
    int[] getValues();

    /**
     * Sets the input values associated with the action.
     *
     * @param values The input values to be set.
     */
    void setValues(int[] values);

    /**
     * Gets the result produced by the action.
     *
     * @return The result of the action.
     */
    BigInteger getResult();

    /**
     * Sets the result produced by the action.
     *
     * @param result The result to be set.
     */
    void setResult(BigInteger result);

    /**
     * Gets the invoker associated with the action.
     *
     * @return The invoker.
     */
    InterfaceInvoker getInvoker();

    /**
     * Returns a string representation of the action.
     *
     * @return A string representation of the action.
     */
    String toString();

    /**
     * Performs the operation associated with the action.
     */
    void operation();
}
