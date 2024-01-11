package main.main;

import main.Controller;
import main.Invoker;
import main.operations.Adder;
import main.policy.*;
import java.util.concurrent.Executors;

public class MainThreads {
    public static void main(String[] args) {
        // every time we assign an action to an invoker we MUST invoke the method take
        // memory.
        // every time that an action has finished we MUST give memory to the Invoker.
        Controller controller = Controller.getInstance();
        RoundRobinImproved roundRobinImproved = new RoundRobinImproved();
        controller.setPolicy(roundRobinImproved);
        // We add the Observer
        Invoker iv1 = new Invoker(1000, "1");
        controller.addInvoker(iv1);
        iv1.addObserver(controller);
        Invoker iv2 = new Invoker(2000, "2");

        controller.addInvoker(iv2);
        iv2.addObserver(controller);

        // setting the number of threads
        iv1.setExecutorService(Executors.newFixedThreadPool(5));
        iv2.setExecutorService(Executors.newFixedThreadPool(3));

        // we have to add the invokers to the controller enviroment
        int[] values = { 1, 2, 3, 4 };
        Adder add1 = new Adder("add", 10, values);
        controller.addAction(add1, 9);
        // same with actions, we have to add this actions to the invoker

        System.out.println("----------------------");
        System.out.println("----------------------");

        if (controller.distributeActions()) {
            System.out.println("Actions distributed");
            System.out.println("The actual memory of iv1 is " + controller.getInvokers().get(0).getTotalMemory());
            System.out.println("The actual memory of iv2 is " + controller.getInvokers().get(1).getTotalMemory());

            controller.executeAllInvokersAsync();
            System.out.println("All actions completed.");

            System.out.println("-----------------");
            controller.analyzeExecutionTime(controller.getMetrics());
            controller.analyzeInvokerMemory(controller.getMetrics());
            System.out.println("-------------");
            controller.analyzeExecutionTimeBis(controller.getMetrics());
        } else
            System.out.println("Not all actions could be assigned");
    }
}
