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

public class Invoker implements InterfaceInvoker {

    private ArrayList<Action> actions;
    private double totalMemory; // memory -> MB
    private String id;
    private ArrayList<Observer> observers = new ArrayList<>();
    public ArrayList<Metric> metrics = new ArrayList<>();
    public ConcurrentHashMap<String, ActionResult> cache = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public Invoker(double totalMemory, String id) {
        this.totalMemory = totalMemory;
        this.id = id;
        this.actions = new ArrayList<Action>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public boolean setAction(Action action) throws InsufficientMemoryException {
        if (action.getMemory() <= totalMemory) {
            actions.add(action);
            action.setInvoker(this);
            takeMemory(action.getMemory());
            // we take memory because exactly in this moment we've associate the action to
            // the invoker.
            return true;
        } else {
            throw new InsufficientMemoryException("Not enough memory to take the action");
        }
    }

    public ArrayList<Metric> getMetrics() {
        return metrics;
    }

    public double getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(double totalMemory) {
        this.totalMemory = totalMemory;
    }

    // method that substracts the action memory of the actual memory of invoker. We
    // should invoke this method when we invoke an action to the Invoker
    private void takeMemory(double memoryToTake) {
        totalMemory = totalMemory - memoryToTake;
    }

    // method that gives back the action memory to the invoker. We should invoke
    // this method when the action is finished
    public void releaseMemory(double memoryReleased) {
        totalMemory = totalMemory + memoryReleased;
    }

    // executes all actions at once
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

    // it checks that a observer isn't an observer right now. To delete duplicates
    public void addObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public ArrayList<Observer> getObservers() {
        return observers;
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(ArrayList<Metric> metrics) {
        for (Observer observer : observers) {
            observer.updateMetric(metrics);
        }
    }

    public void notifyObserversAsync(ArrayList<Future<Metric>> metrics) {
        for (Observer observer : observers) {
            observer.updateMetricAsync(metrics);
        }
    }

    public ConcurrentHashMap<String, ActionResult> getCache() {
        return this.cache;
    }

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

    public void waitForFutures(List<Future<ActionResult>> futures) throws InterruptedException, ExecutionException {
        for (Future<ActionResult> future : futures) {
            try {
                future.get(); // Wait the other futures
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdownExecutorService() {
        executorService.shutdown();
    }

    public String toString() {
        return "InvokerID: " + this.id +
                " | Memory: " + this.totalMemory +
                " | Actions: " + this.actions.toString();
    }
}
