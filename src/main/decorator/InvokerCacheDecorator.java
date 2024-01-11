package main.decorator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import main.Action;
import main.Invoker;
import main.Metric;
import main.exceptions.InsufficientMemoryException;
import main.interfaces.Observer;

/**
 * Decorator for {@link Invoker} that adds the functionality to check the Invoker cache
 * to see if an action is already in the cache to avoid wasting execution resources.
 */
public class InvokerCacheDecorator extends Invoker {
    private Invoker invoker;

    /**
     * Constructs an {@code InvokerCacheDecorator} with the specified {@link Invoker}.
     *
     * @param invoker The original invoker to be decorated.
     */
    public InvokerCacheDecorator(Invoker invoker) {
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
     * Checks if an action is already in the cache and sets the action accordingly.
     *
     * @param action The action to be set.
     * @return {@code true} if the action is not in the cache, {@code false} if it is.
     * @throws InsufficientMemoryException If there is insufficient memory to add the action.
     */
    @Override
    public boolean setAction(Action action) throws InsufficientMemoryException {
        if (!searchHash(action.getValues(), action)) {
            return invoker.setAction(action);
        } else {
            return false;
        }
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
     * Searches the cache to see if an action is already present.
     *
     * @param values The input values of the action.
     * @param action The action to be searched. It checks the type of action.
     * @return {@code true} if the action is already present in the cache, {@code false} otherwise.
     */
    private boolean searchHash(int[] values, Action action) {
        for (ActionResult result : invoker.getCache().values()) {
            if (Arrays.equals(result.getInput(), values) && action.getClass().equals(result.getActionType())) {
                action.setResult(result.getResult());
                return true;
            }
        }
        return false;
    }
}
