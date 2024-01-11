package main.main;

import java.math.BigInteger;
import main.Controller;
import main.Invoker;
import main.interfaces.InterfaceAction;
import main.operations.Adder;
import main.operations.Multiplier;
import main.policy.*;
import main.reflection.ActionProxy;

public class MainReflection {
    public static void main(String[] args) {
        Controller controller = Controller.getInstance();
        controller.setPolicy(new UniformGroup());

        Invoker iv1 = new Invoker(2500, "1");
        Invoker iv2 = new Invoker(1500, "2");

        controller.addInvoker(iv1);
        controller.addInvoker(iv2);

        int[] values = { 1, 2, 3, 4 };
        int[] values2 = { 1, 2, 3 };
        Adder add = new Adder("add", 100, values);
        Multiplier mul = new Multiplier("mul", 100, values);
        Adder add2 = new Adder("add2", 100, values2);

        iv1.addObserver(controller);
        iv2.addObserver(controller);

        InterfaceAction mulDynamicProxy = ActionProxy.invoke(mul);
        InterfaceAction adderDynamicProxy = ActionProxy.invoke(add);
        InterfaceAction InterfaceActionAdd2 = add2;

        // Operation get executed from dynamicProxy using ActionProxy and while doing so
        // get added to the controller
        mulDynamicProxy.operation();
        adderDynamicProxy.operation();

        // Operation get executed directly from the interface due to the DymaicBinding
        // and doesn't get added to the controller
        add2.operation();
        InterfaceActionAdd2.operation();

        System.out.println("------------------");
        if (new BigInteger("24").equals(controller.getActionById("mul").getResult())) {
            System.out.println("mulDynamicProxy (DynamicProxy) executed OK. (Result is expected)");
        } else {
            System.out.println("mulDynamicProxy (DynamicProxy) executed NOT OK. (Result is NOT as expected)");
        }

        System.out.println("------------------");
        if (new BigInteger("10").equals(controller.getActionById("add").getResult())) {
            System.out.println("adderDynamicProxy (DynamicProxy) executed OK. (Result is expected)");
        } else {
            System.out.println("adderDynamicProxy (DynamicProxy) executed NOT OK. (Result is NOT as expected)");
        }

        System.out.println("------------------");
        if (controller.getActionById("add2") == null) {
            System.out.println("add2 is not in controller (as expected)");
        } else {
            System.out.println("add2 IS in controller (NOT as expected)");
        }
        System.out.println("------------------");
        if (new BigInteger("6").equals(add2.getResult())) {
            System.out.println("add2 result saved in add2 OK");
        } else {
            System.out.println("add2 result NOT saved in add2 (NOT as expected)");
        }
    }
}