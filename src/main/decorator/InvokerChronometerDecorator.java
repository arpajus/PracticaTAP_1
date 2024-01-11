package main.decorator;

import java.util.ArrayList;
import java.util.List;

import main.Action;
import main.Controller;
import main.Invoker;
import main.Metric;
import main.exceptions.InsufficientMemoryException;
import main.interfaces.Observer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

//Decorator for Invoker who adds the functionality to print all the execution time of this Invoker and its metrics
public class InvokerChronometerDecorator extends Invoker {
    private Invoker invoker;

    public InvokerChronometerDecorator(Invoker invoker) {
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
        return invoker.setAction(action);
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
    public ArrayList<ActionResult> executeInvokerActions() throws InsufficientMemoryException {
        return invoker.executeInvokerActions();
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

    public ConcurrentHashMap<String, ActionResult> getCache() {
        return invoker.getCache();
    }

    public ArrayList<Metric> getMetrics() {
        return invoker.getMetrics();
    }

    public List<Future<ActionResult>> executeInvokerActionsAsync() {
        return invoker.executeInvokerActionsAsync();
    }

    public void waitForFutures(List<Future<ActionResult>> futures) throws InterruptedException, ExecutionException {
        invoker.waitForFutures(futures);
    }

    public void shutdownExecutorService() {
        invoker.shutdownExecutorService();
    }

    // prints this invoker metrics and its total time
    public String toString() {
        return invoker.toString() +
                " | Metrics: " + invoker.getMetrics().toString() + " || TotalTime: " + chronometer();
    }

    // Adds all the metrics times from this Invoker to get their total time
    public long chronometer() {
        Controller controller = Controller.getInstance();
        long time = 0;
        for (Metric metric : controller.getMetrics()) {
            if (metric.getAssignedInvoker().equals(invoker)) {
                time += metric.getExecutionTime();
            }
        }
        return time;
    }
}
