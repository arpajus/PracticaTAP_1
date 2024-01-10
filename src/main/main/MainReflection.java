package main.main;

import main.Controller;
import main.Invoker;
import main.operations.Adder;
import main.operations.Multiplier;
import main.policy.*;

public class MainReflection {
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
        controller.addAction(add1);
        controller.addAction(add2, 3);
        controller.addAction(f5);
    }
}
