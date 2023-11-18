import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

//El controler es UNICO (hacerlo unico de alguna manera) a nivel de clase, (solo hay un controller)
public class Controller {
    // mirar teoria de ED y pensar que estrucutras son mejores para lo que queremos
    // hacer
    private ArrayList<Invoker> invokers;
    private HashMap<String, Action> actions;

    public Controller(ArrayList<Invoker> invokers, HashMap<String, Action> actions) {
        this.invokers = invokers;
        this.actions = actions;
    }

    // creo que es mejor arraylist, porque los invokers son fijos (si hay 3 hay
    // siempre 3, o 30)
    // por lo que el acceso sera O(1), en vez de O(n) con linked list

    public Controller() {
        this.invokers = new ArrayList<Invoker>();
        this.actions = new HashMap<String, Action>();
    }

    public ArrayList<Invoker> getInvokers() {
        return invokers;
    }

    public void setInvokers(ArrayList<Invoker> invokers) {
        this.invokers = invokers;
    }

    public HashMap<String, Action> getActions() {
        return actions;
    }

    public void setActions(HashMap<String, Action> actions) {
        this.actions = actions;
    }

    public void addAction(Action action) {
        actions.put(action.getId(), action);
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

        // Lo comento porque no se para que sirve, y al cambiar el tipo de estructura
        // hay que reprogramarlo
        /*
         * //Esto que utilidad tiene? Osea creas un arraylist results donde añades todas
         * las actions
         * //Pero no se usa para nada, osea imprimes directamente de la lista actions,
         * se usara mas adelante?
         * List<Action> results = new ArrayList<>();
         * for (Action action : actions) {
         * results.add(action);
         * System.out.println(action);
         * }
         */
    }

    // decidir a que invoker asignar la accion (consultando sus recursos),
    // luego si internamente un invoker esta muy "a tope" puede relegar en
    // invokers de menor jerarquia para ayudarle (sin que el controller lo sepa)
    // mirar la teoria para hacer esto
}
