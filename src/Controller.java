import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

//El controler es UNICO (hacerlo unico de alguna manera)
public class Controller {
    private List<Invoker> invokers;
    private List<Action> actions;

    public Controller(List<Invoker> invokers, List<Action> actions) {
        this.invokers = invokers;
        this.actions = actions;
    }

    //Contructor sin parametros crea linked list de cada uno (en vez de una lista pasada por parametros)
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
            //es mejor declarar dentro o fuera del bucle?
            Invoker invoker = iterator.next();
            invoker.executeAction();
            //EL INVOKER ES EL QUE RESERVA NO EL CONTROLLER, quiza se tiene que hacer el givememory dentro del invoker? No lo se, tengo que mirar la teoria
            invoker.releaseMemory(invoker.getAction().getMemory()); 
            // we give memory because exactly in this moment we've
            // finished the action related to the concret invoker.
        }

        List<Action> results = new ArrayList<>();
        for (Action action : actions) {
            results.add(action);
            System.out.println(action);
        }
    }
}
