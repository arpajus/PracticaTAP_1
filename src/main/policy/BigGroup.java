package main.policy;

import main.interfaces.DistributionPolicy;
import main.Action;
import main.Invoker;
import main.exceptions.InsufficientMemoryException;

import java.util.ArrayList;

public class BigGroup implements DistributionPolicy {

    private final int groupSize;

    public BigGroup(int groupSize) {
        this.groupSize = groupSize;
    }

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
