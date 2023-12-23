import java.util.ArrayList;

public class RoundRobin implements DistributionPolicy {

    @Override
    public void distributeActions(ArrayList<Action> actions, ArrayList<Invoker> invokers) {
        int invokersIndex = 0;

        for (Action action : actions) {
            Invoker invoker = invokers.get(invokersIndex);
            try {
                invoker.setAction(action);
                System.out.println("Action " + action.getId() + " assigned to Invoker " + invokersIndex+1);
            } catch (InsufficientMemoryException e) {
                System.out.println("Error assigning action to Invoker "+invokersIndex+": "+e.getMessage());
            }
            invokersIndex=(invokersIndex+1)%invokers.size();
        }
    }

}
