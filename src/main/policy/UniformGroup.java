package main.policy;

import main.interfaces.DistributionPolicy;
import main.interfaces.InterfaceInvoker;
import main.Action;
import main.Invoker;
import main.exceptions.InsufficientMemoryException;

import java.util.ArrayList;

public class UniformGroup implements DistributionPolicy {

    @Override
    public boolean distributeActions(ArrayList<Action> actions, ArrayList<Invoker> invokers) {
        int actionsPerInvoker = actions.size() / invokers.size();
        int remainingActions = actions.size() % invokers.size();
        int invokersIndex = 0;

        boolean allReturned = true;
        // Iterate all invokers
        for (InterfaceInvoker invoker : invokers) {
            int actionsToAssign = actionsPerInvoker + (remainingActions-- > 0 ? 1 : 0);
            for (int i = 0; i < actionsToAssign; i++) {
                // Assign action to the first invoker then gets removed
                Action action = actions.get(0);
                actions.remove(0);
                try {
                    if (invoker.setAction(action)) {
                        System.out
                                .println(("Action " + action.getId() + " assigned to Invoker " + (invokersIndex + 1)));
                    }
                } catch (InsufficientMemoryException e) {
                    System.out.println(
                            "Error assigning sction to Invoker " + (invokersIndex + 1) + ": " + e.getMessage());
                    allReturned = false;
                }
            }
            invokersIndex++;
        }
        return allReturned;
    }
}