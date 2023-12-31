package main.decorator;

import java.util.ArrayList;

import main.Action;
import main.Controller;
import main.Invoker;
import main.Metric;

public class MemorizationDecorator extends Controller {

    @Override
    public void addAction(Action action) {
        // TODO Auto-generated method stub
        super.addAction(action);
    }

    @Override
    public void addAction(Action action, int nTimes) {
        // TODO Auto-generated method stub
        super.addAction(action, nTimes);
    }

    @Override
    public void addInvoker(Invoker invoker) {
        // TODO Auto-generated method stub
        super.addInvoker(invoker);
    }

    @Override
    public void addInvoker(Invoker invoker, int nTimes) {
        // TODO Auto-generated method stub
        super.addInvoker(invoker, nTimes);
    }

    @Override
    public boolean distributeActions() {
        // TODO Auto-generated method stub
        return super.distributeActions();
    }

    @Override
    public void executeAssignedActions() {
        // TODO Auto-generated method stub
        super.executeAssignedActions();
    }

    @Override
    public Action getActionById(String id) {
        // TODO Auto-generated method stub
        return super.getActionById(id);
    }

    @Override
    public ArrayList<Action> getActions() {
        // TODO Auto-generated method stub
        return super.getActions();
    }

    @Override
    public int getId() {
        // TODO Auto-generated method stub
        return super.getId();
    }

    @Override
    public Invoker getInvokerById(String id) {
        // TODO Auto-generated method stub
        return super.getInvokerById(id);
    }

    @Override
    public ArrayList<Invoker> getInvokers() {
        // TODO Auto-generated method stub
        return super.getInvokers();
    }

    @Override
    public long getMaxExecutionTimeForAction(String actionId) {
        // TODO Auto-generated method stub
        return super.getMaxExecutionTimeForAction(actionId);
    }

    @Override
    public ArrayList<Metric> getMetrics() {
        // TODO Auto-generated method stub
        return super.getMetrics();
    }

    @Override
    public void printExecutionTimeForAction(String id) {
        // TODO Auto-generated method stub
        super.printExecutionTimeForAction(id);
    }

    @Override
    public void printMetrics() {
        // TODO Auto-generated method stub
        super.printMetrics();
    }

    @Override
    public void setActions(ArrayList<Action> actions) {
        // TODO Auto-generated method stub
        super.setActions(actions);
    }

    @Override
    public void setInvokers(ArrayList<Invoker> invokers) {
        // TODO Auto-generated method stub
        super.setInvokers(invokers);
    }

}
