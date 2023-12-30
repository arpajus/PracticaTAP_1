package main.interfaces;

import main.Action;
import main.Invoker;
import java.util.ArrayList;

//DistributionPolicy is a Strategy pattern
public interface DistributionPolicy {
    boolean distributeActions(ArrayList<Action> actions, ArrayList<Invoker> invokers);
}
