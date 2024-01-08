package main;

import main.operations.Adder;
import main.policy.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import main.decorator.Result;


public class Main2 {
    public static void main(String[] args) {
        // every time we assign an action to an invoker we MUST invoke the method take
        // memory.
        // every time that an action has finished we MUST give memory to the Invoker.
        Controller controller = Controller.getInstance();
        RoundRobinImproved roundRobinImproved = new RoundRobinImproved();
        controller.setPolicy(roundRobinImproved);
        // We add the Observer
        // Observer observer=new Observer() {
        Invoker iv1 = new Invoker(1000, "1");
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

            List<Future<Result>> futures = iv1.executeInvokerActionsAsync();  
            List<Future<Result>> futures2 = iv2.executeInvokerActionsAsync();

            try {
                iv1.waitForFutures(futures);
                iv2.waitForFutures(futures2);

                iv1.shutdownExecutorService();
                iv2.shutdownExecutorService();

                controller.analyzeExecutionTime(controller.getMetrics());
                controller.printMetrics();
                controller.analyzeExecutionTimeBis(controller.getMetrics());

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else
            System.out.println("Not all actions could be assigned");
    }
}
