package main;

import main.decorator.Result;
import main.exceptions.InsufficientMemoryException;
import main.interfaces.InterfaceInvoker;
import java.util.ArrayList;
import main.interfaces.Observer;
import java.util.concurrent.ConcurrentHashMap;

public class Invoker implements InterfaceInvoker {

    private ArrayList<Action> actions;
    private double totalMemory; // memory -> MB
    private String id;
    private ArrayList<Observer> observers = new ArrayList<>();
    public ArrayList<Metric> metrics = new ArrayList<>();
    public ConcurrentHashMap<String, Result> cache = new ConcurrentHashMap<>();

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
    public ArrayList<Result> executeInvokerActions() throws InsufficientMemoryException {
        ArrayList<Metric> metricsToNotify = new ArrayList<>();
        ArrayList<Result> results = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (Action action : actions) {
            System.out.println("Executing action: " + action.getId());
            action.operation();
            long endTime = System.currentTimeMillis();
            

            //-------------------------------------------------------------------------
            //cache
            //-------------------------------------------------------------------------
            Result r = new Result(action);            
            results.add(r);
            cache.put(r.getId(),r);
            //-------------------------------------------------------------------------
            //cache
            //-------------------------------------------------------------------------

            Metric metric = new Metric(action.getId(), endTime - startTime, action.getInvoker(),
                    action.getMemory());
            metricsToNotify.add(metric);
        }
        notifyObservers(metricsToNotify);
        actions.clear();
        System.out.println("Metrics recorded.");
        return results;
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

    public ConcurrentHashMap<String, Result> getCache() {
        return this.cache;
    }
}
