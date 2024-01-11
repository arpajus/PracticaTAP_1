package main.main;

import main.Controller;
import main.Invoker;
import main.decorator.InvokerCacheDecorator;
import main.operations.Adder;
import main.operations.Factorial;

import java.math.BigInteger;
import java.util.Scanner;

import main.policy.*;

public class MainCacheDecorator {
    public static void main(String[] args) {
        Controller controller = Controller.getInstance();
        controller.setPolicy(new GreedyGroup());

        Invoker iv1;
        Invoker iv2;

        // Choose mode
        System.out.println("\n\n[0] - DO NOT use cache decorator");
        System.out.println("[Any Key] - Use cache decorator");
        System.out.print("\nChoice: ");

        Scanner sc = new Scanner(System.in);
        char input = sc.next().charAt(0);
        if (input == '0') {
            iv1 = new Invoker(2500, "1");
            iv2 = new Invoker(1500, "2");
        } else {
            iv1 = new InvokerCacheDecorator(new Invoker(2500, "1"));
            iv2 = new InvokerCacheDecorator(new Invoker(1500, "2"));
        }
        sc.close();

        controller.addInvoker(iv1);
        controller.addInvoker(iv2);

        int[] values = { 1, 2, 3, 4 };
        int[] values2 = { 5 };
        Adder add1 = new Adder("add1", 2000, values);
        Adder add2 = new Adder("add2", 100, values);
        Factorial f5 = new Factorial("f5", 800, values2);

        iv1.addObserver(controller);
        controller.addAction(add1);
        controller.addAction(add2, 3); // action add2 gets added 3 times as: add2_0, add2_1, add2_2
        controller.addAction(f5);

        System.out.println("----------------------");
        System.out.println("----------------------");
        System.out.println("Invokers info before distributing actions: ");
        System.out.println(controller.getInvokerById("1").toString());
        System.out.println(controller.getInvokerById("2").toString());
        System.out.println("----------------------");
        System.out.println("----------------------");

        // At this point nothing is in cache, so it distributes and executes everything
        if (controller.distributeActions()) {
            if (distributeActionsOk1()) {
                System.out.println("---------------- Actions distributed as expected ----------------");
            } else {
                System.out.println("-------------- Actions distributed NOT as expected ---------------");
            }
            System.out.println("Invokers info after distributing actions: ");
            System.out.println(controller.getInvokerById("1").toString());
            System.out.println(controller.getInvokerById("2").toString());
            System.out.println("----------------------");
            System.out.println("----------------------");

            System.out.println("Invoker 1 Cache is empty");
            System.out.println("Invoker 2 Cache is empty");
            controller.executeAssignedActions();
            if (executeActionsOk1()) {
                System.out.println("---------------- Actions executed as expected ----------------");
            } else {
                System.out.println("-------------- Actions executed NOT as expected ---------------");
            }
            System.out.println(controller.getInvokerById("1").toString());
            System.out.println(controller.getInvokerById("2").toString());
            System.out.println("Invoker 1 Cache has: adder [values {1,2,3,4}]");
            System.out.println("Invoker 2 Cache has: factorial [values {5}]");

        } else {
            System.out.println("Not all actions could be assigned");
        }

        System.out.println("----------------------");
        System.out.println("----------------------");
        System.out.println("Invokers info before distributing actions: ");
        System.out.println(controller.getInvokerById("1").toString());
        System.out.println(controller.getInvokerById("2").toString());
        System.out.println("----------------------");
        System.out.println("----------------------");

        /*
         * At this point Invoker "1" cache has: adder operation with [values {1,2,3,4}]
         * At this point Invoker "2" cache has: factorial operation with [values {5}]
         * It will only distribute action f5 due to its policy (GreedyGroup), because it
         * tries to assign all to Invoker "1", since f5 is not in his Cache, it gets
         * distributed and executed
         */
        if (controller.distributeActions()) {
            if (distributeActionsOk2()) {
                System.out.println("---------------- Actions distributed as expected ----------------");
            } else {
                System.out.println("-------------- Actions distributed NOT as expected ---------------");
            }
            System.out.println("Invokers info after distributing actions: ");
            System.out.println(controller.getInvokerById("1").toString());
            System.out.println(controller.getInvokerById("2").toString());
            System.out.println("----------------------");
            System.out.println("----------------------");

            controller.executeAssignedActions();
            if (executeActionsOk1()) {
                System.out.println("---------------- Actions executed as expected ----------------");
            } else {
                System.out.println("-------------- Actions executed NOT as expected ---------------");
            }
            System.out.println(controller.getInvokerById("1").toString());
            System.out.println(controller.getInvokerById("2").toString());
            System.out.println("Invoker 1 Cache has: adder [values {1,2,3,4}], factorial [values {5}]");
            System.out.println("Invoker 2 Cache has: factorial [values {5}]");
        } else {
            System.out.println("Not all actions could be assigned");
        }

        Adder add3 = new Adder("add3", 100, values);
        Adder add4 = new Adder("add4", 100, values);
        Factorial f1 = new Factorial("f1", 100, values2);
        Factorial f2 = new Factorial("f2", 100, values2);
        Factorial f3 = new Factorial("f3", 100, values2);
        Factorial f4 = new Factorial("f4", 100, values2);

        controller.addAction(add3);
        controller.addAction(add4);
        controller.addAction(f1);
        controller.addAction(f2);
        controller.addAction(f3);
        controller.addAction(f4);

        System.out.println("----------------------");
        System.out.println("----------------------");
        System.out.println("----CACHE LOADED------");
        System.out.println("----------------------");
        System.out.println("----------------------");
        System.out.println("Now if input and operation is in cache it gets it from cache (no sleep)");
        System.out.println("----------------------");
        System.out.println("----------------------");
        System.out.println("Invokers info before distributing actions: ");
        System.out.println(controller.getInvokerById("1").toString());
        System.out.println(controller.getInvokerById("2").toString());
        System.out.println("----------------------");
        System.out.println("----------------------");
        /*
         * At this point Invoker "1" cache has:
         * adder -> [values {1,2,3,4}], factorial -> [values {5}]
         * At this point Invoker "2" cache has:
         * factorial -> [values {5}]
         * Tries to distribute add3, add4, f1, f2, f3, f4 to Invoker 1 first due to its policy (GreedyGroup).
         * It won't distribute any of the new actions nor execute them, because they share its actionType
         * and input (already in cache).
         */
        if (controller.distributeActions()) {
            if (distributeActionsOk3()) {
                System.out.println("---------------- Actions distributed as expected ----------------");
            } else {
                System.out.println("-------------- Actions distributed NOT as expected ---------------");
            }
            System.out.println("Invokers info after distributing actions: ");
            System.out.println(controller.getInvokerById("1").toString());
            System.out.println(controller.getInvokerById("2").toString());
            System.out.println("----------------------");
            System.out.println("----------------------");

            controller.executeAssignedActions();
            if (executeActionsOk1()) {
                System.out.println("---------------- Actions executed as expected ----------------");
            } else {
                System.out.println("-------------- Actions executed NOT as expected ---------------");
            }
            System.out.println(controller.getInvokerById("1").toString());
            System.out.println(controller.getInvokerById("2").toString());
            System.out.println("Invoker 1 Cache has: adder [values {1,2,3,4}], factorial [values {5}]");
            System.out.println("Invoker 2 Cache has: factorial [values {5}]");
        } else {
            System.out.println(
                    "Not all actions could be assigned (they are not assigned because they are already in cache)");
        }
    }

    private static boolean distributeActionsOk1() {
        Controller controller = Controller.getInstance();
        return controller.getInvokerById("1").getActions().get(0).getId().equals("add1") &&
                controller.getInvokerById("1").getActions().get(1).getId().equals("add2_0") &&
                controller.getInvokerById("1").getActions().get(2).getId().equals("add2_1") &&
                controller.getInvokerById("1").getActions().get(3).getId().equals("add2_2") &&
                controller.getInvokerById("2").getActions().get(0).getId().equals("f5")
                && controller.getInvokerById("1").getActions().size() == 4
                && controller.getInvokerById("2").getActions().size() == 1 &&
                controller.getInvokerById("1").getTotalMemory() == 200 &&
                controller.getInvokerById("2").getTotalMemory() == 700;
    }

    private static boolean executeActionsOk1() {
        Controller controller = Controller.getInstance();
        return controller.getActionById("add1").getResult().equals(new BigInteger("10")) &&
                controller.getActionById("add2_0").getResult().equals(new BigInteger("10")) &&
                controller.getActionById("add2_1").getResult().equals(new BigInteger("10")) &&
                controller.getActionById("add2_2").getResult().equals(new BigInteger("10")) &&
                controller.getActionById("f5").getResult().equals(new BigInteger("120")) &&
                controller.getInvokerById("1").getActions().size() == 0 &&
                controller.getInvokerById("2").getActions().size() == 0;
    }

    private static boolean distributeActionsOk2() {
        Controller controller = Controller.getInstance();
        return controller.getInvokerById("1").getActions().size() == 1 &&
                controller.getInvokerById("2").getActions().size() == 0 &&
                controller.getInvokerById("1").getActions().get(0).getId().equals("f5") &&
                controller.getInvokerById("1").getTotalMemory() == 1700 &&
                controller.getInvokerById("2").getTotalMemory() == 1500;
    }

    private static boolean distributeActionsOk3() {
        Controller controller = Controller.getInstance();
        return controller.getInvokerById("1").getActions().size() == 0
                && controller.getInvokerById("2").getActions().size() == 0 &&
                controller.getInvokerById("1").getTotalMemory() == 2500 &&
                controller.getInvokerById("2").getTotalMemory() == 1500;
    }
}
