package main.main;

import main.Controller;
import main.Invoker;
import main.decorator.InvokerCacheDecorator;
import main.operations.Adder;
import main.policy.*;
import java.util.concurrent.ExecutionException;

public class MainReflection {
    public static void main(String[] args) {
        // every time we assign an action to an invoker we MUST invoke the method take
        // memory.
        // every time that an action has finished we MUST give memory to the Invoker.
        Controller controller = Controller.getInstance();
        RoundRobinImproved roundRobinImproved = new RoundRobinImproved();
        controller.setPolicy(roundRobinImproved);
        // We add the Observer
        Invoker iv1 = new InvokerCacheDecorator(new Invoker(1000, "1"));
        controller.addInvoker(iv1);
        iv1.addObserver(controller);
        /*Invoker iv2 = new InvokerCacheDecorator(new Invoker(2000, "2"));

        controller.addInvoker(iv2);
        iv2.addObserver(controller);*/
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
            //System.out.println("The actual memory of iv2 is " + controller.getInvokers().get(1).getTotalMemory());

            try {

                controller.executeAllInvokersAsync();
                System.out.println("All actions completed.");

                /*
                 * System.out.println("-----------------");
                 * controller.analyzeExecutionTime(controller.getMetrics());
                 * controller.analyzeInvokerMemory(controller.getMetrics());
                 * System.out.println("-------------");
                 * controller.analyzeExecutionTimeBis(controller.getMetrics());
                 */

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else
            System.out.println("Not all actions could be assigned");
    }
}
