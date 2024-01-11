package main.main;

import main.Controller;
import main.Invoker;
import main.operations.Adder;
import main.operations.Multiplier;
import main.policy.*;

import java.math.BigInteger;

public class MainPolicyGreedyGroup {
    public static void main(String[] args) {
        Controller controller = Controller.getInstance();
        controller.setPolicy(new GreedyGroup());

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
        System.out.println(controller.getInvokerById("1").toString());
        System.out.println(controller.getInvokerById("2").toString());
        System.out.println("----------------------");
        System.out.println("----------------------");
        /*
         * It will try to distribute as it follows:
         * Invoker 1: add1, add2_0, add2_1, add2_3 (all it fits) (f5 mem = 800, invoker
         * 1 mem left = 200)
         * Invoker 2: f5
         */
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

            controller.executeAssignedActions();
            if (executeActionsOk1()) {
                System.out.println("---------------- Actions executed as expected ----------------");
            } else {
                System.out.println("-------------- Actions executed NOT as expected ---------------");
            }
            System.out.println(controller.getInvokerById("1").toString());
            System.out.println(controller.getInvokerById("2").toString());

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

        Adder add3 = new Adder("add3", 100, values2);
        Multiplier mul4 = new Multiplier("mul4", 100, values);

        controller.addAction(add3);
        controller.addAction(mul4);
        /*
         * It will try to distribute as it follows:
         * Invoker 1: add1, add2_0, add2_1, add2_3, add3, mul4 (all it fits)
         * Invoker 2: f5
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
            if (executeActionsOk2()) {
                System.out.println("---------------- Actions executed as expected ----------------");
            } else {
                System.out.println("-------------- Actions executed NOT as expected ---------------");
            }
            System.out.println(controller.getInvokerById("1").toString());
            System.out.println(controller.getInvokerById("2").toString());
        } else {
            System.out.println("Not all actions could be assigned");
        }
    }

    private static boolean distributeActionsOk1() {
        Controller controller = Controller.getInstance();
        return controller.getInvokerById("1").getActions().get(0).getId().equals("add1") &&
                controller.getInvokerById("1").getActions().get(1).getId().equals("add2_0") &&
                controller.getInvokerById("1").getActions().get(2).getId().equals("add2_1") &&
                controller.getInvokerById("1").getActions().get(3).getId().equals("add2_2") &&
                controller.getInvokerById("2").getActions().get(0).getId().equals("mul5")
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
                controller.getActionById("mul5").getResult().equals(new BigInteger("25")) &&
                controller.getActionById("add3") == null &&
                controller.getActionById("mul4") == null &&
                controller.getInvokerById("1").getActions().size() == 0 &&
                controller.getInvokerById("2").getActions().size() == 0;
    }

    private static boolean distributeActionsOk2() {
        Controller controller = Controller.getInstance();
        return controller.getInvokerById("1").getActions().get(0).getId().equals("add1") &&
                controller.getInvokerById("1").getActions().get(1).getId().equals("add2_0") &&
                controller.getInvokerById("1").getActions().get(2).getId().equals("add2_1") &&
                controller.getInvokerById("1").getActions().get(3).getId().equals("add2_2") &&
                controller.getInvokerById("1").getActions().get(4).getId().equals("add3") &&
                controller.getInvokerById("1").getActions().get(5).getId().equals("mul4") &&
                controller.getInvokerById("2").getActions().get(0).getId().equals("mul5") &&
                controller.getInvokerById("1").getActions().size() == 6 &&
                controller.getInvokerById("2").getActions().size() == 1 &&
                controller.getInvokerById("1").getTotalMemory() == 0 &&
                controller.getInvokerById("2").getTotalMemory() == 700;
    }

    private static boolean executeActionsOk2() {
        Controller controller = Controller.getInstance();
        return controller.getActionById("add1").getResult().equals(new BigInteger("10")) &&
                controller.getActionById("add2_0").getResult().equals(new BigInteger("10")) &&
                controller.getActionById("add2_1").getResult().equals(new BigInteger("10")) &&
                controller.getActionById("add2_2").getResult().equals(new BigInteger("10")) &&
                controller.getActionById("mul5").getResult().equals(new BigInteger("25")) &&
                controller.getActionById("add3").getResult().equals(new BigInteger("10")) &&
                controller.getActionById("mul4").getResult().equals(new BigInteger("24")) &&
                controller.getInvokerById("1").getActions().size() == 0 &&
                controller.getInvokerById("2").getActions().size() == 0;
    }
}
