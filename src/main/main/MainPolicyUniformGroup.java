package main.main;

import main.Controller;
import main.Invoker;
import main.operations.Adder;
import main.operations.Multiplier;
import main.policy.*;

public class MainPolicyUniformGroup {
    public static void main(String[] args) {
        Controller controller = Controller.getInstance();
        controller.setPolicy(new UniformGroup());

        Invoker iv1 = new Invoker(2500, "1");
        Invoker iv2 = new Invoker(1500, "2");

        controller.addInvoker(iv1);
        controller.addInvoker(iv2);

        int[] values = { 1, 2, 3, 4 };
        int[] values2 = { 5, 5 };
        Adder add1 = new Adder("add1", 2000, values);
        Adder add2 = new Adder("add2", 100, values);
        Multiplier f5 = new Multiplier("mul5", 800, values2);

        iv1.addObserver(controller);
        iv2.addObserver(controller);
        controller.addAction(add1);
        controller.addAction(add2, 3); // action add2 gets added 3 times as: add2_0, add2_1, add2_2
        controller.addAction(f5);

        System.out.println("----------------------");
        System.out.println("----------------------");
        System.out.println("Invokers info before distributing actions: ");
        System.out.println(controller.getInvokers().get(0).toString());
        System.out.println(controller.getInvokers().get(1).toString());
        System.out.println("----------------------");
        System.out.println("----------------------");
        /*
         * It will try to distribute as it follows:
         * Tries to distribute equally:
         * Invoker 1: add1, add2_0, add2_1
         * Invoker 2: add2_2, f5
         */
        if (controller.distributeActions()) {
            if (distributeActionsOk1()) {
                System.out.println("---------------- Actions distributed as expected ----------------");
            } else {
                System.out.println("-------------- Actions distributed NOT as expected ---------------");
            }
            System.out.println("Invokers info after distributing actions: ");
            System.out.println(controller.getInvokers().get(0).toString());
            System.out.println(controller.getInvokers().get(1).toString());
            System.out.println("----------------------");
            System.out.println("----------------------");

            controller.executeAssignedActions();
            System.out.println("---------------- Actions executed ----------------");
            System.out.println("---------------- Actions executed ----------------");
            System.out.println(controller.getInvokers().get(0).toString());
            System.out.println(controller.getInvokers().get(1).toString());

        } else {
            System.out.println("Not all actions could be assigned");
        }

        System.out.println("----------------------");
        System.out.println("----------------------");
        System.out.println("Invokers info before distributing actions: ");
        System.out.println(controller.getInvokers().get(0).toString());
        System.out.println(controller.getInvokers().get(1).toString());
        System.out.println("----------------------");
        System.out.println("----------------------");

        Adder add3 = new Adder("add3", 100, values2);
        Multiplier mul4 = new Multiplier("mul4", 100, values);

        controller.addAction(add3);
        controller.addAction(mul4);
        /*
         * It will try to distribute as it follows:
         * Tries to distribute equally:
         * Invoker 1: add1, add2_0, add2_1, add3
         * Invoker 2: add2_2, f5, mul4
         */
        if (controller.distributeActions()) {
            if (distributeActionsOk2()) {
                System.out.println("---------------- Actions distributed as expected ----------------");
            } else {
                System.out.println("-------------- Actions distributed NOT as expected ---------------");
            }
            System.out.println("Invokers info after distributing actions: ");
            System.out.println(controller.getInvokers().get(0).toString());
            System.out.println(controller.getInvokers().get(1).toString());
            System.out.println("----------------------");
            System.out.println("----------------------");

            controller.executeAssignedActions();
            System.out.println("---------------- Actions executed ----------------");
            System.out.println("---------------- Actions executed ----------------");
            System.out.println(controller.getInvokers().get(0).toString());
            System.out.println(controller.getInvokers().get(1).toString());
        } else {
            System.out.println("Not all actions could be assigned");
        }
    }

    private static boolean distributeActionsOk1() {
        Controller controller = Controller.getInstance();
        return controller.getInvokers().get(0).getActions().get(0).getId().equals("add1") &&
                controller.getInvokers().get(0).getActions().get(1).getId().equals("add2_0") &&
                controller.getInvokers().get(0).getActions().get(2).getId().equals("add2_1") &&
                controller.getInvokers().get(1).getActions().get(0).getId().equals("add2_2") &&
                controller.getInvokers().get(1).getActions().get(1).getId().equals("mul5")
                && controller.getInvokers().get(0).getActions().size() == 3
                && controller.getInvokers().get(1).getActions().size() == 2 &&
                controller.getInvokers().get(0).getTotalMemory() == 300 &&
                controller.getInvokers().get(1).getTotalMemory() == 600;
    }

    private static boolean distributeActionsOk2() {
        Controller controller = Controller.getInstance();
        return controller.getInvokers().get(0).getActions().get(0).getId().equals("add1") &&
                controller.getInvokers().get(0).getActions().get(1).getId().equals("add2_0") &&
                controller.getInvokers().get(0).getActions().get(2).getId().equals("add2_1") &&
                controller.getInvokers().get(1).getActions().get(0).getId().equals("add2_2") &&
                controller.getInvokers().get(1).getActions().get(1).getId().equals("mul5") &&
                controller.getInvokers().get(1).getActions().get(2).getId().equals("mul4") &&
                controller.getInvokers().get(0).getActions().get(3).getId().equals("add3") &&
                controller.getInvokers().get(0).getActions().size() == 4 &&
                controller.getInvokers().get(1).getActions().size() == 3 &&
                controller.getInvokers().get(0).getTotalMemory() == 200 &&
                controller.getInvokers().get(1).getTotalMemory() == 500;
    }
}