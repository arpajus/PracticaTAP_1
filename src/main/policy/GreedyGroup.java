package main.policy;

import main.interfaces.DistributionPolicy;
import main.Action;

import java.util.ArrayList;
import java.util.Iterator;
import main.Invoker;
import main.exceptions.InsufficientMemoryException;

public class GreedyGroup implements DistributionPolicy {

    @Override
    public boolean distributeActions(ArrayList<Action> actions, ArrayList<Invoker> invokers) {
        boolean assigned = false;

        for (Action action : actions) {
            Invoker invoker = findAvailableInvoker(invokers, action);
            if (invoker != null) {
                try {
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