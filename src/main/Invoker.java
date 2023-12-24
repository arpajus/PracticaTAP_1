package main;

import java.util.ArrayList;
import main.interfaces.Observer;
import java.util.HashMap;
import java.util.List;

import org.junit.internal.ArrayComparisonFailure;

public class Invoker {
    private ArrayList<Action> actions;
    private double totalMemory; // memory -> MB
    private int id;
    private ArrayList<Observer> observers=new ArrayList<>();
    private ArrayList<Metric> metrics=new ArrayList<>();

    //guarda en cache el id del action y su resultado (object porque yo no se que devuelve action)
    private HashMap<String,Object> cache;
    
    
    // invokers hijos?

    public Invoker(double totalMemory, int id) {
        this.totalMemory = totalMemory;
        this.id=id;
        this.actions = new ArrayList<Action>();
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setAction(Action action) throws InsufficientMemoryException {
   /*      if (cache.containsKey(action.getId())){
            //esta accion esta en cache, no hace falta calcular
            
            //hacer que setAction devuelva algo?
            //return cache.get(action.getId())
        } */
        if (action.getMemory() <= totalMemory) {
            actions.add(action);
            action.setInvoker(this);
            takeMemory(action.getMemory());
            // we take memory because exactly in this moment we've associate the action to
            // the invoker.
        } else {
            throw new InsufficientMemoryException("Not enough memory to take the action");
        }

    }

    public double getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(double totalMemory) {
        this.totalMemory = totalMemory;
    }

    // method that substracts the action memory of the actual memory of invoker. We
    // should invoke this method when we invoke an action to the Invoker
    private void takeMemory(double memoryToTake) {
        totalMemory = totalMemory - memoryToTake;
    }

    // method that gives back the action memory to the invoker. We should invoke
    // this method when the action is finished
    public void releaseMemory(double memoryReleased) {
        totalMemory = totalMemory + memoryReleased;
    }

    //executes all actions at once
    public void executeInvokerActions() throws InsufficientMemoryException {
        long startTime = System.currentTimeMillis();
        ArrayList<Metric> metricsToNotify=new ArrayList<>();
        for (Action action : actions) {
            System.out.println("Executing action: " + action.getId());
            
            action.operation();
            //--------------------------value es operation result, que no se cual sera
            //cache.put(action.getId(), "operation_result");

            long endTime = System.currentTimeMillis();
            Metric metric = new Metric(action.getId(), endTime - startTime, action.getInvoker(), action.getMemory());
            metricsToNotify.add(metric);
        }
        notifyObservers(metricsToNotify);
        actions.clear();
        System.out.println("Metrics recorded."); 
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer){
        observers.remove(observer);
    }

    public void notifyObservers(ArrayList<Metric> metrics) {
            for (Observer observer : observers) {
                observer.updateMetric(metrics);
            }
        }

    

    // Merece la pena añadir interfaces para todas estas clases? Pensar y decidir

    // FALTAN DECORATORS, para funcionalidades adicionales como caching and timing.
    // SON CLASES DIFERENTES?? Mirar la teoria a ver si pone algo al respecto

    // Añadir jerarquia de delegacion de invokers a invokers en 3 niveles
    // (COMPOSITE)

    /*
     * El Controller puede sobrecargarse si debe gestionar muchos Invokers. Para
     * simplificar el
     * proceso, cada Invoker podrá gestionar internamente diferentes niveles
     * Invokers. Un Invoker
     * podrá pasar una petición de invocación a los Invokers de niveles más bajos.
     * Implementad el
     * sistema para soportar varias jerarquías (3 niveles) de Invokers que operen
     * con normalidad y
     * de manera transparente al Controller.
     * Usad el patrón composite para implementar la jerarquía de Invokers.
     */
}
