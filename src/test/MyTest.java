package test;

import main.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.Test;

public class MyTest {

    Controller controller = new Controller();
    RoundRobinImproved roundRobinImproved = new RoundRobinImproved();
    int[] values = { 1, 2, 3, 4 };

    @Test
    public void createInvokers() {

        controller.setPolicy(roundRobinImproved);

        Invoker iv1 = new Invoker(1000);
        Invoker iv2 = new Invoker(2000);

        controller.addInvoker(iv1);
        controller.addInvoker(iv2);

        assertEquals(iv1.getTotalMemory(), 1000);
        assertEquals(iv2.getTotalMemory(), 2000);

        assertEquals(controller.getInvokers().contains(iv1), true);
        assertEquals(controller.getInvokers().contains(iv2), true);

        assertFalse(controller.getInvokers().isEmpty());
    }

    @Test
    public void addRegularActions() {
        Adder add1 = new Adder("add1", 1500, values);
        Multiplier mltplr1 = new Multiplier("mltplr1", 200, values);

        controller.addAction(add1);
        controller.addAction(mltplr1);

        assertEquals(controller.getActions().contains(add1), true);
        assertEquals(controller.getActions().contains(mltplr1), true);
    }

    @Test
    public void addInsufficientMemoryExceptionActions() {
        // This action is a test to invoke InsufficientMemoryException
        Adder add2 = new Adder("add2", 500, values);
        Adder add3 = new Adder("add3", 200, values);
        Adder add4 = new Adder("add4", 200, values);
        Adder add5 = new Adder("add5", 200, values);

        controller.addAction(add2);
        controller.addAction(add3);
        controller.addAction(add4);
        controller.addAction(add5);

        assertFalse(controller.getActions().isEmpty());

        assertEquals(controller.getActions().contains(add2), true);
        assertEquals(controller.getActions().contains(add3), true);
        assertEquals(controller.getActions().contains(add4), true);
        assertEquals(controller.getActions().contains(add5), true);
    }

    @Test
    public void distributeActionsTest() {
        assertEquals(controller.distributeActions(), true);
    }

    @Test
    public void executeAssignedActionsTest() {
        assertFalse(controller.getInvokers().isEmpty());

        assertEquals(controller.getInvokers().get(0).getTotalMemory(), 200);
        assertEquals(controller.getInvokers().get(1).getTotalMemory(), 0);

        controller.executeAssignedActions();

        assertEquals(controller.getInvokers().get(0).getTotalMemory(), 1000);
        assertEquals(controller.getInvokers().get(1).getTotalMemory(), 1000);
    }

    @Test
    public void executeAssignedActionsResultsTest() {
        // This shows the results of the actions that have been done
        List<Action> results = controller.getActions();
        for (Action action : results) {
            if (action.getResult() != 0) {
                switch (action.getId()) {
                    case "add1":
                        assertEquals(action.getResult(), 10);
                        break;
                    case "add2":
                        assertEquals(action.getResult(), 10);
                        break;
                    case "add3":
                        assertEquals(action.getResult(), 10);
                        break;
                    case "add4":
                        assertEquals(action.getResult(), 10);
                        break;
                    case "add5":
                        assertEquals(action.getResult(), 10);
                        break;
                    case "mltplr1":
                        assertEquals(action.getResult(), 24);
                        break;
                    default:
                        // error, this case should never be reached
                        assertEquals(true, false);
                        break;
                }
            }
        }
    }
}