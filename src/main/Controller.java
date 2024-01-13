package main;

import main.decorator.InvokerCacheDecorator;
import main.exceptions.InsufficientMemoryException;
import main.interfaces.DistributionPolicy;
import main.interfaces.InterfaceInvoker;
import main.interfaces.Observer;
import main.operations.Adder;
import main.operations.Factorial;
import main.operations.Multiplier;
import main.policy.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.concurrent.ExecutionException;
import main.decorator.ActionResult;

/**
 * The `Controller` class is a central component responsible for managing the
 * execution of actions
 * using a set of invokers. It acts as an observer to capture metrics and
 * provides methods for
 * analyzing and printing the collected metrics.
 */
public class Controller implements Observer {

    // volatile: to ensure that changes made by one thread are visible to other
    // threads
    private static volatile Controller controller = new Controller(1);

    private int id;
    private ArrayList<Invoker> invokers;
    private ArrayList<Action> actions;
    private DistributionPolicy policy; // Controller acts as Context from Strategy pattern
    private ArrayList<Metric> metrics; // metrics for the Observer

    /**
     * Private constructor used for creating a new Controller instance with
     * specified parameters.
     *
     * @param invokers List of invokers associated with the controller.
     * @param actions  List of actions managed by the controller.
     * @param id       Identifier for the controller.
     */
    private Controller(ArrayList<Invoker> invokers, ArrayList<Action> actions, int id) {
        this.invokers = invokers;
        this.actions = actions;
        this.policy = new RoundRobinImproved(); // default policy set to RoundRobinImproved
        this.metrics = new ArrayList<>();
        this.id = id;
    }

    /**
     * Constructs a new Controller instance with the specified parameters.
     *
     * @param nInvokers   The number of invokers to add to the controller.
     * @param totalMemory The total memory allocated for invokers.
     * @param actions     The list of actions to be managed by the controller.
     * @param id          The unique identifier for the controller.
     */
    private Controller(int nInvokers, float totalMemory, ArrayList<Action> actions, int id) {
        addInvoker(new Invoker(totalMemory, "1"), nInvokers);
        this.actions = actions;
        this.policy = new RoundRobinImproved(); // default policy set to RoundRobinImproved
        this.metrics = new ArrayList<>();
        this.id = id;
    }

    /**
     * Private constructor used for creating a new Controller instance with a
     * specified identifier.
     *
     * @param id Identifier for the controller.
     */
    private Controller(int id) {
        this.invokers = new ArrayList<>();
        this.actions = new ArrayList<>();
        this.policy = new RoundRobin();
        this.metrics = new ArrayList<>();
        this.id = id;
    }

    /**
     * Retrieves the singleton instance of the Controller.
     *
     * @return The singleton instance of the Controller.
     */
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

    /**
     * Resets the singleton instance of the Controller. Used for testing purposes.
     */
    public static void resetInstance() {
        controller = null;
    }

    /**
     * Gets the identifier of the controller.
     *
     * @return The identifier of the controller.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the list of invokers associated with the controller.
     *
     * @return The list of invokers.
     */
    public ArrayList<Invoker> getInvokers() {
        return invokers;
    }

    /**
     * Sets the list of invokers associated with the controller.
     *
     * @param invokers The list of invokers to be set.
     */
    public void setInvokers(ArrayList<Invoker> invokers) {
        this.invokers = invokers;
    }

    /**
     * Sets the distribution policy for the controller.
     *
     * @param policy The distribution policy to be set.
     */
    public void setPolicy(DistributionPolicy policy) {
        this.policy = policy;
    }

    /**
     * Adds an action to the list of actions managed by the controller.
     *
     * @param action The action to be added.
     */
    public void addAction(Action action) {
        actions.add(action);
    }

    /**
     * Gets the list of actions managed by the controller.
     *
     * @return The list of actions.
     */
    public ArrayList<Action> getActions() {
        return actions;
    }

    /**
     * Gets an action by its identifier.
     *
     * @param id The identifier of the action.
     * @return The action with the specified identifier, or null if not found.
     */
    public Action getActionById(String id) {
        for (Action action : actions) {
            if (action.getId().equals(id)) {
                return action;
            }
        }
        return null;
    }

    /**
     * Sets the list of actions managed by the controller.
     *
     * @param actions The list of actions to be set.
     */
    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }

    /**
     * Adds an action to the list multiple times with modified identifiers.
     *
     * @param action The action to be added.
     * @param nTimes The number of times to add the action.
     */
    public void addAction(Action action, int nTimes) {
        for (int i = 0; i < nTimes; i++) {
            if (action instanceof Adder) {
                Adder newAction = new Adder(action.getId() + "_" + i, action.getMemory(),
                        action.getValues());
                actions.add(newAction);
            } else if (action instanceof Multiplier) {
                Multiplier newAction = new Multiplier(action.getId() + "_" + i, action.getMemory(),
                        action.getValues());
                actions.add(newAction);
            } else if (action instanceof Factorial) {
                Factorial newAction = new Factorial(action.getId() + "_" + i, action.getMemory(),
                        action.getValues());
                actions.add(newAction);
            }
        }
    }

    /**
     * Adds an invoker to the list of invokers associated with the controller.
     *
     * @param invoker The invoker to be added.
     */
    public void addInvoker(Invoker invoker) {
        invokers.add(invoker);
        invoker.addObserver(this);
    }

    /**
     * Adds an invoker to the list multiple times with modified identifiers.
     *
     * @param invoker The invoker to be added.
     * @param nTimes  The number of times to add the invoker.
     */
    public void addInvoker(Invoker invoker, int nTimes) {
        for (int i = 0; i < nTimes; i++) {
            Invoker newInvoker;
            if (invoker instanceof InvokerCacheDecorator) {
                newInvoker = new InvokerCacheDecorator(new Invoker(invoker.getTotalMemory(),
                        invoker.getId() + "_" + i));
            } else {
                newInvoker = new Invoker(invoker.getTotalMemory(), invoker.getId() + "_" + i);
            }
            invokers.add(newInvoker);
            newInvoker.addObserver(this);
        }
    }

    /**
     * Gets an invoker by its identifier.
     *
     * @param id The identifier of the invoker.
     * @return The invoker with the specified identifier, or null if not found.
     */
    public Invoker getInvokerById(String id) {
        for (Invoker invoker : invokers) {
            if (invoker.getId().equals(id)) {
                return invoker;
            }
        }
        return null;
    }

    /**
     * Distributes the actions among the invokers based on the configured
     * distribution policy.
     *
     * @return True if the distribution is successful, false otherwise.
     */
    public boolean distributeActions() {
        return policy.distributeActions(actions, invokers);
    }

    /**
     * Executes a specific action by its identifier.
     *
     * @param id The identifier of the action to be executed.
     */
    public void executeActionById(String id) {
        Action foundAction = actions.stream()
                .filter(action -> action.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (foundAction != null && foundAction.getInvoker() != null) {
            InterfaceInvoker invoker = foundAction.getInvoker();
            if (invoker instanceof InvokerCacheDecorator) {
                invoker.executeThisAction(foundAction); // with cache
            } else {
                invoker.executeThisAction(foundAction); // without cache
            }
            foundAction.getInvoker().releaseMemory(foundAction.getMemory());
            foundAction.setInvoker(null);
        }
    }

    /**
     * Executes all assigned actions managed by the controller.
     */
    public void executeAssignedActions() {
        for (Action action : actions) {
            try {
                // if invoker is null, the action is not assigned
                if (action.getInvoker() != null) {
                    InterfaceInvoker invoker = action.getInvoker();
                    if (invoker instanceof InvokerCacheDecorator) {
                        invoker.executeInvokerActions(); // with cache
                    } else {
                        invoker.executeInvokerActions(); // without cache
                    }
                    action.getInvoker().releaseMemory(action.getMemory());
                    action.setInvoker(null);
                }
            } catch (InsufficientMemoryException e) {
                System.out.println("Error executing actions for invoker: " + e.getMessage());
            }
        }
    }

    /**
     * Executes all actions using threads asynchronously.
     */
    public void executeAllInvokersAsync() {
        List<Future<ActionResult>> allFutures = new ArrayList<>();

        for (Invoker invoker : invokers) {
            // saves the results into a list of Futures
            List<Future<ActionResult>> invokerFutures = invoker.executeInvokerActionsAsync();
            allFutures.addAll(invokerFutures);
            // waits for current result before continuing
            try {
                invoker.waitForFutures(allFutures);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // ends the threads
        for (Invoker invoker : invokers) {
            invoker.shutdownExecutorService();
        }
    }

    /**
     * Updates the metrics with the provided list of metrics.
     *
     * @param metrics The list of metrics to be added to the controller's metrics.
     */
    public void updateMetric(ArrayList<Metric> metrics) {
        this.metrics.addAll(metrics);
    }

    /**
     * Updates the metrics asynchronously using threads and futures.
     *
     * @param metrics The list of futures representing asynchronous metric updates.
     */
    @Override
    public void updateMetricAsync(ArrayList<Future<Metric>> metrics) {
        ArrayList<Metric> auxiliary = new ArrayList<>();
        for (Future<Metric> future : metrics) {
            try {
                Metric metric = future.get();
                auxiliary.add(metric);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.metrics.addAll(auxiliary);
    }

    /**
     * Retrieves the collected metrics.
     *
     * @return The list of metrics collected during the execution.
     */
    public ArrayList<Metric> getMetrics() {
        return metrics;
    }

    /**
     * Analyzes the execution time metrics. It calculates the minimum, maximum,
     * average,
     * and total execution time for each action.
     *
     * @param metrics The list of metrics collected during the execution.
     */
    public void analyzeExecutionTime(ArrayList<Metric> metrics) {
        Map<String, List<Metric>> metricsByAction = metrics.stream()
                .collect(Collectors.groupingBy(Metric::getActionId));

        // Calculates the min, max, avg, and total time for one action
        metricsByAction.forEach((actionId, actionMetrics) -> {
            long minExecutionTime = actionMetrics.stream().mapToLong(Metric::getExecutionTime).min().orElse(0);
            long maxExecutionTime = actionMetrics.stream().mapToLong(Metric::getExecutionTime).max().orElse(0);
            double averageExecutionTime = actionMetrics.stream().mapToLong(Metric::getExecutionTime).average()
                    .orElse(0);
            long totalExecutionTime = actionMetrics.stream().mapToLong(Metric::getExecutionTime).sum();

            System.out.println("Action: " + actionId +
                    ", Min Execution Time: " + minExecutionTime +
                    ", Max Execution Time: " + maxExecutionTime +
                    ", Average Execution Time: " + averageExecutionTime +
                    ", Total Execution Time: " + totalExecutionTime);
        });
    }

    /**
     * Analyzes the execution time metrics using an alternative method. It
     * calculates the minimum,
     * maximum, average, and total execution time for each invoker.
     *
     * @param metrics The list of metrics collected during the execution.
     */
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

    /**
     * Analyzes the memory usage of each invoker based on the provided list of
     * metrics.
     * It calculates the total memory usage, percentage used, and prints the results
     * for each invoker.
     *
     * @param metrics The list of metrics containing information about invoker
     *                memory usage.
     */
    public void analyzeInvokerMemory(ArrayList<Metric> metrics) {
        // Create a concurrent map to store the memory usage of each invoker
        Map<String, Double> invokerMemoryUsage = new ConcurrentHashMap<>();

        // Iterate through the metrics to aggregate memory usage for each invoker
        metrics.forEach(metric -> {
            String invokerId = metric.getAssignedInvoker().getId();
            // Add the memory used for every action
            invokerMemoryUsage.merge(invokerId, metric.getUsedMemory(), Double::sum);
        });

        // Iterate through the aggregated memory usage and calculate percentage used for
        // each invoker
        invokerMemoryUsage.forEach((invokerId, memoryUsage) -> {
            double totalMemory = metrics.stream()
                    .filter(metric -> metric.getAssignedInvoker().getId().equals(invokerId))
                    .mapToDouble(metric -> metric.getAssignedInvoker().getTotalMemory())
                    .findFirst().orElse(0.0);

            // Calculate the percentage of memory used
            double percentageUsed = (memoryUsage / totalMemory) * 100;

            // Print the results for each invoker
            System.out.println("Invoker: " + invokerId +
                    ", Memory Usage: " + memoryUsage +
                    " MB, Percentage Used: " + percentageUsed + "%");
        });
    }

    /**
     * Prints the collected metrics, including action ID, execution time, invoker
     * ID, and memory usage.
     * The method retrieves the metrics using the `getMetrics` method.
     */
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