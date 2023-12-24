import java.util.ArrayList;;

public class Invoker {
    // creo que tendra que tener un array de Action, pero de momento esta bien asi
    private ArrayList<Action> actions;
    private double totalMemory; // memory -> MB
    private ArrayList<Observer> observers;
    // invokers hijos?

    public Invoker(double totalMemory) {
        this.totalMemory = totalMemory;
        this.actions = new ArrayList<Action>();
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setAction(Action action) throws InsufficientMemoryException {
        if (action.getMemory() <= totalMemory) {
            actions.add(action);
            action.setInvoker(this);
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
        long startTime=System.currentTimeMillis();
        for (Action action : actions) {
            System.out.println("Executing action: " + action.getId());
            action.operation();
        long endTime=System.currentTimeMillis();
        Metric metric=new Metric(action.getId(), endTime-startTime, action.getInvoker(), action.getMemory());
        notifyObservers(metric);
        }
        actions.clear();

    }

    public void addObserver(Observer observer) {
        if (observers == null) {
            observers = new ArrayList<>();
        }
        observers.add(observer);
    }

    public void notifyObservers(Metric metric) {
        if (observers != null) {
            for (Observer observer : observers) {
                observer.updateMetric(metric);
            }
        }
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
