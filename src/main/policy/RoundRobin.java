package main.policy;

import main.interfaces.DistributionPolicy;
import main.Action;
import main.Invoker;
import main.InsufficientMemoryException;
import java.util.ArrayList;

public class RoundRobin implements DistributionPolicy {

@Override
public boolean distributeActions(ArrayList<Action> actions, ArrayList<Invoker> invokers) {
    int invokersIndex = 0;
    boolean allReturned = true;
    for (Action action : actions) {
        Invoker invoker = invokers.get(invokersIndex);
        try {
            invoker.setAction(action);
            System.out.println("Action " + action.getId() + " assigned to Invoker " + (invokersIndex + 1));
        } catch (InsufficientMemoryException e) {
            System.out.println("Error assigning action to Invoker " + (invokersIndex + 1) + ": " + e.getMessage());
            allReturned = false;
        }
        invokersIndex = (invokersIndex + 1) % invokers.size();
    }
    return allReturned;
}

}
