public class Metric {
    private String actionId;
    private long executionTime;
    private Invoker assignedInvoker;
    private double usedMemory;
    
    public Metric(String actionId, long executionTime, Invoker assignedInvoker, double usedMemory) {
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

    public Invoker getAssignedInvoker() {
        return assignedInvoker;
    }

    public void setAssignedInvoker(Invoker assignedInvoker) {
        this.assignedInvoker = assignedInvoker;
    }

    public double getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(double usedMemory) {
        this.usedMemory = usedMemory;
    }

    
}
