package main;

import main.interfaces.DistributionPolicy;
import main.interfaces.Observer;
import main.policy.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
                // if invoker is null, the action is not assigned
                if (action.getInvoker() != null) {
                    action.getInvoker().executeInvokerActions();
                    action.getInvoker().releaseMemory(action.getMemory());
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

    public void analyzeExecutionTime(ArrayList<Metric> metrics) {
        Map<String, List<Metric>> metricsByAction = metrics.stream()
                .collect(Collectors.groupingBy(Metric::getActionId));

        // Calculates the min, max, avg and total time for one action
        metricsByAction.forEach((actionId, actionMetrics) -> {
            long minExecutionTime = actionMetrics.stream().mapToLong(Metric::getExecutionTime).min().orElse(0);
            long maxExecutionTime = actionMetrics.stream().mapToLong(Metric::getExecutionTime).max().orElse(0);
            double averageExecutionTime = actionMetrics.stream().mapToLong(Metric::getExecutionTime).average().orElse(0);
            long totalExecutionTime = actionMetrics.stream().mapToLong(Metric::getExecutionTime).sum();

            System.out.println("Action: " + actionId +
                    ", Min Execution Time: " + minExecutionTime +
                    ", Max Execution Time: " + maxExecutionTime +
                    ", Average Execution Time: " + averageExecutionTime +
                    ", Total Execution Time: " + totalExecutionTime);
        });
    }

        public void analyzeExecutionTimeBis(ArrayList<Metric> metrics) {
        Map<String, Long> minExecutionTimes = new HashMap<>();
        Map<String, Long> maxExecutionTimes = new HashMap<>();
        Map<String, Long> totalExecutionTimes = new HashMap<>();
        Map<String, Integer> actionCounts = new HashMap<>();

        metrics.forEach(metric -> {
            String invokerId = metric.getAssignedInvoker().getId();
            long executionTime = metric.getExecutionTime();

            minExecutionTimes.merge(invokerId, executionTime, Long::min);
            maxExecutionTimes.merge(invokerId, executionTime, Long::max);
            totalExecutionTimes.merge(invokerId, executionTime, Long::sum);
            actionCounts.merge(invokerId, 1, Integer::sum);
        });

        minExecutionTimes.forEach((invokerId, minExecutionTime) -> {
            long maxExecutionTime = maxExecutionTimes.get(invokerId);
            long totalExecutionTime = totalExecutionTimes.get(invokerId);
            int actionCount = actionCounts.get(invokerId);

            double averageExecutionTime = actionCount > 0 ? (double) totalExecutionTime / actionCount : 0.0;

            System.out.println("Invoker: " + invokerId +
                    ", Min Execution Time: " + minExecutionTime +
                    ", Max Execution Time: " + maxExecutionTime +
                    ", Average Execution Time: " + averageExecutionTime +
                    ", Total Execution Time: " + totalExecutionTime);
        });
    }

    public void analyzeInvokerMemory(ArrayList<Metric> metrics) {
        Map<String, Double> invokerMemoryUsage = new ConcurrentHashMap<>();

        metrics.forEach(metric -> {
            String invokerId = metric.getAssignedInvoker().getId();
            // Add the memory used for every action
            invokerMemoryUsage.merge(invokerId, metric.getUsedMemory(), Double::sum);
        });

        invokerMemoryUsage.forEach((invokerId, memoryUsage) -> {
            double totalMemory = metrics.stream()
                    .filter(metric -> metric.getAssignedInvoker().getId().equals(invokerId))
                    .mapToDouble(metric -> metric.getAssignedInvoker().getTotalMemory())
                    .findFirst().orElse(0.0);

            double percentageUsed = (memoryUsage / totalMemory) * 100;
            
            System.out.println("Invoker: " + invokerId +
                    ", Memory Usage: " + memoryUsage +
                    " MB, Percentage Used: " + percentageUsed + "%");
        });
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
