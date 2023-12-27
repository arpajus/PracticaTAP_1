package main.policy;

import main.interfaces.DistributionPolicy;
import main.Action;
import main.InsufficientMemoryException;
import main.Invoker;
import java.util.ArrayList;

public class BigGroup implements DistributionPolicy {

    private final int groupSize;

    public BigGroup(int groupSize) {
        this.groupSize = groupSize;
    }

    @Override
    public boolean distributeActions(ArrayList<Action> actions, ArrayList<Invoker> invokers) {
        if (invokers.isEmpty() || actions.isEmpty()) {
            return false;
        }

        for (Invoker invoker : invokers) {
            for (int i = 0; i < groupSize && !actions.isEmpty(); i++) {
                Action action = actions.remove(0);
                try {
                    invoker.setAction(action);
                } catch (InsufficientMemoryException e) {
                    System.out.println(
                            "Error assigning sction to Invoker " + (invoker.getId() + 1) + ": " + e.getMessage());
                }
            }
        }

        return true;
    }

}