package main.main;

import main.Action;
import main.Controller;
import main.Invoker;
import main.decorator.InvokerCacheDecorator;
import main.operations.Adder;
import main.policy.*;
import java.math.BigInteger;
import java.util.List;

public class MainObserver {
    public static void main(String[] args) {
        // every time we assign an action to an invoker we MUST invoke the method take
        // memory.
        // every time that an action has finished we MUST give memory to the Invoker.
        Controller controller = Controller.getInstance();
        RoundRobinImproved roundRobinImproved = new RoundRobinImproved();
        controller.setPolicy(roundRobinImproved);
        // We add the Observer
        // Observer observer=new Observer() {
        // Invoker iv1 = new Invoker(1000, "1");
        Invoker iv1 = new InvokerCacheDecorator(new Invoker(1000, "1"));

        controller.addInvoker(iv1);
        iv1.addObserver(controller);

        Invoker iv2 = new Invoker(2000, "2");
        controller.addInvoker(iv2);
        iv2.addObserver(controller);

        // we have to add the invokers to the controller enviroment
        int[] values = { 1, 2, 3, 4 };
        Adder add1 = new Adder("add1", 10, values);
        controller.addAction(add1, 9);
        // same with actions, we have to add this actions to the invoker

        System.out.println("----------------------");
        System.out.println("----------------------");

        if (controller.distributeActions()) {
            System.out.println("Actions distributed");
            System.out.println("The actual memory of iv1 is " + controller.getInvokers().get(0).getTotalMemory());
            System.out.println("The actual memory of iv2 is " + controller.getInvokers().get(1).getTotalMemory());

            controller.executeAssignedActions();
            System.out.println("Actions executed");

            controller.printMetrics();
            // when actions are executed the method realese memory is working
            System.out.println("The actual memory of iv1 is " + controller.getInvokers().get(0).getTotalMemory());
            System.out.println("The actual memory of iv2 is " + controller.getInvokers().get(1).getTotalMemory());

            System.out.println("-----------------");
            controller.analyzeExecutionTime(controller.getMetrics());
            controller.analyzeInvokerMemory(controller.getMetrics());
            System.out.println("-------------");
            controller.analyzeExecutionTimeBis(controller.getMetrics());
            // This shows the results of the actions that have been done
            System.out.println("Results of the actions: ");
            List<Action> results = controller.getActions();
            for (Action action : results) {
                if (action.getResult() != BigInteger.ZERO) {
                    System.out.println("Result of the action " + action.getId() + ": " + action.getResult());
                }
            }
        } else
            System.out.println("Not all actions could be assigned");
    }
}