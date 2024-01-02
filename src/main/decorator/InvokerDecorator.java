package main.decorator;

import java.util.ArrayList;

import main.Action;
import main.InsufficientMemoryException;
import main.Invoker;
import main.Metric;
import main.Result;
import main.interfaces.Observer;

import java.util.concurrent.ConcurrentHashMap;

public class InvokerDecorator extends Invoker {
    private Invoker invoker;

    public InvokerDecorator(Invoker invoker) {
        super(invoker.getTotalMemory(), invoker.getId());
        this.invoker = invoker;
    }

    @Override
    public String getId() {
        return invoker.getId();
    }

    @Override
    public void setId(String id) {
        invoker.setId(id);
    }

    @Override
    public ArrayList<Action> getActions() {
        return invoker.getActions();
    }

    @Override
    public boolean setAction(Action action) throws InsufficientMemoryException {
        if (!searchHash(action.getValues(), action)) {
            return invoker.setAction(action);
        } else {
            // No se si esto se tiene que hacer o no, pero creo que si
            // action.setResult(invoker.getCache().get(action.getId()).getResult());
            return false;
        }
    }

    @Override
    public double getTotalMemory() {
        return invoker.getTotalMemory();
    }

    @Override
    public void setTotalMemory(double totalMemory) {
        invoker.setTotalMemory(totalMemory);
    }

    @Override
    public void releaseMemory(double memoryReleased) {
        invoker.releaseMemory(memoryReleased);
    }

    @Override
    public ArrayList<Result> executeInvokerActions() throws InsufficientMemoryException {
        ArrayList<Result> results = invoker.executeInvokerActions();
        for (Result result : results) {
            // -------------------------------------------------------------------------
            // cache
            // -------------------------------------------------------------------------

            // invoker.getCache().put(result.getId(), result);

            // -------------------------------------------------------------------------
            // cache
            // -------------------------------------------------------------------------
        }
        return results;
    }

    @Override
    public void addObserver(Observer observer) {
        invoker.addObserver(observer);
    }

    @Override
    public ArrayList<Observer> getObservers() {
        return invoker.getObservers();
    }

    @Override
    public void removeObserver(Observer observer) {
        invoker.removeObserver(observer);
    }

    @Override
    public void notifyObservers(ArrayList<Metric> metrics) {
        invoker.notifyObservers(metrics);
    }

    public ConcurrentHashMap<String, Result> getCache() {
        return invoker.getCache();
    }

    private boolean searchHash(int[] values, Action action) {
        for (Result result : invoker.getCache().values()) {
            if (arrayEqual(result.getInput(), values) && action.getId().equals(result.getId())) {
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
}
