package main.policy;

import main.interfaces.DistributionPolicy;
import main.Action;
import main.Invoker;
import main.exceptions.InsufficientMemoryException;

import java.util.ArrayList;

public class RoundRobin implements DistributionPolicy {

    @Override
    public boolean distributeActions(ArrayList<Action> actions, ArrayList<Invoker> invokers) {
        int invokersIndex = 0;
        boolean allReturned = true;
        // Iterate all actions
        for (Action action : actions) {
            // Gets an invoker swaping between them (1,2,1,2...)
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
