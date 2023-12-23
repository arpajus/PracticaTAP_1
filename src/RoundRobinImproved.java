import java.util.ArrayList;

public class RoundRobinImproved implements DistributionPolicy {
    private int currentInvokerIndex = 0; //This variable is usefull to know the last invoker that we've set an action

    @Override
    public void distributeActions(ArrayList<Action> actions, ArrayList<Invoker> invokers) {
        int initialIndex = currentInvokerIndex;
        for (Action action : actions) {
            Invoker invoker = findAvailableInvoker(invokers, action);

            if(invoker!=null){
                try {
                    invoker.setAction(action);
                    System.out.println("Action "+action.getId()+" assigned to Invoker "+(invokers.indexOf(invoker)+1));
                } catch (InsufficientMemoryException e) {
                    System.out.println("Error assigning action to Invoker "+(invokers.indexOf(invoker)+1)+": "+e.getMessage());
                }
            } else{
                System.out.println("No available invoker for action "+action.getId());
            }
        }
        currentInvokerIndex=initialIndex;
    }

    private Invoker findAvailableInvoker(ArrayList<Invoker> invokers, Action action) {
        int initialIndex=currentInvokerIndex;

        do {
            Invoker invoker=invokers.get(currentInvokerIndex);
            currentInvokerIndex=(currentInvokerIndex+1)%invokers.size();

            if(invoker.getTotalMemory()>=action.getMemory()) return invoker;
        } while (currentInvokerIndex!=initialIndex); //if the indexes match means that we've seen all the invokers

        return null;
    }

}
