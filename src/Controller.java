import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

//El controler es UNICO (hacerlo unico de alguna manera) a nivel de clase, (solo hay un controller)
public class Controller {
    private List<Invoker> invokers;
    private List<Action> actions;

    public Controller(List<Invoker> invokers, List<Action> actions) {
        this.invokers = invokers;
        this.actions = actions;
    }

    public Controller() {
        this.invokers = new LinkedList<Invoker>();
        this.actions = new LinkedList<Action>();
    }

    public List<Invoker> getInvokers() {
        return invokers;
    }

    public void setInvokers(List<Invoker> invokers) {
        this.invokers = invokers;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public void addInvoker(Invoker invoker) {
        invokers.add(invoker);
    }

    public void executeActions() {
        Iterator<Invoker> iterator = invokers.iterator();
        while (iterator.hasNext()) {
            Invoker invoker = iterator.next();
            invoker.executeAction();
            invoker.releaseMemory(invoker.getAction().getMemory()); 
            // we give memory because exactly in this moment we've
            // finished the action related to the concret invoker.
        }

        //Esto que utilidad tiene? Osea creas un arraylist results donde añades todas las actions
        //Pero no se usa para nada, osea imprimes directamente de la lista actions, se usara mas adelante?
        List<Action> results = new ArrayList<>();
        for (Action action : actions) {
            results.add(action);
            System.out.println(action);
        }
    }
}
