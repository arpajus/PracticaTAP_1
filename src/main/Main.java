package main;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // every time we assign an action to an invoker we MUST invoke the method take
        // memory.
        // every time that an action has finished we MUST give memory to the Invoker.

        Controller controller = new Controller();
        RoundRobinImproved roundRobinImproved = new RoundRobinImproved();
        controller.setPolicy(roundRobinImproved);

        // We add the Observer
        // Observer observer=new Observer() {

        Invoker iv1 = new Invoker(1000);
        controller.addInvoker(iv1);
        Invoker iv2 = new Invoker(2000);
        controller.addInvoker(iv2);
        // we have to add the invokers to the controller enviroment

        int[] values = { 1, 2, 3, 4 };
        Adder add1 = new Adder("add1", 1500, values);
        controller.addAction(add1);
        Multiplier mltplr1 = new Multiplier("mltplr1", 200, values);
        controller.addAction(mltplr1);
        // same with actions, we have to add this actions to the invoker

        // This action is a test to invoke InsufficientMemoryException
        Adder add2 = new Adder("add2", 500, values);
        controller.addAction(add2);
        Adder add3 = new Adder("add3", 200, values);
        controller.addAction(add3);
        Adder add4 = new Adder("add4", 200, values);
        controller.addAction(add4);
        Adder add5 = new Adder("add5", 200, values);
        controller.addAction(add5);
        System.out.println("----------------------");

        System.out.println("----------------------");

        if (controller.distributeActions()) {
            System.out.println("Actions distributed");
            System.out.println("The actual memory of iv1 is " + iv1.getTotalMemory());
            System.out.println("The actual memory of iv2 is " + iv2.getTotalMemory());
            controller.executeAssignedActions();
            System.out.println("Actions executed");
            // when actions are executed the method realese memory is working
            System.out.println("The actual memory of iv1 is " + iv1.getTotalMemory());
            System.out.println("The actual memory of iv2 is " + iv2.getTotalMemory());
            // This shows the results of the actions that have been done
            System.out.println("Results of the actions: ");
            List<Action> results = controller.getActions();
            for (Action action : results) {
                if (action.getResult() != 0) {
                    System.out.println("Result of the action " + action.getId() + ": " + action.getResult());
                }
            }
        } else
            System.out.println("Not all actions could be assigned");
    }

    // TESTS ADRI_______________________________
    // TESTS ADRI_______________________________
    // TESTS ADRI_______________________________

    // cambio funciones que no se use fuera de su propia clase a private

}
