package main.decorator;

import java.util.ArrayList;

import main.Action;
import main.InsufficientMemoryException;
import main.Invoker;
import main.Metric;
import main.Result;
import main.interfaces.Observer;
import java.util.concurrent.ConcurrentHashMap;

//Decorator for memorization (cache)
public class InvokerCacheDecorator extends Invoker {
    public ConcurrentHashMap<String, Result> cache = new ConcurrentHashMap<>();
    Invoker invoker;

    public InvokerCacheDecorator(Invoker invoker) {
        super(invoker.getTotalMemory(), invoker.getId());
        this.invoker = invoker;
    }

    public String getId() {
        return invoker.getId();
    }

    public void setId(String id) {
        invoker.setId(id);
    }

    public ArrayList<Action> getActions() {
        return invoker.getActions();
    }

    public void setAction(Action action) throws InsufficientMemoryException {
        if (action.getMemory() <= invoker.getTotalMemory()) {
            if (!searchHash(cache, action.getValues(), action)) {
                invoker.getActions().add(action);
                action.setInvoker(this);
                invoker.setTotalMemory(invoker.getTotalMemory() - action.getMemory());
            }
            // we take memory because exactly in this moment we've associate the action to
            // the invoker.
        } else {
            throw new InsufficientMemoryException("Not enough memory to take the action");
        }
    }

    private boolean searchHash(ConcurrentHashMap<String, Result> hashmap, int[] values, Action action) {
        for (Result result : hashmap.values()) {
            if (arrayEqual(result.getInput(), values)) {
                action.setResult(result.getResult());
                return true;
            }
        }
        return false;
    }

    private boolean arrayEqual(int[] input, int[] values) {
        if (input == values) {
            return true;
        }

        if (input == null || values == null) {
            return false;
        }

        if (input.length != values.length) {
            return false;
        }

        for (int i = 0; i < input.length; i++) {
            if (input[i] != values[i]) {
                return false;
            }
        }
        return true;
    }

    public double getTotalMemory() {
        return invoker.getTotalMemory();
    }

    public void setTotalMemory(double totalMemory) {
        invoker.setTotalMemory(totalMemory);
    }

    // method that gives back the action memory to the invoker. We should invoke
    // this method when the action is finished
    public void releaseMemory(double memoryReleased) {
        invoker.releaseMemory(memoryReleased);
    }

    // executes all actions at once
    public void executeInvokerActions() throws InsufficientMemoryException {
        ArrayList<Metric> metricsToNotify = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (Action action : invoker.getActions()) {
            System.out.println("Executing action: " + action.getId());
            action.operation();
            long endTime = System.currentTimeMillis();

            // added to the hashmap
            Result result = new Result(action.getId(), action.getValues(), action.getResult());
            cache.put(result.getId(), result);

            // --------------------------value es operation result, que no se cual sera
            //
            Metric metric = new Metric(action.getId(), endTime - startTime, action.getInvoker(),
                    action.getMemory());
            metricsToNotify.add(metric);
        }
        notifyObservers(metricsToNotify);
        invoker.getActions().clear();
        System.out.println("Metrics recorded.");
    }

    public void addObserver(Observer observer) {
        invoker.addObserver(observer);
    }

    public ArrayList<Observer> getObservers() {
        return invoker.getObservers();
    }

    public void removeObserver(Observer observer) {
        invoker.removeObserver(observer);
    }

    public void notifyObservers(ArrayList<Metric> metrics) {
        invoker.notifyObservers(metrics);
    }

    public ConcurrentHashMap<String, Result> getCache() {
        return this.cache;
    }
}
