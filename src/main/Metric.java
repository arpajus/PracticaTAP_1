package main;

import main.interfaces.InterfaceInvoker;

public class Metric {
    private String actionId;
    private long executionTime;
    private InterfaceInvoker assignedInvoker;
    private double usedMemory;
    
    public Metric(String actionId, long executionTime, InterfaceInvoker assignedInvoker, double usedMemory) {
        this.actionId = actionId;
        this.executionTime = executionTime;
        this.assignedInvoker = assignedInvoker;
        this.usedMemory = usedMemory;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public InterfaceInvoker getAssignedInvoker() {
        return assignedInvoker;
    }

    public void setAssignedInvoker(InterfaceInvoker assignedInvoker) {
        this.assignedInvoker = assignedInvoker;
    }

    public double getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(double usedMemory) {
        this.usedMemory = usedMemory;
    }
}
