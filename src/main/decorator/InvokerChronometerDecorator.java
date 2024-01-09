package main.decorator;

import java.util.ArrayList;
import java.util.Optional;

import main.Action;
import main.Controller;
import main.Invoker;
import main.Metric;
import main.exceptions.InsufficientMemoryException;
import main.interfaces.Observer;
import java.util.concurrent.ConcurrentHashMap;

public class InvokerChronometerDecorator extends Invoker {
    private Invoker invoker;

    public InvokerChronometerDecorator(Invoker invoker) {
        super(invoker.getTotalMemory(), invoker.getId());
        this.invoker = invoker;
    }

    @Override
    public String getId() {
        return invoker.getId();
    }

    @Override
    public void setId(String id) {
        invoker.setId(id);
    }

    @Override
    public ArrayList<Action> getActions() {
        return invoker.getActions();
    }

    @Override
    public boolean setAction(Action action) throws InsufficientMemoryException {
        return invoker.setAction(action);
    }

    @Override
    public double getTotalMemory() {
        return invoker.getTotalMemory();
    }

    @Override
    public void setTotalMemory(double totalMemory) {
        invoker.setTotalMemory(totalMemory);
    }

    @Override
    public void releaseMemory(double memoryReleased) {
        invoker.releaseMemory(memoryReleased);
    }

    @Override
    public ArrayList<ActionResult> executeInvokerActions() throws InsufficientMemoryException {
        return invoker.executeInvokerActions();
    }

    @Override
    public void addObserver(Observer observer) {
        invoker.addObserver(observer);
    }

    @Override
    public ArrayList<Observer> getObservers() {
        return invoker.getObservers();
    }

    @Override
    public void removeObserver(Observer observer) {
        invoker.removeObserver(observer);
    }

    @Override
    public void notifyObservers(ArrayList<Metric> metrics) {
        invoker.notifyObservers(metrics);
    }

    public ConcurrentHashMap<String, ActionResult> getCache() {
        return invoker.getCache();
    }

    public String chronometer(Action action) {
        Controller controller = Controller.getInstance();
        ArrayList<Metric> metrics = controller.getMetrics();
        String text = "";
        Optional<Metric> actionMetric = metrics.stream()
                .filter(metric -> metric.getActionId().equals(action.getId()))
                .findFirst();
        if (actionMetric.isPresent()) {
            Metric m = actionMetric.get();
            text = "Action: " + m.getActionId() +
                    ", Time: " + m.getExecutionTime() +
                    ", Invoker: " + m.getAssignedInvoker().getId() +
                    ", Memory Used: " + m.getUsedMemory();
                    System.out.println(text);
            return text;
        }
        return text;
    }
}
