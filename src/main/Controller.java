package main;

import main.interfaces.DistributionPolicy;
import main.interfaces.Observer;
import main.policy.*;
import java.util.ArrayList;

public class Controller implements Observer {

    // volatile: to ensure that changes made by one thread are visible to other
    // threads
    private static volatile Controller controller = new Controller(1);

    private int id;
    private ArrayList<Invoker> invokers;
    private ArrayList<Action> actions;
    // Controller acts as Context from Strategy pattern
    private DistributionPolicy policy;
    private ArrayList<Metric> metrics; // metrics for the Obsverver

    // Singleton: ensures that only one instance of a class exists in the entire
    // application
    public static Controller getInstance() {
        if (controller == null) {
            synchronized (Controller.class) {
                // Double check within sync
                if (controller == null) {
                    controller = new Controller(1);
                }
            }
        }
        return controller;
    }

    // used for tests
    public static void resetInstance() {
        controller = null;
    }

    // investigar concurrentHashMap si es necesario para optimizar costes (y el
    // concurrent se puede usar con multithreading)

    private Controller(ArrayList<Invoker> invokers, ArrayList<Action> actions, int id) {
        this.invokers = invokers;
        this.actions = actions;
        this.policy = new RoundRobin();
        this.metrics = new ArrayList<>();
        this.id = id;
    }

    // creo que es mejor arraylist, porque los invokers son fijos (si hay 3 hay
    // siempre 3, o 30)
    // por lo que el acceso sera O(1), en vez de O(n) con linked list

    private Controller(int id) {
        this.invokers = new ArrayList<Invoker>();
        this.actions = new ArrayList<Action>();
        this.policy = new RoundRobin();
        this.metrics = new ArrayList<>();
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public Action getActionById(String id) {
        for (Action action : actions) {
            if (action.getId().equals(id)) {
                return action;
            }
        }
        return null;
    }

    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }

    public ArrayList<Invoker> getInvokers() {
        return invokers;
    }

    public void setInvokers(ArrayList<Invoker> invokers) {
        this.invokers = invokers;
    }

    public void setPolicy(DistributionPolicy policy) {
        this.policy = policy;
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public void addAction(Action action, int nTimes) {
        // adds the same action N times, it laso adds i to de ID
        // action.id = hi, n = 4 -> hi0, hi1, hi2, hi3
        for (int i = 0; i < nTimes; i++) {
            Action newAction = new ConcreteAction(action.getId() + i, action.getMemory(), action.getValues());
            actions.add(newAction);
        }
    }

    public void addInvoker(Invoker invoker) {
        invokers.add(invoker);
        invoker.addObserver(this);
    }

    public void addInvoker(Invoker invoker, int nTimes) {
        // adds the invoker action N times, it laso adds i to de ID
        // invoker.id = hi, n = 4 -> hi0, hi1, hi2, hi3
        for (int i = 0; i < nTimes; i++) {
            Invoker newInvoker = new Invoker(invoker.getTotalMemory(), invoker.getId() + "_" + i);
            invokers.add(newInvoker);
            newInvoker.addObserver(this);
        }
    }

    public Invoker getInvokerById(String id) {
        for (Invoker invoker : invokers) {
            if (invoker.getId().equals(id)) {
                return invoker;
            }
        }
        return null;
    }

    public boolean distributeActions() {
        return policy.distributeActions(actions, invokers);
    }

    public void executeAssignedActions() {
        for (Action action : actions) {
            try {
                Invoker invoker = action.getInvoker();
                // if invoker is null, the action is not assigned
                if (invoker != null) {
                    invoker.executeInvokerActions();
                    invoker.releaseMemory(action.getMemory());
                    action.setInvoker(null);
                }
                // we give memory because exactly in this moment we've
                // finished the action related to the concret invoker.
            } catch (InsufficientMemoryException e) {
                System.out.println("Error executing actions for invoker: " + e.getMessage());
            }
        }
    }

    public void updateMetric(ArrayList<Metric> metrics) {
        this.metrics.addAll(metrics);
    }

    public ArrayList<Metric> getMetrics() {
        return metrics;
    }

    public long getMaxExecutionTimeForAction(String actionId) {
        return metrics.stream() // it converts the list to a stream
                .filter(metric -> metric.getActionId().equals(actionId)) // it filters de metric to the action we want
                .mapToLong(Metric::getExecutionTime) // we get the execution time
                .max() // we get the max of the all Execution times
                .orElse(0);
    }

    public void printExecutionTimeForAction(String id) {
        System.out.println(getMaxExecutionTimeForAction(id));
    }

    public void printMetrics() {
        System.out.println("Metrics:");
        ArrayList<Metric> metrics = getMetrics();
        for (Metric metric : metrics) {
            System.out.println("Action: " + metric.getActionId() +
                    ", Time: " + metric.getExecutionTime() +
                    ", Invoker: " + metric.getAssignedInvoker().getId() +
                    ", Memory Used: " + metric.getUsedMemory());
        }
    }

}

// Lo comento porque no se para que sirve, y al cambiar el tipo de estructura
// hay que reprogramarlo
/*
 * //Esto que utilidad tiene? Osea creas un arraylist results donde añades todas
 * las actions
 * //Pero no se usa para nada, osea imprimes directamente de la lista actions,
 * se usara mas adelante?
 * 
 * }
 */

// decidir a que invoker asignar la accion (consultando sus recursos),
// luego si internamente un invoker esta muy "a tope" puede relegar en
// invokers de menor jerarquia para ayudarle (sin que el controller lo sepa)
// mirar la teoria para hacer esto
