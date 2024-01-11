package main.policy;

import main.interfaces.DistributionPolicy;
import main.Action;

import java.util.ArrayList;
import java.util.Iterator;
import main.Invoker;
import main.exceptions.InsufficientMemoryException;

/**
 * Implementation of a distribution policy that greedily assigns actions to available invokers.
 */
public class GreedyGroup implements DistributionPolicy {

    /**
     * Distributes a list of actions among a list of invokers using the greedy group strategy.
     *
     * @param actions  The list of actions to be distributed.
     * @param invokers The list of invokers available for distribution.
     * @return True if the actions were successfully distributed; false otherwise.
     */
    @Override
    public boolean distributeActions(ArrayList<Action> actions, ArrayList<Invoker> invokers) {
        boolean assigned = false;

        // Iterate all actions
        for (Action action : actions) {
            Invoker invoker = findAvailableInvoker(invokers, action);
            if (invoker != null) {
                try {
                    // Assign action
                    if (invoker.setAction(action)) {
                        assigned = true;
                        System.out.println(
                                "Action " + action.getId() + " assigned to Invoker " + (invokers.indexOf(invoker) + 1));
                    }
                } catch (InsufficientMemoryException e) {
                    System.out.println("Error assigning action to Invoker " + (invokers.indexOf(invoker) + 1) + ": "
                            + e.getMessage());
                }
            } else {
                System.out.println("No available invoker for action " + action.getId());
            }
        }
        return assigned;
    }

    /**
     * Finds an available invoker for a given action based on memory constraints.
     *
     * @param invokers The list of invokers to search.
     * @param action   The action to be assigned.
     * @return An available invoker if found; otherwise, null.
     */
    private Invoker findAvailableInvoker(ArrayList<Invoker> invokers, Action action) {
        Iterator<Invoker> iterator = invokers.iterator();
        while (iterator.hasNext()) {
            Invoker invoker = iterator.next();
            if (invoker.getTotalMemory() >= action.getMemory()) {
                return invoker;
            }
        }
        return null;
    }
}
