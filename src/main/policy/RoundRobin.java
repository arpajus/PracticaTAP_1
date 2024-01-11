package main.policy;

import main.interfaces.DistributionPolicy;
import main.Action;
import main.Invoker;
import main.exceptions.InsufficientMemoryException;

import java.util.ArrayList;

/**
 * Implementation of a distribution policy that assigns actions in a round-robin fashion to invokers.
 */
public class RoundRobin implements DistributionPolicy {

    /**
     * Distributes a list of actions among a list of invokers using the round-robin strategy.
     *
     * @param actions  The list of actions to be distributed.
     * @param invokers The list of invokers available for distribution.
     * @return True if the actions were successfully distributed; false otherwise.
     */
    @Override
    public boolean distributeActions(ArrayList<Action> actions, ArrayList<Invoker> invokers) {
        int invokersIndex = 0;
        boolean allReturned = true;
        // Iterate all actions
        for (Action action : actions) {
            // Gets an invoker swapping between them (1,2,1,2...)
            Invoker invoker = invokers.get(invokersIndex);
            try {
                if (invoker.setAction(action)) {
                    System.out.println("Action " + action.getId() + " assigned to Invoker " + (invokersIndex + 1));
                }
            } catch (InsufficientMemoryException e) {
                System.out.println("Error assigning action to Invoker " + (invokersIndex + 1) + ": " + e.getMessage());
                allReturned = false;
            }
            invokersIndex = (invokersIndex + 1) % invokers.size();
        }
        return allReturned;
    }
}
