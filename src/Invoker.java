public class Invoker {
    // creo que tendra que tener un array de Action, pero de momento esta bien asi
    private Action action;
    private double totalMemory; // memory -> MB

    public Invoker(double totalMemory) {
        this.totalMemory = totalMemory;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        try {
            if (action.getMemory() <= totalMemory) {
                this.action = action;
                takeMemory(action.getMemory()); 
                // we take memory because exactly in this moment we've associate the action to the invoker.
            } else {
                throw new InsufficientMemoryException("Not enough memory to take the action");
            }
        } catch (InsufficientMemoryException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public double getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(double totalMemory) {
        this.totalMemory = totalMemory;
    }

    // method that substracts the action memory of the actual memory of invoker. We
    // should invoke this method when we invoke an action to the Invoker
    public double takeMemory(double memoryToTake) {
        totalMemory = totalMemory - memoryToTake;
        return totalMemory;
    }

    // method that gives back the action memory to the invoker. We should invoke
    // this method when the action is finished
    public double releaseMemory(double memoryReleased) {
        totalMemory = totalMemory + memoryReleased;
        return totalMemory;
    }

    public void executeAction() {
        action.operation();
    }

}
