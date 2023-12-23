public class Invoker {
    // creo que tendra que tener un array de Action, pero de momento esta bien asi
    private Action action;
    private double totalMemory; // memory -> MB
    //invokers hijos?

    public Invoker(double totalMemory) {
        this.totalMemory = totalMemory;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) throws InsufficientMemoryException {
            if (action.getMemory() <= totalMemory) {
                this.action = action;
                takeMemory(action.getMemory());
                // we take memory because exactly in this moment we've associate the action to
                // the invoker.
            } else {
                throw new InsufficientMemoryException("Not enough memory to take the action");
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

    public void executeAction() throws InsufficientMemoryException {
        if(action!=null) action.operation();
        else System.out.println("No action set for this invoker");
    }

    // Merece la pena añadir interfaces para todas estas clases? Pensar y decidir

    // FALTAN DECORATORS, para funcionalidades adicionales como caching and timing.
    // SON CLASES DIFERENTES?? Mirar la teoria a ver si pone algo al respecto

    // Añadir jerarquia de delegacion de invokers a invokers en 3 niveles
    // (COMPOSITE)

    /*
     * El Controller puede sobrecargarse si debe gestionar muchos Invokers. Para
     * simplificar el
     * proceso, cada Invoker podrá gestionar internamente diferentes niveles
     * Invokers. Un Invoker
     * podrá pasar una petición de invocación a los Invokers de niveles más bajos.
     * Implementad el
     * sistema para soportar varias jerarquías (3 niveles) de Invokers que operen
     * con normalidad y
     * de manera transparente al Controller.
     * Usad el patrón composite para implementar la jerarquía de Invokers.
     */
}
