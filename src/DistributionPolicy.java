import java.util.ArrayList;

public interface DistributionPolicy {
    void distributeActions(ArrayList<Action> actions, ArrayList<Invoker> invokers);
}
