package main.interfaces;

import main.Action;
import main.Metric;
import main.decorator.ActionResult;
import main.exceptions.InsufficientMemoryException;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public interface InterfaceInvoker {
    public String getId();

    public void setId(String id);

    public ArrayList<Action> getActions();

    public boolean setAction(Action action) throws InsufficientMemoryException;

    public double getTotalMemory();

    public void setTotalMemory(double totalMemory);

    public void releaseMemory(double memoryReleased);

    public ArrayList<ActionResult> executeInvokerActions() throws InsufficientMemoryException;

    public ActionResult executeThisAction(Action foundAction);

    public void addObserver(Observer observer);

    public ArrayList<Observer> getObservers();

    public void removeObserver(Observer observer);

    public void notifyObservers(ArrayList<Metric> metrics);

    public ConcurrentHashMap<String, ActionResult> getCache();

}
