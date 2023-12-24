package main.policy;

import main.interfaces.DistributionPolicy;
import main.Action;
import main.Invoker;
import main.InsufficientMemoryException;
import java.util.ArrayList;

public class BigGroup implements DistributionPolicy {

    @Override
    public boolean distributeActions(ArrayList<Action> actions, ArrayList<Invoker> invokers) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'distributeActions'");
    }

}