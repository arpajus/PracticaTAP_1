import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Controller {
    private List invokers;
    private List<Action> actions;

    public Controller(List invokers, List actions) {
        this.invokers = invokers;
        this.actions = actions;
    }

    public List getInvokers() {
        return invokers;
    }

    public void setInvokers(List invokers) {
        this.invokers = invokers;
    }

    public List getActions() {
        return actions;
    }

    public void setActions(List actions) {
        this.actions = actions;
    }


    public Controller() {
        this.invokers=new LinkedList();
        this.actions=new LinkedList();
    }

    public void addAction(Action action){
        actions.add(action);
    }

    public void addInvoker(Invoker invoker){
        invokers.add(invoker);
    }
    
    public void executeActions() {
        Iterator<Invoker> iterator = invokers.iterator();
        while (iterator.hasNext()) {
            Invoker invoker = iterator.next();
            invoker.executeAction();
            invoker.giveMemory(invoker.getAction().getMemory()); //we give memory because exactly in this moment we've finished the action related to the concret invoker. 
        }
        
        List<Action> results = new ArrayList<>();
        for (Action action : actions) {
            results.add(action);
            System.out.println(action);
        }
    }
    
}
