package main.policy;

import main.interfaces.DistributionPolicy;
import main.Action;
import main.Invoker;
import main.exceptions.InsufficientMemoryException;

import java.util.ArrayList;

/**
 * Implementation of a distribution policy that assigns actions in a round-robin fashion to invokers.
 * This version improves the assignment strategy by considering available memory for each invoker.
 */
public class RoundRobinImproved implements DistributionPolicy {
    private int currentInvokerIndex = 0; // This variable is useful to know the last invoker that we've set an action

    /**
     * Distributes a list of actions among a list of invokers using an improved round-robin strategy.
     * This strategy considers available memory for each invoker.
     *
     * @param actions  The list of actions to be distributed.
     * @param invokers The list of invokers available for distribution.
     * @return True if the actions were successfully distributed; false otherwise.
     */
    @Override
    public boolean distributeActions(ArrayList<Action> actions, ArrayList<Invoker> invokers) {
        int initialIndex = currentInvokerIndex;
        boolean allReturned = true;

        // Iterate all actions
        for (Action action : actions) {
            Invoker invoker = findAvailableInvoker(invokers, action);

            if (invoker != null) {
                try {
                    // Assign action
                    if (invoker.setAction(action)) {
                        System.out.println(
                                "Action " + action.getId() + " assigned to Invoker " + (invokers.indexOf(invoker) + 1));
                    }
                } catch (InsufficientMemoryException e) {
                    System.out.println("Error assigning action to Invoker " + (invokers.indexOf(invoker) + 1) + ": "
                            + e.getMessage());
                    allReturned = false;
                }
            } else {
                System.out.println("No available invoker for action " + action.getId());
                allReturned = false;
            }
        }
        currentInvokerIndex = initialIndex;
        return allReturned;
    }

    private Invoker findAvailableInvoker(ArrayList<Invoker> invokers, Action action) {
        int initialIndex = currentInvokerIndex;

        do {
            // Gets an invoker swapping between them (1,2,1,2...) only if it fits
            Invoker invoker = invokers.get(currentInvokerIndex);
            currentInvokerIndex = (currentInvokerIndex + 1) % invokers.size();

            if (invoker.getTotalMemory() >= action.getMemory())
                return invoker;
        } while (currentInvokerIndex != initialIndex); // if the indexes match means that we've seen all the invokers

        return null;
    }
}
