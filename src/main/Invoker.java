package main;

import main.decorator.ActionResult;
import main.exceptions.InsufficientMemoryException;
import main.interfaces.InterfaceInvoker;
import java.util.ArrayList;
import java.util.List;

import main.interfaces.Observer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Represents an invoker that can execute a list of actions. It manages memory
 * usage, execution,
 * and provides support for asynchronous execution using CompletableFuture.
 */
public class Invoker implements InterfaceInvoker {

    /** The list of actions to be executed by the invoker. */
    private ArrayList<Action> actions;

    /** The total available memory (in megabytes) for the invoker. */
    private double totalMemory;

    /** The unique identifier of the invoker. */
    private String id;

    /** The list of observers to notify when metrics are recorded. */
    private ArrayList<Observer> observers = new ArrayList<>();

    /** The list of metrics recorded during invoker actions. */
    public ArrayList<Metric> metrics = new ArrayList<>();

    /** The cache to store results of executed actions. */
    public ConcurrentHashMap<String, ActionResult> cache = new ConcurrentHashMap<>();

    /** The executor service for asynchronous action execution. */
    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    /**
     * Constructs an invoker with the specified total memory and identifier.
     *
     * @param totalMemory The total available memory (in megabytes) for the invoker.
     * @param id          The unique identifier of the invoker.
     */
    public Invoker(double totalMemory, String id) {
        this.totalMemory = totalMemory;
        this.id = id;
        this.actions = new ArrayList<>();
    }

    /**
     * Gets the unique identifier of the invoker.
     *
     * @return The unique identifier of the invoker.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the invoker.
     *
     * @param id The unique identifier of the invoker.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the list of actions to be executed by the invoker.
     *
     * @return The list of actions to be executed by the invoker.
     */
    public ArrayList<Action> getActions() {
        return actions;
    }

    /**
     * Sets the executor service for asynchronous action execution.
     *
     * @param executorService The executor service for asynchronous action
     *                        execution.
     */
    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * Assigns an action to the invoker and checks if there is sufficient memory for
     * the action.
     *
     * @param action The action to be assigned to the invoker.
     * @return True if the action is successfully assigned, otherwise false.
     * @throws InsufficientMemoryException If there is not enough memory to assign
     *                                     the action.
     */
    public boolean setAction(Action action) throws InsufficientMemoryException {
        if (action.getMemory() <= totalMemory) {
            actions.add(action);
            action.setInvoker(this);
            takeMemory(action.getMemory());
            // We take memory because exactly at this moment, we've associated the action
            // with the invoker.
            return true;
        } else {
            throw new InsufficientMemoryException("Not enough memory to take the action");
        }
    }

    /**
     * Gets the list of recorded metrics during invoker actions.
     *
     * @return The list of recorded metrics during invoker actions.
     */
    public ArrayList<Metric> getMetrics() {
        return metrics;
    }

    /**
     * Gets the total available memory (in megabytes) for the invoker.
     *
     * @return The total available memory (in megabytes) for the invoker.
     */
    public double getTotalMemory() {
        return totalMemory;
    }

    /**
     * Sets the total available memory (in megabytes) for the invoker.
     *
     * @param totalMemory The total available memory (in megabytes) for the invoker.
     */
    public void setTotalMemory(double totalMemory) {
        this.totalMemory = totalMemory;
    }

    /**
     * Subtracts the action memory from the actual memory of the invoker.
     *
     * @param memoryToTake The amount of memory (in megabytes) to subtract.
     */
    private void takeMemory(double memoryToTake) {
        totalMemory = totalMemory - memoryToTake;
    }

    /**
     * Gives back the action memory to the invoker. Invoked when the action is
     * finished.
     *
     * @param memoryReleased The amount of memory (in megabytes) to release.
     */
    public void releaseMemory(double memoryReleased) {
        totalMemory = totalMemory + memoryReleased;
    }

    /**
     * Executes all actions of the invoker synchronously.
     *
     * @return The list of ActionResult for each executed action.
     * @throws InsufficientMemoryException If there is not enough memory to execute
     *                                     the actions.
     */
    public ArrayList<ActionResult> executeInvokerActions() throws InsufficientMemoryException {
        ArrayList<Metric> metricsToNotify = new ArrayList<>();
        ArrayList<ActionResult> results = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (Action action : actions) {
            System.out.println("Executing action: " + action.getId());
            action.operation();
            long endTime = System.currentTimeMillis();

            ActionResult r = new ActionResult(action);
            action.setResult(r.getResult());
            results.add(r);
            cache.put(r.getId(), r);

            Metric metric = new Metric(action.getId(), endTime - startTime, action.getInvoker(),
                    action.getMemory());
            metricsToNotify.add(metric);
        }
        metrics.addAll(metricsToNotify);
        notifyObservers(metricsToNotify);
        actions.clear();
        System.out.println("Metrics recorded.");
        return results;
    }

    /**
     * Executes only the specified action of the invoker synchronously.
     *
     * @param action The action to be executed.
     * @return The ActionResult for the executed action.
     */
    public ActionResult executeThisAction(Action action) {
        ArrayList<Metric> metricsToNotify = new ArrayList<>();
        ActionResult result;
        System.out.println("Executing action: " + action.getId());

        long startTime = System.currentTimeMillis();
        action.operation();
        long endTime = System.currentTimeMillis();

        result = new ActionResult(action);
        cache.put(result.getId(), result);

        Metric metric = new Metric(action.getId(), endTime - startTime, action.getInvoker(),
                action.getMemory());
        metricsToNotify.add(metric);
        metrics.addAll(metricsToNotify);
        notifyObservers(metricsToNotify);
        actions.remove(action);
        System.out.println("Metrics recorded.");
        return result;
    }

    /**
     * Adds an observer to the list of observers if it is not already present.
     *
     * @param observer The observer to be added.
     */
    public void addObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Gets the list of observers for the invoker.
     *
     * @return The list of observers for the invoker.
     */
    public ArrayList<Observer> getObservers() {
        return observers;
    }

    /**
     * Removes an observer from the list of observers.
     *
     * @param observer The observer to be removed.
     */
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all observers with the list of metrics.
     *
     * @param metrics The list of metrics to notify observers about.
     */
    public void notifyObservers(ArrayList<Metric> metrics) {
        for (Observer observer : observers) {
            observer.updateMetric(metrics);
        }
    }

    /**
     * Notifies all observers asynchronously with the list of future metrics.
     *
     * @param metrics The list of future metrics to notify observers about.
     */
    public void notifyObserversAsync(ArrayList<Future<Metric>> metrics) {
        for (Observer observer : observers) {
            observer.updateMetricAsync(metrics);
        }
    }

    /**
     * Gets the cache storing results of executed actions.
     *
     * @return The cache storing results of executed actions.
     */
    public ConcurrentHashMap<String, ActionResult> getCache() {
        return this.cache;
    }

    /**
     * Executes all actions of the invoker asynchronously using CompletableFuture.
     *
     * @return The list of Future ActionResult for each executed action.
     */
    public List<Future<ActionResult>> executeInvokerActionsAsync() {
        ArrayList<Future<Metric>> metricsToNotify = new ArrayList<>();
        List<Future<ActionResult>> futures = new ArrayList<>();
        long startTime = System.currentTimeMillis();

        // Use CompletableFuture to execute each action asynchronously
        List<CompletableFuture<ActionResult>> completableFutures = actions.stream()
                .map(action -> CompletableFuture.supplyAsync(() -> {
                    try {
                        CompletableFuture<Metric> future = new CompletableFuture<>();

                        System.out.println("Executing action: " + action.getId() + " on Thread: "
                                + Thread.currentThread().getName());

                        action.operation();

                        long endTime = System.currentTimeMillis();
                        ActionResult r = new ActionResult(action);
                        cache.put(r.getId(), r);
                        Metric metric = new Metric(action.getId(), endTime - startTime, action.getInvoker(),
                                action.getMemory());
                        future.complete(metric);
                        metricsToNotify.add(future);
                        return r;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }, executorService))
                .collect(Collectors.toList());

        // Wait for all CompletableFuture to complete
        CompletableFuture<Void> allOf = CompletableFuture.allOf(
                completableFutures.toArray(new CompletableFuture[0]));

        try {
            allOf.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        notifyObserversAsync(metricsToNotify);
        actions.clear();
        System.out.println("Metrics recorded.");
        return futures;
    }

    /**
     * Waits for all futures to complete.
     *
     * @param futures The list of futures to wait for completion.
     * @throws InterruptedException If the waiting thread is interrupted.
     * @throws ExecutionException   If an error occurs during the execution of the
     *                              futures.
     */
    public void waitForFutures(List<Future<ActionResult>> futures) throws InterruptedException, ExecutionException {
        for (Future<ActionResult> future : futures) {
            try {
                future.get(); // Wait for the other futures
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Shuts down the executor service.
     */
    public void shutdownExecutorService() {
        executorService.shutdown();
    }

    /**
     * Returns a string representation of the invoker.
     */

    public String toString() {
        return "InvokerID: " + this.id +
                " | Memory: " + this.totalMemory +
                " | Actions: " + this.actions.toString();
    }
}
