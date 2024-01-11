package main.main;

import main.Controller;
import main.Invoker;
import main.decorator.InvokerChronometerDecorator;
import main.operations.Adder;
import main.operations.Factorial;
import java.util.Scanner;

import main.policy.*;

public class MainChronoDecorator {
    public static void main(String[] args) {
        Controller controller = Controller.getInstance();
        controller.setPolicy(new GreedyGroup());

        Invoker iv1;
        Invoker iv2;

        // Choose mode
        System.out.println("\n\n[0] - DO NOT use chronometer decorator");
        System.out.println("[Any Key] - Use chronometer decorator");
        System.out.print("\nChoice: ");

        Scanner sc = new Scanner(System.in);
        char input = sc.next().charAt(0);
        if (input == '0') {
            iv1 = new Invoker(2500, "1");
            iv2 = new Invoker(1500, "2");
        } else {
            iv1 = new InvokerChronometerDecorator(new Invoker(2500, "1"));
            iv2 = new InvokerChronometerDecorator(new Invoker(1500, "2"));
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
        controller.addAction(add2, 3);
        controller.addAction(f5);

        System.out.println("----------------------");
        System.out.println("----------------------");
        System.out.println("Invokers info before distributing actions: ");
        System.out.println(controller.getInvokerById("1").toString());
        System.out.println(controller.getInvokerById("2").toString());
        System.out.println("----------------------");
        System.out.println("----------------------");

        if (controller.distributeActions()) {
            System.out.println("----------------------");
            System.out.println("Invokers info after distributing actions: ");
            System.out.println(controller.getInvokerById("1").toString());
            System.out.println(controller.getInvokerById("2").toString());
            controller.executeAssignedActions();
            System.out.println("----------------------");
            System.out.println("Executed actions: ");
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

        if (controller.distributeActions()) {
            System.out.println("----------------------");
            System.out.println("Invokers info after distributing actions: ");
            System.out.println(controller.getInvokerById("1").toString());
            System.out.println(controller.getInvokerById("2").toString());
            System.out.println("----------------------");
            System.out.println("----------------------");
            controller.executeAssignedActions();
            System.out.println("----------------------");
            System.out.println("Executed actions: ");
            System.out.println(controller.getInvokerById("1").toString());
            System.out.println(controller.getInvokerById("2").toString());
        } else {
            System.out.println("Not all actions could be assigned");
        }

        Adder add3 = new Adder("add3", 100, values);
        Adder add4 = new Adder("add4", 100, values);
        Factorial f1 = new Factorial("f1", 100, values2);
        Factorial f2 = new Factorial("f2", 100, values2);

        controller.addAction(add3);
        controller.addAction(add4);
        controller.addAction(f1);
        controller.addAction(f2);

        System.out.println("----------------------");
        System.out.println("----------------------");
        System.out.println("Invokers info before distributing actions: ");
        System.out.println(controller.getInvokerById("1").toString());
        System.out.println(controller.getInvokerById("2").toString());
        System.out.println("----------------------");
        System.out.println("----------------------");

        if (controller.distributeActions()) {
            System.out.println("----------------------");
            System.out.println("Invokers info after distributing actions: ");
            System.out.println(controller.getInvokerById("1").toString());
            System.out.println(controller.getInvokerById("2").toString());
            System.out.println("----------------------");
            System.out.println("----------------------");
            controller.executeAssignedActions();
            System.out.println("----------------------");
            System.out.println("Executed actions: ");
            System.out.println(controller.getInvokerById("1").toString());
            System.out.println(controller.getInvokerById("2").toString());
        } else {
            System.out.println(
                    "Not all actions could be assigned (they are not assigned because they are already in cache)");
        }
    }
}