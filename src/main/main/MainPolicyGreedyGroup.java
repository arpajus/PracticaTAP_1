package main.main;

import main.Controller;
import main.Invoker;
import main.decorator.InvokerCacheDecorator;
import main.operations.Adder;
import main.policy.*;
import java.util.concurrent.ExecutionException;

public class MainPolicyGreedyGroup {
    public static void main(String[] args) {
        Controller controller = Controller.getInstance();
        RoundRobinImproved roundRobinImproved = new RoundRobinImproved();
        controller.setPolicy(roundRobinImproved);
        Invoker iv1 = new InvokerCacheDecorator(new Invoker(1000, "1"));
        controller.addInvoker(iv1);
        iv1.addObserver(controller);
        int[] values = { 1, 2, 3, 4 };
        Adder add1 = new Adder("add1", 10, values);
        controller.addAction(add1, 9);

        System.out.println("----------------------");
        System.out.println("----------------------");

        if (controller.distributeActions()) {
            System.out.println("Actions distributed");
            System.out.println("The actual memory of iv1 is " + controller.getInvokers().get(0).getTotalMemory());
            try {
                controller.executeAllInvokersAsync();
                System.out.println("All actions completed.");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else
            System.out.println("Not all actions could be assigned");
    }
}
