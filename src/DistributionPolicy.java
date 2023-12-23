import java.util.ArrayList;

public interface DistributionPolicy {
    boolean distributeActions(ArrayList<Action> actions, ArrayList<Invoker> invokers);
}
