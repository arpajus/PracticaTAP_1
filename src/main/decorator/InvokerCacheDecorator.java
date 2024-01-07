package main.decorator;

import java.util.ArrayList;

import main.Action;
import java.util.Arrays;
import main.Invoker;
import main.Metric;
import main.exceptions.InsufficientMemoryException;
import main.interfaces.Observer;
import java.util.concurrent.ConcurrentHashMap;

public class InvokerCacheDecorator extends Invoker {
    private Invoker invoker;

    public InvokerCacheDecorator(Invoker invoker) {
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
        /*for (Result result : results) {
            // -------------------------------------------------------------------------
            // cache
            // -------------------------------------------------------------------------

             invoker.getCache().put(result.getId(), result);

            // -------------------------------------------------------------------------
            // cache
            // -------------------------------------------------------------------------
        }*/
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
            if (Arrays.equals(result.getInput(), values) && action.getId().equals(result.getId())) {
                action.setResult(result.getResult());
                return true;
            }
        }
        return false;
    }
}
