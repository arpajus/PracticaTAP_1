package main.policy;

import main.interfaces.DistributionPolicy;
import main.Action;
import main.Invoker;
import main.exceptions.InsufficientMemoryException;

import java.util.ArrayList;

/**
 * Implementation of a distribution policy that assigns actions in groups to invokers.
 */
public class BigGroup implements DistributionPolicy {

    private final int groupSize;

    /**
     * Constructs a BigGroup distribution policy with the specified group size.
     *
     * @param groupSize The size of the action groups to be assigned.
     */
    public BigGroup(int groupSize) {
        this.groupSize = groupSize;
    }

    /**
     * Distributes a list of actions among a list of invokers using the big group strategy.
     *
     * @param actions  The list of actions to be distributed.
     * @param invokers The list of invokers available for distribution.
     * @return True if the actions were successfully distributed; false otherwise.
     */
    @Override
    public boolean distributeActions(ArrayList<Action> actions, ArrayList<Invoker> invokers) {
        // Check if invokers or actions are empty
        boolean assigned = false;
        if (invokers.isEmpty() || actions.isEmpty()) {
            return false;
        }

        // Iterate over groups until all actions are assigned
        for (int i = 0; i < actions.size(); i += groupSize) {
            // Create a group of actions
            ArrayList<Action> groupActions = new ArrayList<>();
            for (int j = i; j < i + groupSize && j < actions.size(); j++) {
                groupActions.add(actions.get(j));
            }

            // Try to assign the entire group to an invoker
            for (Invoker invoker : invokers) {
                if (canAssignGroup(invoker, groupActions)) {
                    assigned = true;
                    assignGroup(invoker, groupActions);
                    break;
                }
            }
        }
        return assigned;
    }

    private boolean canAssignGroup(Invoker invoker, ArrayList<Action> groupActions) {
        double groupMemory = groupActions.stream().mapToDouble(Action::getMemory).sum();
        return groupMemory <= invoker.getTotalMemory();
    }

    private void assignGroup(Invoker invoker, ArrayList<Action> groupActions) {
        for (Action action : groupActions) {
            try {
                invoker.setAction(action);
            } catch (InsufficientMemoryException e) {
                System.out.println("Error assigning action to Invoker " + invoker.getId() + ": " + e.getMessage());
            }
        }
    }
}
