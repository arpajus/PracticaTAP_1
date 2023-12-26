package main.policy;

import main.interfaces.DistributionPolicy;
import main.Action;
import main.Invoker;
import main.InsufficientMemoryException;
import java.util.ArrayList;
import java.util.Iterator;

public class GreedyGroup implements DistributionPolicy {
    @Override
    public boolean distributeActions(ArrayList<Action> actions, ArrayList<Invoker> invokers) {
        boolean allReturned = true;

        for (Action action : actions) {
            Invoker invoker = findAvailableInvoker(invokers, action);
            if (invoker != null) {
                try {
                    invoker.setAction(action);
                    System.out.println(
                            "Action " + action.getId() + " assigned to Invoker " + (invokers.indexOf(invoker) + 1));
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
        return allReturned;
    }

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