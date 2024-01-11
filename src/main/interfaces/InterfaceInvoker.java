package main.interfaces;

import main.Action;
import main.Metric;
import main.decorator.ActionResult;
import main.exceptions.InsufficientMemoryException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Interface representing an invoker that can perform actions.
 */
public interface InterfaceInvoker {

    /**
     * Gets the ID of the invoker.
     *
     * @return The ID of the invoker.
     */
    public String getId();

    /**
     * Sets the ID of the invoker.
     *
     * @param id The new ID to be set.
     */
    public void setId(String id);

    /**
     * Gets the list of actions associated with the invoker.
     *
     * @return The list of actions.
     */
    public ArrayList<Action> getActions();

    /**
     * Sets an action for execution by the invoker.
     *
     * @param action The action to be set.
     * @return {@code true} if the action is set successfully, {@code false} otherwise.
     * @throws InsufficientMemoryException If there is insufficient memory to add the action.
     */
    public boolean setAction(Action action) throws InsufficientMemoryException;

    /**
     * Gets the total memory available to the invoker.
     *
     * @return The total memory.
     */
    public double getTotalMemory();

    /**
     * Sets the total memory available to the invoker.
     *
     * @param totalMemory The new total memory to be set.
     */
    public void setTotalMemory(double totalMemory);

    /**
     * Releases a specified amount of memory.
     *
     * @param memoryReleased The amount of memory to be released.
     */
    public void releaseMemory(double memoryReleased);

    /**
     * Executes the actions associated with the invoker and returns the results.
     *
     * @return A list of ActionResults.
     * @throws InsufficientMemoryException If there is insufficient memory to execute the actions.
     */
    public ArrayList<ActionResult> executeInvokerActions() throws InsufficientMemoryException;

    /**
     * Executes a specific action and returns the result.
     *
     * @param foundAction The action to be executed.
     * @return The result of the executed action.
     */
    public ActionResult executeThisAction(Action foundAction);

    /**
     * Adds an observer to the list of observers.
     *
     * @param observer The observer to be added.
     */
    public void addObserver(Observer observer);

    /**
     * Gets the list of observers associated with the invoker.
     *
     * @return The list of observers.
     */
    public ArrayList<Observer> getObservers();

    /**
     * Removes an observer from the list of observers.
     *
     * @param observer The observer to be removed.
     */
    public void removeObserver(Observer observer);

    /**
     * Notifies the observers with the specified metrics.
     *
     * @param metrics The metrics to be sent to the observers.
     */
    public void notifyObservers(ArrayList<Metric> metrics);

    /**
     * Gets the cache associated with the invoker.
     *
     * @return The cache.
     */
    public ConcurrentHashMap<String, ActionResult> getCache();

    /**
     * Executes the actions associated with the invoker asynchronously.
     *
     * @return The list of futures representing the asynchronous execution of actions.
     */
    public List<Future<ActionResult>> executeInvokerActionsAsync();

    /**
     * Waits for the completion of the specified futures.
     *
     * @param futures The list of futures to wait for.
     * @throws InterruptedException If the waiting is interrupted.
     * @throws ExecutionException   If an execution exception occurs.
     */
    public void waitForFutures(List<Future<ActionResult>> futures) throws InterruptedException, ExecutionException;

    /**
     * Shuts down the executor service associated with the invoker.
     */
    public void shutdownExecutorService();
}
