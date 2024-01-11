package main;

import main.interfaces.InterfaceInvoker;

/**
 * Represents a metric capturing performance and resource usage information for
 * an action execution.
 */
public class Metric {

    /** The unique identifier of the associated action. */
    private String actionId;

    /** The execution time (in milliseconds) of the associated action. */
    private long executionTime;

    /** The invoker to which the action was assigned. */
    private InterfaceInvoker assignedInvoker;

    /** The amount of memory (in megabytes) used by the action. */
    private double usedMemory;

    /**
     * Constructs a Metric with the specified parameters.
     *
     * @param actionId        The unique identifier of the associated action.
     * @param executionTime   The execution time (in milliseconds) of the associated
     *                        action.
     * @param assignedInvoker The invoker to which the action was assigned.
     * @param usedMemory      The amount of memory (in megabytes) used by the
     *                        action.
     */
    public Metric(String actionId, long executionTime, InterfaceInvoker assignedInvoker, double usedMemory) {
        this.actionId = actionId;
        this.executionTime = executionTime;
        this.assignedInvoker = assignedInvoker;
        this.usedMemory = usedMemory;
    }

    /**
     * Gets the unique identifier of the associated action.
     *
     * @return The unique identifier of the associated action.
     */
    public String getActionId() {
        return actionId;
    }

    /**
     * Sets the unique identifier of the associated action.
     *
     * @param actionId The unique identifier of the associated action.
     */
    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    /**
     * Gets the execution time (in milliseconds) of the associated action.
     *
     * @return The execution time (in milliseconds) of the associated action.
     */
    public long getExecutionTime() {
        return executionTime;
    }

    /**
     * Sets the execution time (in milliseconds) of the associated action.
     *
     * @param executionTime The execution time (in milliseconds) of the associated
     *                      action.
     */
    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    /**
     * Gets the invoker to which the action was assigned.
     *
     * @return The invoker to which the action was assigned.
     */
    public InterfaceInvoker getAssignedInvoker() {
        return assignedInvoker;
    }

    /**
     * Sets the invoker to which the action was assigned.
     *
     * @param assignedInvoker The invoker to which the action was assigned.
     */
    public void setAssignedInvoker(InterfaceInvoker assignedInvoker) {
        this.assignedInvoker = assignedInvoker;
    }

    /**
     * Gets the amount of memory (in megabytes) used by the action.
     *
     * @return The amount of memory (in megabytes) used by the action.
     */
    public double getUsedMemory() {
        return usedMemory;
    }

    /**
     * Sets the amount of memory (in megabytes) used by the action.
     *
     * @param usedMemory The amount of memory (in megabytes) used by the action.
     */
    public void setUsedMemory(double usedMemory) {
        this.usedMemory = usedMemory;
    }

    /**
     * Returns a string representation of the metric.
     *
     * @return A string representation of the metric.
     */
    @Override
    public String toString() {
        return "ActionID: " + actionId + " | ExecTime: " + executionTime + " | InvokerID: " + assignedInvoker.getId()
                + " | MemUsed:" + usedMemory;
    }
}
