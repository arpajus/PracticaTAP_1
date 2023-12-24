package main;

import main.interfaces.DistributionPolicy;
import main.interfaces.Observer;
import main.policy.*;
import java.util.ArrayList;

//El controler es UNICO (hacerlo unico de alguna manera) a nivel de clase, (solo hay un controller)
public class Controller implements Observer {
    // mirar teoria de ED y pensar que estrucutras son mejores para lo que queremos
    // hacer
    private ArrayList<Invoker> invokers;
    private ArrayList<Action> actions;
    private DistributionPolicy policy;
    private ArrayList<Metric> metrics;

    // investigar concurrentHashMap si es necesario para optimizar costes (y el
    // concurrent se puede usar con multithreading)

    public Controller(ArrayList<Invoker> invokers, ArrayList<Action> actions) {
        this.invokers = invokers;
        this.actions = actions;
        this.policy = new RoundRobin();
        this.metrics = new ArrayList<>();
    }

    // creo que es mejor arraylist, porque los invokers son fijos (si hay 3 hay
    // siempre 3, o 30)
    // por lo que el acceso sera O(1), en vez de O(n) con linked list

    public Controller() {
        this.invokers = new ArrayList<Invoker>();
        this.actions = new ArrayList<Action>();
        this.policy = new RoundRobin();
        this.metrics = new ArrayList<>();
    }

    public ArrayList<Action> getActions() {
        return actions;
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

    public void addInvoker(Invoker invoker) {
        invokers.add(invoker);
    }

    public boolean distributeActions() {
        return policy.distributeActions(actions, invokers);
    }

    public void executeAssignedActions() {
        for (Action action : actions) {
            try {
                Invoker invoker = action.getInvoker();
                invoker.executeInvokerActions();
                invoker.releaseMemory(action.getMemory());
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
