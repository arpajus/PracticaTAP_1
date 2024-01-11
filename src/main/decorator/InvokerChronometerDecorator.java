package main.decorator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import main.Action;
import main.Controller;
import main.Invoker;
import main.Metric;
import main.exceptions.InsufficientMemoryException;
import main.interfaces.Observer;

/**
 * Decorator for {@link Invoker} that adds functionality to print all the execution time
 * of this Invoker and its metrics.
 */
public class InvokerChronometerDecorator extends Invoker {
    private Invoker invoker;

    /**
     * Constructs an {@code InvokerChronometerDecorator} with the specified {@link Invoker}.
     *
     * @param invoker The original invoker to be decorated.
     */
    public InvokerChronometerDecorator(Invoker invoker) {
        super(invoker.getTotalMemory(), invoker.getId());
        this.invoker = invoker;
    }

    /**
     * Gets the ID of the invoker.
     *
     * @return The ID of the invoker.
     */
    @Override
    public String getId() {
        return invoker.getId();
    }

    /**
     * Sets the ID of the invoker.
     *
     * @param id The new ID to be set.
     */
    @Override
    public void setId(String id) {
        invoker.setId(id);
    }

    /**
     * Gets the list of actions associated with the invoker.
     *
     * @return The list of actions.
     */
    @Override
    public ArrayList<Action> getActions() {
        return invoker.getActions();
    }

    /**
     * Sets an action for execution by the invoker.
     *
     * @param action The action to be set.
     * @return {@code true} if the action is set successfully, {@code false} otherwise.
     * @throws InsufficientMemoryException If there is insufficient memory to add the action.
     */
    @Override
    public boolean setAction(Action action) throws InsufficientMemoryException {
        return invoker.setAction(action);
    }

    /**
     * Gets the total memory available to the invoker.
     *
     * @return The total memory.
     */
    @Override
    public double getTotalMemory() {
        return invoker.getTotalMemory();
    }

    /**
     * Sets the total memory available to the invoker.
     *
     * @param totalMemory The new total memory to be set.
     */
    @Override
    public void setTotalMemory(double totalMemory) {
        invoker.setTotalMemory(totalMemory);
    }

    /**
     * Releases a specified amount of memory.
     *
     * @param memoryReleased The amount of memory to be released.
     */
    @Override
    public void releaseMemory(double memoryReleased) {
        invoker.releaseMemory(memoryReleased);
    }

    /**
     * Executes the actions associated with the invoker and returns the results.
     *
     * @return The list of action results.
     * @throws InsufficientMemoryException If there is insufficient memory to execute the actions.
     */
    @Override
    public ArrayList<ActionResult> executeInvokerActions() throws InsufficientMemoryException {
        return invoker.executeInvokerActions();
    }

    /**
     * Adds an observer to the list of observers.
     *
     * @param observer The observer to be added.
     */
    @Override
    public void addObserver(Observer observer) {
        invoker.addObserver(observer);
    }

    /**
     * Gets the list of observers associated with the invoker.
     *
     * @return The list of observers.
     */
    @Override
    public ArrayList<Observer> getObservers() {
        return invoker.getObservers();
    }

    /**
     * Removes an observer from the list of observers.
     *
     * @param observer The observer to be removed.
     */
    @Override
    public void removeObserver(Observer observer) {
        invoker.removeObserver(observer);
    }

    /**
     * Notifies the observers with the specified metrics.
     *
     * @param metrics The metrics to be sent to the observers.
     */
    @Override
    public void notifyObservers(ArrayList<Metric> metrics) {
        invoker.notifyObservers(metrics);
    }

    /**
     * Gets the cache associated with the invoker.
     *
     * @return The cache.
     */
    public ConcurrentHashMap<String, ActionResult> getCache() {
        return invoker.getCache();
    }

    /**
     * Gets the list of metrics associated with the invoker.
     *
     * @return The list of metrics.
     */
    public ArrayList<Metric> getMetrics() {
        return invoker.getMetrics();
    }

    /**
     * Executes the actions associated with the invoker asynchronously.
     *
     * @return The list of futures representing the asynchronous execution of actions.
     */
    public List<Future<ActionResult>> executeInvokerActionsAsync() {
        return invoker.executeInvokerActionsAsync();
    }

    /**
     * Waits for the completion of the specified futures.
     *
     * @param futures The list of futures to wait for.
     * @throws InterruptedException If the waiting is interrupted.
     * @throws ExecutionException   If an execution exception occurs.
     */
    public void waitForFutures(List<Future<ActionResult>> futures) throws InterruptedException, ExecutionException {
        invoker.waitForFutures(futures);
    }

    /**
     * Shuts down the executor service associated with the invoker.
     */
    public void shutdownExecutorService() {
        invoker.shutdownExecutorService();
    }

    /**
     * Returns a string representation of this invoker, including metrics and total time.
     *
     * @return A string representation of this invoker.
     */
    @Override
    public String toString() {
        return invoker.toString() +
                " | Metrics: " + invoker.getMetrics().toString() + " || TotalTime: " + chronometer();
    }

    /**
     * Adds all the metrics times from this invoker to get their total time.
     *
     * @return The total time of metrics associated with this invoker.
     */
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
