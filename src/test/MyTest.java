package test;

import main.*;
import main.decorator.InvokerCacheDecorator;
import main.decorator.InvokerChronometerDecorator;
import main.exceptions.InsufficientMemoryException;
import main.interfaces.InterfaceAction;
import main.operations.Adder;
import main.operations.Factorial;
import main.operations.Multiplier;
import main.policy.*;
import main.reflection.ActionProxy;
import main.reflection.DynamicProxy;
import java.lang.reflect.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class MyTest {

    Controller controller = Controller.getInstance();
    RoundRobinImproved roundRobinImproved = new RoundRobinImproved();
    int[] values = { 1, 2, 3, 4 };

    @Test
    public void checkOneController() {
        assertEquals(controller.getId(), 1);
        Controller controller2 = Controller.getInstance();
        assertEquals(controller2.getId(), 1);
    }

    @Test
    public void createInvokers() {
        Controller.resetInstance();
        controller = Controller.getInstance();
        controller.setPolicy(roundRobinImproved);

        Invoker iv1 = new Invoker(1000, "1");
        Invoker iv2 = new Invoker(2000, "2");

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
        Controller.resetInstance();
        controller = Controller.getInstance();
        Adder add1 = new Adder("add1", 1500, values);
        Multiplier mltplr1 = new Multiplier("mltplr1", 200, values);

        controller.addAction(add1);
        controller.addAction(mltplr1);

        assertEquals(controller.getActions().contains(add1), true);
        assertEquals(controller.getActions().contains(mltplr1), true);
    }

    @Test
    public void addMoreActions() {
        Controller.resetInstance();
        controller = Controller.getInstance();
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
    public void addInsufficientMemoryActions() {
        Controller.resetInstance();
        controller = Controller.getInstance();
        // Configure System.out
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Invoker iv1 = new Invoker(1000, "1");
        Invoker iv2 = new Invoker(2000, "2");

        controller.addInvoker(iv1);
        controller.addInvoker(iv2);

        Adder add1 = new Adder("add1", 1500, values);

        controller.addAction(add1);
        controller.distributeActions();

        assertEquals(controller.distributeActions(), false);

        // Reset System.out
        System.setOut(new PrintStream(System.out));

        assertTrue(outContent.toString().contains("Not enough memory to take the action"));
    }

    @Test
    public void addActionsandLastInsufficientMemoryActions() {
        Controller.resetInstance();
        controller = Controller.getInstance();
        // configure system.out
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Invoker iv1 = new Invoker(1000, "1");
        Invoker iv2 = new Invoker(2000, "2");

        controller.addInvoker(iv1);
        controller.addInvoker(iv2);
        Adder add1 = new Adder("add1", 100, values);
        controller.addAction(add1);
        Adder add2 = new Adder("add2", 100, values);
        controller.addAction(add2);
        Adder add3 = new Adder("add3", 100, values);
        controller.addAction(add3);
        Adder add4 = new Adder("add4", 2000, values);
        controller.addAction(add4);

        boolean actionsAssigned = controller.distributeActions();
        assertFalse(actionsAssigned);

        System.setOut(new PrintStream(System.out));

        assertTrue(outContent.toString().contains("Not enough memory to take the action"));
    }

    @Test
    public void distributeActionsTestRoundRobin() {
        Controller.resetInstance();
        controller = Controller.getInstance();
        Invoker iv1 = new Invoker(1000, "1");
        Invoker iv2 = new Invoker(2000, "2");

        controller.addInvoker(iv1);
        controller.addInvoker(iv2);
        Adder add1 = new Adder("add1", 100, values);
        controller.addAction(add1);
        Adder add2 = new Adder("add2", 100, values);
        controller.addAction(add2);
        Adder add3 = new Adder("add3", 100, values);
        controller.addAction(add3);
        Adder add4 = new Adder("add4", 1000, values);
        controller.addAction(add4);
        assertTrue(controller.distributeActions());
    }

    @Test
    public void distributeActionsTestRoundRobinImproved() {
        Controller.resetInstance();
        controller = Controller.getInstance();
        RoundRobinImproved roundRobinImproved = new RoundRobinImproved();
        controller.setPolicy(roundRobinImproved);
        Invoker iv1 = new Invoker(1000, "1");
        Invoker iv2 = new Invoker(2000, "2");

        controller.addInvoker(iv1);
        controller.addInvoker(iv2);
        Adder add1 = new Adder("add1", 100, values);
        controller.addAction(add1);
        Adder add2 = new Adder("add2", 100, values);
        controller.addAction(add2);
        Adder add3 = new Adder("add3", 100, values);
        controller.addAction(add3);
        Adder add4 = new Adder("add4", 1000, values);
        controller.addAction(add4);
        assertTrue(controller.distributeActions());
    }

    @Test
    public void distributeActionsInsufficientMemoryRoundRobinImproved() {
        Controller.resetInstance();
        controller = Controller.getInstance();
        RoundRobinImproved roundRobinImproved = new RoundRobinImproved();
        controller.setPolicy(roundRobinImproved);
        Invoker iv1 = new Invoker(1000, "1");
        Invoker iv2 = new Invoker(2000, "2");

        controller.addInvoker(iv1);
        controller.addInvoker(iv2);
        Adder add1 = new Adder("add1", 2500, values);
        controller.addAction(add1);
        assertFalse(controller.distributeActions());
    }

    @Test
    public void distributeActionsComparasionRoundRobinAndRoundRobinImproved() {
        Controller.resetInstance();
        controller = Controller.getInstance();
        Invoker iv1 = new Invoker(1000, "1");
        Invoker iv2 = new Invoker(2000, "2");

        controller.addInvoker(iv1);
        controller.addInvoker(iv2);
        Adder add1 = new Adder("add1", 1500, values);
        controller.addAction(add1);
        Adder add2 = new Adder("add2", 100, values);
        controller.addAction(add2);
        Adder add3 = new Adder("add3", 100, values);
        controller.addAction(add3);
        Adder add4 = new Adder("add4", 100, values);
        controller.addAction(add4);
        assertFalse(controller.distributeActions());
        controller.setPolicy(new RoundRobinImproved());
        assertTrue(controller.distributeActions());
    }

    @Test
    public void lifeCycleMemoryTest() {
        Controller.resetInstance();
        controller = Controller.getInstance();
        Invoker iv1 = new Invoker(1000, "1");
        controller.addInvoker(iv1);
        Invoker iv2 = new Invoker(2000, "2");
        controller.addInvoker(iv2);
        Adder add1 = new Adder("add1", 100, values);
        controller.addAction(add1);
        Adder add2 = new Adder("add2", 100, values);
        controller.addAction(add2);
        Adder add3 = new Adder("add3", 100, values);
        controller.addAction(add3);
        Adder add4 = new Adder("add4", 100, values);
        controller.addAction(add4);

        assertEquals(iv1.getTotalMemory(), 1000);
        assertEquals(iv2.getTotalMemory(), 2000);
        controller.distributeActions();
        assertEquals(iv1.getTotalMemory(), 800);
        assertEquals(iv2.getTotalMemory(), 1800);

        controller.executeAssignedActions();

        assertEquals(iv1.getTotalMemory(), 1000);
        assertEquals(iv2.getTotalMemory(), 2000);
    }

    @Test
    public void lifeCylceMemoryRoundRobinImproved() {
        Controller.resetInstance();
        controller = Controller.getInstance();
        Invoker iv1 = new Invoker(1000, "1");
        controller.addInvoker(iv1);
        Invoker iv2 = new Invoker(2000, "2");
        controller.addInvoker(iv2);
        Adder add1 = new Adder("add1", 2000, values);
        controller.addAction(add1);
        Adder add2 = new Adder("add2", 100, values);
        controller.addAction(add2);
        Adder add3 = new Adder("add3", 100, values);
        controller.addAction(add3);
        Adder add4 = new Adder("add4", 100, values);
        controller.addAction(add4);

        assertEquals(iv1.getTotalMemory(), 1000);
        assertEquals(iv2.getTotalMemory(), 2000);
        controller.setPolicy(new RoundRobinImproved());
        controller.distributeActions();
        assertEquals(iv1.getTotalMemory(), 700);
        assertEquals(iv2.getTotalMemory(), 0);

        controller.executeAssignedActions();

        assertEquals(iv1.getTotalMemory(), 1000);
        assertEquals(iv2.getTotalMemory(), 2000);
    }

    @Test
    public void executeAssignedActionsResultsTest() {
        Controller.resetInstance();
        controller = Controller.getInstance();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        // This shows the results of the actions that have been done
        Invoker iv1 = new Invoker(1000, "1");
        controller.addInvoker(iv1);
        Invoker iv2 = new Invoker(2000, "2");
        controller.addInvoker(iv2);
        Adder add1 = new Adder("add1", 100, values);
        controller.addAction(add1);
        Multiplier mltplr1 = new Multiplier("mltplr1", 100, values);
        controller.addAction(mltplr1);

        Adder add2 = new Adder("add2", 100, values);
        controller.addAction(add2);
        Adder add3 = new Adder("add3", 100, values);
        controller.addAction(add3);
        Adder add4 = new Adder("add4", 100, values);
        controller.addAction(add4);
        Adder add5 = new Adder("add5", 100, values);
        controller.addAction(add5);

        controller.distributeActions();
        controller.executeAssignedActions();

        System.setOut(new PrintStream(System.out));

        List<Action> results = controller.getActions();
        BigInteger[] expectedResults = new BigInteger[controller.getActions().size()];
        int i = 0;
        for (Action action : results) {
            if (action.getResult() != BigInteger.ZERO) {
                expectedResults[i] = action.getResult();
                i++;
            }
        }
        assertArrayEquals(expectedResults, new BigInteger[] { BigInteger.valueOf(10), BigInteger.valueOf(24),
                BigInteger.valueOf(10), BigInteger.valueOf(10), BigInteger.valueOf(10), BigInteger.valueOf(10) });
    }

    @Test
    public void GreedyGroupTest() {
        Controller.resetInstance();
        controller = Controller.getInstance();

        Invoker iv1Reuglar = new Invoker(3000, "1");
        Invoker iv2Reuglar = new Invoker(1000, "2");

        // InvokerDecorator iv1 = new InvokerCacheDecorator(iv1Reuglar);
        // InvokerDecorator iv2 = new InvokerCacheDecorator(iv2Reuglar);
        Invoker iv1 = new InvokerCacheDecorator(iv1Reuglar);
        Invoker iv2 = new InvokerCacheDecorator(iv2Reuglar);

        controller.addInvoker(iv1);
        controller.addInvoker(iv2);

        Adder add1 = new Adder("add1", 2000, values);
        Adder add2 = new Adder("add2", 100, values);
        Adder add5 = new Adder("add5", 800, values);

        controller.setPolicy(new GreedyGroup());
        controller.addAction(add1);
        controller.addAction(add2, 3);
        controller.addAction(add5);

        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);

        assertTrue(controller.distributeActions());

        assertEquals(4, iv1.getActions().size());
        assertEquals(1, iv2.getActions().size());
        assertEquals(iv1.getTotalMemory(), 700);
        assertEquals(iv2.getTotalMemory(), 200);

        controller.executeAssignedActions();

        assertEquals(0, iv1.getActions().size());
        assertEquals(0, iv2.getActions().size());
        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);

        controller.getActions().clear();
        Adder add6 = new Adder("add6", 3100, values);

        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);

        controller.addAction(add6);

        assertFalse(controller.distributeActions());

        // testing it doesn't try to execute an unassigned action
        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);
        assertEquals(0, iv1.getActions().size());
        assertEquals(0, iv2.getActions().size());

        controller.executeAssignedActions();

        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);
        assertEquals(0, iv1.getActions().size());
        assertEquals(0, iv2.getActions().size());

        // Test 3______________________________
        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);

        controller.getActions().clear();
        controller.addAction(add1, 2);

        assertTrue(controller.distributeActions());

        // result already in cache
        assertFalse(iv1.getCache() == null);
        assertFalse(iv1.getCache().get(add1.getId()) == null);

        assertEquals(1, iv1.getActions().size());
        assertEquals(0, iv2.getActions().size());
        assertEquals(iv1.getTotalMemory(), 1000);
        assertEquals(iv2.getTotalMemory(), 1000);

        controller.executeAssignedActions();

        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);
        assertEquals(0, iv1.getActions().size());
        assertEquals(0, iv2.getActions().size());

        Controller.resetInstance();
        controller = Controller.getInstance();

        iv1Reuglar = new Invoker(3000, "1");
        iv2Reuglar = new Invoker(1000, "2");

        iv1 = new InvokerCacheDecorator(iv1Reuglar);
        iv2 = new InvokerCacheDecorator(iv2Reuglar);

        controller.addInvoker(iv1);
        controller.addInvoker(iv2);
        add1 = new Adder("add1", 2000, values);
        add2 = new Adder("add2", 100, values);
        add5 = new Adder("add5", 800, values);
        controller.setPolicy(new GreedyGroup());

        controller.addAction(add1);
        controller.addAction(add2, 3);
        controller.addAction(add5);

        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);

        assertTrue(controller.distributeActions());

        assertEquals(iv1.getTotalMemory(), 700);
        assertEquals(iv2.getTotalMemory(), 200);
        assertEquals(4, iv1.getActions().size());
        assertEquals(1, iv2.getActions().size());

        controller.executeAssignedActions();

        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);
        assertEquals(0, iv1.getActions().size());
        assertEquals(0, iv2.getActions().size());

        add6 = new Adder("add6", 3100, values);

        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);

        controller.getActions().clear();
        controller.addAction(add6);

        assertFalse(controller.distributeActions());

        // testing it doesn't try to execute an unassigned action
        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);
        assertEquals(0, iv1.getActions().size());
        assertEquals(0, iv2.getActions().size());

        controller.executeAssignedActions();

        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);
        assertEquals(0, iv1.getActions().size());
        assertEquals(0, iv2.getActions().size());

        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);

        controller.getActions().clear();
        controller.addAction(add1, 2);

        assertTrue(controller.distributeActions());

        // result already in cache
        assertEquals(iv1.getTotalMemory(), 1000);
        assertEquals(iv2.getTotalMemory(), 1000);
        assertEquals(1, iv1.getActions().size());
        assertEquals(0, iv2.getActions().size());

        controller.executeAssignedActions();

        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);
        assertEquals(0, iv1.getActions().size());
        assertEquals(0, iv2.getActions().size());

        Controller.resetInstance();
        controller = Controller.getInstance();

        iv1Reuglar = new Invoker(90, "1");
        iv2Reuglar = new Invoker(10, "2");

        iv1 = new InvokerCacheDecorator(iv1Reuglar);
        iv2 = new InvokerCacheDecorator(iv2Reuglar);
        controller.addInvoker(iv1);
        controller.addInvoker(iv2);
        add1 = new Adder("add1", 2000, values);
        add2 = new Adder("add2", 100, values);
        add5 = new Adder("add5", 800, values);
        controller.setPolicy(new GreedyGroup());

        controller.addAction(add1);
        controller.addAction(add2, 3);
        controller.addAction(add5);

        assertEquals(iv1.getTotalMemory(), 90);
        assertEquals(iv2.getTotalMemory(), 10);

        assertFalse(controller.distributeActions());

        assertEquals(0, iv1.getActions().size());
        assertEquals(0, iv2.getActions().size());

        assertEquals(iv1.getTotalMemory(), 90);
        assertEquals(iv2.getTotalMemory(), 10);

        controller.executeAssignedActions();

        assertEquals(0, iv1.getActions().size());
        assertEquals(0, iv2.getActions().size());

        assertEquals(iv1.getTotalMemory(), 90);
        assertEquals(iv2.getTotalMemory(), 10);

        Adder add3 = new Adder("add3", 3, values);
        controller.addAction(add3, 35); // 105

        assertEquals(iv1.getTotalMemory(), 90);
        assertEquals(iv2.getTotalMemory(), 10);

        assertTrue(controller.distributeActions());

        assertEquals(30, iv1.getActions().size());
        assertEquals(3, iv2.getActions().size());
        assertEquals(iv1.getTotalMemory(), 0);
        assertEquals(iv2.getTotalMemory(), 1);

        controller.executeAssignedActions();

        assertEquals(0, iv1.getActions().size());
        assertEquals(0, iv2.getActions().size());
        assertEquals(iv1.getTotalMemory(), 90);
        assertEquals(iv2.getTotalMemory(), 10);

        Controller.resetInstance();
        controller = Controller.getInstance();

        Invoker iv3Reuglar = new Invoker(1500, "3");
        Invoker iv4Reuglar = new Invoker(2000, "4");

        InvokerCacheDecorator iv3 = new InvokerCacheDecorator(iv3Reuglar);
        InvokerCacheDecorator iv4 = new InvokerCacheDecorator(iv4Reuglar);

        controller.addInvoker(iv3);
        controller.addInvoker(iv4);

        add3 = new Adder("add3", 1200, values);
        Adder add4 = new Adder("add4", 500, values);
        Adder add7 = new Adder("add7", 600, values);

        controller.setPolicy(new GreedyGroup());

        controller.addAction(add3);
        controller.addAction(add4, 2);
        controller.addAction(add7);

        assertEquals(iv3.getTotalMemory(), 1500);
        assertEquals(iv4.getTotalMemory(), 2000);

        assertTrue(controller.distributeActions());

        assertEquals(1, iv3.getActions().size());
        assertEquals(3, iv4.getActions().size());
        assertEquals(iv3.getTotalMemory(), 300);
        assertEquals(iv4.getTotalMemory(), 400);

        controller.executeAssignedActions();

        assertEquals(0, iv3.getActions().size());
        assertEquals(0, iv4.getActions().size());
        assertEquals(iv3.getTotalMemory(), 1500);
        assertEquals(iv4.getTotalMemory(), 2000);

        values = new int[] { 1, 2 };
        Adder add8 = new Adder("add8", 1800, values);

        assertEquals(iv3.getTotalMemory(), 1500);
        assertEquals(iv4.getTotalMemory(), 2000);

        controller.getActions().clear();
        controller.addAction(add8);

        assertTrue(controller.distributeActions());

        assertEquals(iv3.getTotalMemory(), 1500);
        assertEquals(iv4.getTotalMemory(), 200);
        assertEquals(0, iv3.getActions().size());
        assertEquals(1, iv4.getActions().size());

        controller.executeAssignedActions();

        assertEquals(iv3.getTotalMemory(), 1500);
        assertEquals(iv4.getTotalMemory(), 2000);
        assertEquals(0, iv3.getActions().size());
        assertEquals(0, iv4.getActions().size());

        assertEquals(iv3.getTotalMemory(), 1500);
        assertEquals(iv4.getTotalMemory(), 2000);

        controller.getActions().clear();
        controller.addAction(add3, 3);

        assertTrue(controller.distributeActions());

        // result already in cache
        assertEquals(iv3.getTotalMemory(), 300);
        assertEquals(iv4.getTotalMemory(), 800);
        assertEquals(1, iv3.getActions().size());
        assertEquals(1, iv4.getActions().size());

        controller.executeAssignedActions();

        assertEquals(iv3.getTotalMemory(), 1500);
        assertEquals(iv4.getTotalMemory(), 2000);
        assertEquals(0, iv3.getActions().size());
        assertEquals(0, iv4.getActions().size());
    }

    @Test
    public void GreedyGroupTest2() {
        Controller.resetInstance();
        controller = Controller.getInstance();

        Invoker iv1 = new Invoker(3000, "1");
        Invoker iv2 = new Invoker(1000, "2");
        controller.addInvoker(iv1);
        controller.addInvoker(iv2);
        Adder add1 = new Adder("add1", 2000, values);
        Adder add2 = new Adder("add2", 100, values);
        Adder add5 = new Adder("add5", 800, values);

        controller.setPolicy(new GreedyGroup());
        controller.addAction(add1);
        controller.addAction(add2, 3);
        controller.addAction(add5);

        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);

        assertTrue(controller.distributeActions());

        assertEquals(4, iv1.getActions().size());
        assertEquals(1, iv2.getActions().size());
        assertEquals(iv1.getTotalMemory(), 700);
        assertEquals(iv2.getTotalMemory(), 200);

        controller.executeAssignedActions();

        assertEquals(0, iv1.getActions().size());
        assertEquals(0, iv2.getActions().size());
        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);
    }

    @Test
    public void testUniformGroupDistribution() throws InsufficientMemoryException {
        Controller.resetInstance();
        controller = Controller.getInstance();
        // 3 actions 2 invokers
        UniformGroup uniformGroup = new UniformGroup(); // -> 2 actions 1 invoker
        controller.setPolicy(uniformGroup); // -> 1 action 1 invoker

        Invoker iv1 = new Invoker(1000, "1");
        controller.addInvoker(iv1);
        Invoker iv2 = new Invoker(2000, "2");
        controller.addInvoker(iv2);

        Adder add1 = new Adder("add1", 100, values);
        Adder add2 = new Adder("add2", 200, values);
        Adder add3 = new Adder("add3", 300, values);

        controller.addAction(add1);
        controller.addAction(add2);
        controller.addAction(add3);

        assertTrue(controller.distributeActions());
        assertEquals(2, iv1.getActions().size());
        assertEquals(1, iv2.getActions().size());
    }

    @Test
    public void testUniformGroupInsufficientMemory() {
        Controller.resetInstance();
        controller = Controller.getInstance();
        UniformGroup uniformGroup = new UniformGroup();
        controller.setPolicy(uniformGroup);

        Invoker iv1 = new Invoker(1000, "1");
        controller.addInvoker(iv1);
        Invoker iv2 = new Invoker(2000, "2");
        controller.addInvoker(iv2);

        Adder add1 = new Adder("add1", 1500, values);
        controller.addAction(add1);

        assertFalse(controller.distributeActions());
    }

    @Test
    public void UniformGroupTestFail() {
        Controller.resetInstance();
        controller = Controller.getInstance();
        UniformGroup uniformGroup = new UniformGroup();
        controller.setPolicy(uniformGroup);
        Invoker iv1 = new Invoker(1000, "1");
        controller.addInvoker(iv1);
        Invoker iv2 = new Invoker(2000, "2");
        controller.addInvoker(iv2);

        Adder add1 = new Adder("add1", 100, values);
        Adder add2 = new Adder("add2", 100, values);
        Adder add3 = new Adder("add3", 900, values);
        Adder add4 = new Adder("add4", 100, values);
        Adder add5 = new Adder("add5", 100, values);

        controller.addAction(add1);
        controller.addAction(add2);
        controller.addAction(add3);
        controller.addAction(add4);
        controller.addAction(add5);

        assertFalse(controller.distributeActions());
    }

    @Test
    public void testUniformGroupWithMultipleInvokersAndActions() throws InsufficientMemoryException {
        Controller.resetInstance();
        controller = Controller.getInstance();
        UniformGroup uniformGroup = new UniformGroup();
        controller.setPolicy(uniformGroup);

        Invoker iv1 = new Invoker(1000, "1");
        controller.addInvoker(iv1);
        Invoker iv2 = new Invoker(1500, "2");
        controller.addInvoker(iv2);
        Invoker iv3 = new Invoker(2000, "3");
        controller.addInvoker(iv3);

        for (int i = 1; i <= 7; i++) {
            Action action = new Adder("add" + i, 100 * i, values);
            controller.addAction(action);
        }
        assertTrue(controller.distributeActions());
        assertEquals(3, iv1.getActions().size());
        assertEquals(2, iv2.getActions().size());
        assertEquals(2, iv3.getActions().size());
        assertEquals(400, iv1.getTotalMemory()); // 1000-100-200-300=400
        assertEquals(600, iv2.getTotalMemory()); // 1500-400-500=600
        assertEquals(700, iv3.getTotalMemory()); // 2000-600-700=700
    }

    @Test
    public void BigGroupTest() {
        Controller.resetInstance();
        controller = Controller.getInstance();
        BigGroup bigGroup = new BigGroup(12);
        controller.setPolicy(bigGroup);

        Invoker iv1 = new Invoker(2000, "1");
        Invoker iv2 = new Invoker(5000, "2");

        controller.addInvoker(iv1);
        controller.addInvoker(iv2);

        Action action = new Adder("add", 100, values);
        controller.addAction(action, 18);

        assertEquals(iv1.getTotalMemory(), 2000);
        assertEquals(iv2.getTotalMemory(), 5000);

        assertTrue(controller.distributeActions());

        assertEquals(iv1.getTotalMemory(), 200);
        assertEquals(iv2.getTotalMemory(), 5000);

        assertEquals(18, iv1.getActions().size());
        assertEquals(0, iv2.getActions().size());
    }

    @Test
    public void BigGroup3Invokers() {
        Controller.resetInstance();
        controller = Controller.getInstance();
        BigGroup bigGroup = new BigGroup(7);
        controller.setPolicy(bigGroup);

        Invoker iv1 = new Invoker(1500, "1");
        Invoker iv2 = new Invoker(1000, "2");
        Invoker iv3 = new Invoker(3000, "3");

        controller.addInvoker(iv1);
        controller.addInvoker(iv2);
        controller.addInvoker(iv3);

        Action action = new Adder("add", 100, values);
        controller.addAction(action, 18);

        assertEquals(iv1.getTotalMemory(), 1500);
        assertEquals(iv2.getTotalMemory(), 1000);
        assertEquals(iv3.getTotalMemory(), 3000);

        assertTrue(controller.distributeActions());

        assertEquals(iv1.getTotalMemory(), 100);
        assertEquals(iv2.getTotalMemory(), 600);
        assertEquals(iv3.getTotalMemory(), 3000);

        assertEquals(14, iv1.getActions().size()); // 700+700
        assertEquals(4, iv2.getActions().size()); // 400
        assertEquals(0, iv3.getActions().size());
    }

    @Test
    public void BigGroupTest1() {
        Controller.resetInstance();
        controller = Controller.getInstance();
        BigGroup bigGroup = new BigGroup(4);
        controller.setPolicy(bigGroup);

        Invoker iv1 = new Invoker(1400, "1");
        Invoker iv2 = new Invoker(2000, "2");
        controller.addInvoker(iv1);
        controller.addInvoker(iv2);

        Action action0 = new Adder("action0", 250, values);
        Action action1 = new Adder("action1", 250, values);
        Action action2 = new Adder("action2", 250, values);
        Action action3 = new Adder("action3", 250, values);

        Action action4 = new Adder("action4", 100, values);
        Action action5 = new Adder("action5", 200, values);
        Action action6 = new Adder("action6", 100, values);
        Action action7 = new Adder("action7", 200, values);

        Action action8 = new Adder("action8", 100, values);
        Action action9 = new Adder("action9", 100, values);

        controller.addAction(action0);
        controller.addAction(action1);
        controller.addAction(action2);
        controller.addAction(action3); // g1 = 1000

        controller.addAction(action4);
        controller.addAction(action5);
        controller.addAction(action6);
        controller.addAction(action7); // g2 = 600

        controller.addAction(action8);
        controller.addAction(action9); // g3 = 200

        assertEquals(iv1.getTotalMemory(), 1400);
        assertEquals(iv2.getTotalMemory(), 2000);

        assertTrue(controller.distributeActions());

        assertEquals(iv1.getTotalMemory(), 200); // g1 + g3
        assertEquals(iv2.getTotalMemory(), 1400); // g2
        assertEquals(6, iv1.getActions().size());
        assertEquals(4, iv2.getActions().size());

    }

    @Test
    public void BigGroupFail() {
        Controller.resetInstance();
        controller = Controller.getInstance();
        BigGroup bigGroup = new BigGroup(12);
        controller.setPolicy(bigGroup);

        Invoker iv1 = new Invoker(0, "1");
        Invoker iv2 = new Invoker(0, "2");
        controller.addInvoker(iv1);
        controller.addInvoker(iv2);

        Adder add1 = new Adder("add1", 1000, values);
        controller.addAction(add1, 12);

        assertFalse(controller.distributeActions());
        assertEquals(0, iv1.getActions().size());
        assertEquals(0, iv2.getActions().size());
    }

    @Test
    public void BigGroupAsGreedy() {
    }

    @Test
    public void testResult() {
        Controller.resetInstance();
        controller = Controller.getInstance();

        Invoker iv1Regular = new Invoker(1000, "invoker");
        Invoker iv1 = new InvokerCacheDecorator(iv1Regular);
        controller.addInvoker(iv1, 2);

        // check observer
        ArrayList<Invoker> invokers = controller.getInvokers();
        for (Invoker invoker : invokers) {
            assertTrue(invoker.getObservers().contains(controller));
        }

        Adder add1 = new Adder("add1", 100, values);
        Multiplier mltplr1 = new Multiplier("mltplr1", 100, values);

        BigGroup bigGroup = new BigGroup(1);
        controller.setPolicy(bigGroup);

        controller.addAction(add1);
        controller.addAction(mltplr1);

        assertEquals(controller.getInvokerById("invoker_0").getTotalMemory(), 1000);
        assertEquals(controller.getInvokerById("invoker_1").getTotalMemory(), 1000);

        assertTrue(controller.distributeActions());

        assertEquals(controller.getInvokerById("invoker_0").getTotalMemory(), 800);
        assertEquals(controller.getInvokerById("invoker_1").getTotalMemory(), 1000);
        assertEquals(2, controller.getInvokerById("invoker_0").getActions().size());
        assertEquals(0, controller.getInvokerById("invoker_1").getActions().size());

        controller.executeAssignedActions();

        assertTrue(controller.getMetrics().get(0).getActionId().equals("add1"));
        assertTrue(controller.getMetrics().get(1).getActionId().equals("mltplr1"));

        assertEquals(controller.getInvokerById("invoker_0").getTotalMemory(), 1000);
        assertEquals(controller.getInvokerById("invoker_1").getTotalMemory(), 1000);
        assertEquals(0, controller.getInvokerById("invoker_0").getActions().size());
        assertEquals(0, controller.getInvokerById("invoker_1").getActions().size());

        assertEquals(BigInteger.valueOf(10), controller.getActionById("add1").getResult());
        assertEquals(BigInteger.valueOf(24), controller.getActionById("mltplr1").getResult());

        assertEquals(BigInteger.valueOf(10), controller.getInvokerById("invoker_0").getCache().get("add1").getResult());
        assertEquals(BigInteger.valueOf(24),
                controller.getInvokerById("invoker_0").getCache().get("mltplr1").getResult());
        assertNull(controller.getInvokerById("invoker_1").getCache().get("add1"));
        assertNull(controller.getInvokerById("invoker_1").getCache().get("mltplr1"));

        assertEquals(BigInteger.valueOf(10), controller.getInvokers().get(0).getCache().get("add1").getResult());
        assertEquals(BigInteger.valueOf(24),
                controller.getInvokerById("invoker_0").getCache().get("mltplr1").getResult());
        assertNull(controller.getInvokerById("invoker_1").getCache().get("add1"));
        assertNull(controller.getInvokerById("invoker_1").getCache().get("mltplr1"));
    }

    @Test
    public void DecoratorFactorial5s() {
        Controller.resetInstance();
        controller = Controller.getInstance();

        Invoker iv1Regular = new Invoker(1000, "invoker");
        Invoker iv1 = new InvokerCacheDecorator(iv1Regular);
        controller.addInvoker(iv1, 2);

        // check observer
        ArrayList<Invoker> invokers = controller.getInvokers();
        for (Invoker invoker : invokers) {
            assertTrue(invoker.getObservers().contains(controller));
        }

        BigGroup bigGroup = new BigGroup(1);
        controller.setPolicy(bigGroup);

        values = new int[] { 150 }; // muy rapido, mirar si es long long (comprobar resultado)
        Factorial factorial = new Factorial("factorial", 100, values);
        controller.addAction(factorial);

        assertEquals(controller.getInvokerById("invoker_0").getTotalMemory(), 1000);
        assertEquals(controller.getInvokerById("invoker_1").getTotalMemory(), 1000);

        assertTrue(controller.distributeActions());

        assertEquals(controller.getInvokerById("invoker_0").getTotalMemory(), 900);
        assertEquals(controller.getInvokerById("invoker_1").getTotalMemory(), 1000);
        assertEquals(1, controller.getInvokerById("invoker_0").getActions().size());
        assertEquals(0, controller.getInvokerById("invoker_1").getActions().size());

        controller.executeAssignedActions();

        assertTrue(controller.getMetrics().get(0).getActionId().equals("factorial"));

        assertEquals(controller.getInvokerById("invoker_0").getTotalMemory(), 1000);
        assertEquals(controller.getInvokerById("invoker_1").getTotalMemory(), 1000);
        assertEquals(0, controller.getInvokerById("invoker_0").getActions().size());
        assertEquals(0, controller.getInvokerById("invoker_1").getActions().size());

        BigInteger bigInteger = new BigInteger(
                "57133839564458545904789328652610540031895535786011264182548375833179829124845398393126574488675311145377107878746854204162666250198684504466355949195922066574942592095735778929325357290444962472405416790722118445437122269675520000000000000000000000000000000000000");
        assertEquals(bigInteger, controller.getActionById("factorial").getResult());

        // factorial has sleep(10s)
        assertEquals(bigInteger, controller.getInvokerById("invoker_0").getCache().get("factorial").getResult());
        assertNull(controller.getInvokerById("invoker_1").getCache().get("factorial"));

        // now the result is in cache
        assertTrue(controller.distributeActions());

        // now it does not allocate memory because the action is in cache.
        assertEquals(controller.getInvokerById("invoker_0").getTotalMemory(), 1000);
        assertEquals(controller.getInvokerById("invoker_1").getTotalMemory(), 1000);
        assertEquals(0, controller.getInvokerById("invoker_0").getActions().size());
        assertEquals(0, controller.getInvokerById("invoker_1").getActions().size());

        // factorial should NOT sleep(10s)
        controller.executeAssignedActions();

        assertTrue(controller.getMetrics().get(0).getActionId().equals("factorial"));

        assertEquals(0, controller.getInvokerById("invoker_0").getActions().size());
        assertEquals(0, controller.getInvokerById("invoker_1").getActions().size());
        assertEquals(controller.getInvokerById("invoker_0").getTotalMemory(), 1000);
        assertEquals(controller.getInvokerById("invoker_1").getTotalMemory(), 1000);

        assertEquals(bigInteger, controller.getActionById("factorial").getResult());
        assertEquals(bigInteger, controller.getInvokerById("invoker_0").getCache().get("factorial").getResult());
        assertNull(controller.getInvokerById("invoker_1").getCache().get("factorial"));
    }

    @Test
    public void FactorialWithoutCache10s() {
        Controller.resetInstance();
        controller = Controller.getInstance();

        Invoker iv1Regular = new Invoker(1000, "invoker");
        Invoker iv1 = iv1Regular;
        controller.addInvoker(iv1, 2);

        // check observer
        ArrayList<Invoker> invokers = controller.getInvokers();
        for (Invoker invoker : invokers) {
            assertTrue(invoker.getObservers().contains(controller));
        }

        BigGroup bigGroup = new BigGroup(1);
        controller.setPolicy(bigGroup);

        values = new int[] { 150 }; // muy rapido, mirar si es long long (comprobar resultado)
        Factorial factorial = new Factorial("factorial", 100, values);
        controller.addAction(factorial);

        assertEquals(controller.getInvokerById("invoker_0").getTotalMemory(), 1000);
        assertEquals(controller.getInvokerById("invoker_1").getTotalMemory(), 1000);

        assertTrue(controller.distributeActions());

        assertEquals(controller.getInvokerById("invoker_0").getTotalMemory(), 900);
        assertEquals(controller.getInvokerById("invoker_1").getTotalMemory(), 1000);
        assertEquals(1, controller.getInvokerById("invoker_0").getActions().size());
        assertEquals(0, controller.getInvokerById("invoker_1").getActions().size());

        controller.executeAssignedActions();

        assertTrue(controller.getMetrics().get(0).getActionId().equals("factorial"));

        assertEquals(controller.getInvokerById("invoker_0").getTotalMemory(), 1000);
        assertEquals(controller.getInvokerById("invoker_1").getTotalMemory(), 1000);
        assertEquals(0, controller.getInvokerById("invoker_0").getActions().size());
        assertEquals(0, controller.getInvokerById("invoker_1").getActions().size());

        BigInteger bigInteger = new BigInteger(
                "57133839564458545904789328652610540031895535786011264182548375833179829124845398393126574488675311145377107878746854204162666250198684504466355949195922066574942592095735778929325357290444962472405416790722118445437122269675520000000000000000000000000000000000000");
        assertEquals(bigInteger, controller.getActionById("factorial").getResult());

        // factorial has sleep(10s)
        assertEquals(bigInteger, controller.getInvokerById("invoker_0").getCache().get("factorial").getResult());
        assertNull(controller.getInvokerById("invoker_1").getCache().get("factorial"));

        assertTrue(controller.distributeActions());

        assertEquals(controller.getInvokerById("invoker_0").getTotalMemory(), 900);
        assertEquals(controller.getInvokerById("invoker_1").getTotalMemory(), 1000);
        assertEquals(1, controller.getInvokerById("invoker_0").getActions().size());
        assertEquals(0, controller.getInvokerById("invoker_1").getActions().size());

        // factorial should sleep(10s)
        controller.executeAssignedActions();

        assertTrue(controller.getMetrics().get(0).getActionId().equals("factorial"));

        assertEquals(0, controller.getInvokerById("invoker_0").getActions().size());
        assertEquals(0, controller.getInvokerById("invoker_1").getActions().size());
        assertEquals(controller.getInvokerById("invoker_0").getTotalMemory(), 1000);
        assertEquals(controller.getInvokerById("invoker_1").getTotalMemory(), 1000);

        assertEquals(bigInteger, controller.getActionById("factorial").getResult());
        assertEquals(bigInteger, controller.getInvokerById("invoker_0").getCache().get("factorial").getResult());
        assertNull(controller.getInvokerById("invoker_1").getCache().get("factorial"));
    }

    @Test
    public void DecoratorGreedy() {
        Controller.resetInstance();
        controller = Controller.getInstance();

        Invoker iv1Regular = new Invoker(1000, "invoker");
        Invoker iv1 = new InvokerCacheDecorator(iv1Regular);
        controller.addInvoker(iv1, 2);

        // check observer
        ArrayList<Invoker> invokers = controller.getInvokers();
        for (Invoker invoker : invokers) {
            assertTrue(invoker.getObservers().contains(controller));
        }

        controller.setPolicy(new GreedyGroup());

        values = new int[] { 2, 3, 4 };
        Action add100 = new Adder("add100", 100, values);
        Adder add200 = new Adder("add200", 200, values);
        Adder add800 = new Adder("add800", 800, values);

        controller.addAction(add100);
        controller.addAction(add200);
        controller.addAction(add800);
        assertTrue(controller.getActions().size() == 3);

        // ID DIFERENTE, MISMO VALUE (deberia iterar todo el hashmap y buscar si en
        // algun lao hay esos values)

        assertEquals(controller.getInvokerById("invoker_0").getTotalMemory(), 1000);
        assertEquals(controller.getInvokerById("invoker_1").getTotalMemory(), 1000);

        assertTrue(controller.getMetrics().size() == 0);
        assertTrue(controller.distributeActions());
        assertTrue(controller.getMetrics().size() == 0);

        assertEquals(controller.getInvokerById("invoker_0").getTotalMemory(), 700);
        assertEquals(controller.getInvokerById("invoker_1").getTotalMemory(), 200);
        assertEquals(2, controller.getInvokerById("invoker_0").getActions().size());
        assertEquals(1, controller.getInvokerById("invoker_1").getActions().size());

        controller.executeAssignedActions();

        assertTrue(controller.getMetrics().get(0).getActionId().equals("add100"));
        assertTrue(controller.getMetrics().get(1).getActionId().equals("add200"));
        assertTrue(controller.getMetrics().get(2).getActionId().equals("add800"));
        assertTrue(controller.getMetrics().size() == 3);

        assertEquals(controller.getInvokerById("invoker_0").getTotalMemory(), 1000);
        assertEquals(controller.getInvokerById("invoker_1").getTotalMemory(), 1000);
        assertEquals(0, controller.getInvokerById("invoker_0").getActions().size());
        assertEquals(0, controller.getInvokerById("invoker_1").getActions().size());

        controller.addAction(add100);
        controller.addAction(add100);
        controller.addAction(add100);

        assertTrue(controller.getActions().size() == 6);
        assertTrue(controller.distributeActions());

        assertTrue(controller.getMetrics().get(0).getActionId().equals("add100"));
        assertTrue(controller.getMetrics().get(1).getActionId().equals("add200"));
        assertTrue(controller.getMetrics().get(2).getActionId().equals("add800"));
        assertTrue(controller.getMetrics().size() == 3);

        assertEquals(controller.getInvokerById("invoker_0").getTotalMemory(), 200);
        assertEquals(controller.getInvokerById("invoker_1").getTotalMemory(), 1000);
        assertEquals(1, controller.getInvokerById("invoker_0").getActions().size());
        assertEquals(0, controller.getInvokerById("invoker_1").getActions().size());

        controller.executeAssignedActions();

        assertTrue(controller.getMetrics().get(0).getActionId().equals("add100"));
        assertTrue(controller.getMetrics().get(1).getActionId().equals("add200"));
        assertTrue(controller.getMetrics().get(2).getActionId().equals("add800"));
        assertTrue(controller.getMetrics().size() == 4);

        assertEquals(controller.getInvokerById("invoker_0").getTotalMemory(), 1000);
        assertEquals(controller.getInvokerById("invoker_1").getTotalMemory(), 1000);
        assertEquals(0, controller.getInvokerById("invoker_0").getActions().size());
        assertEquals(0, controller.getInvokerById("invoker_1").getActions().size());

        controller.addAction(add100);
        // should not be added (is in cache)
        assertFalse(controller.distributeActions());

        assertEquals(controller.getInvokerById("invoker_0").getTotalMemory(), 1000);
        assertEquals(controller.getInvokerById("invoker_1").getTotalMemory(), 1000);
        assertEquals(0, controller.getInvokerById("invoker_0").getActions().size());
        assertEquals(0, controller.getInvokerById("invoker_1").getActions().size());

        controller.executeAssignedActions();

        assertEquals(controller.getInvokerById("invoker_0").getTotalMemory(), 1000);
        assertEquals(controller.getInvokerById("invoker_1").getTotalMemory(), 1000);
        assertEquals(0, controller.getInvokerById("invoker_0").getActions().size());
        assertEquals(0, controller.getInvokerById("invoker_1").getActions().size());
    }

    @Test
    public void ObserverTest() {
        Controller.resetInstance();
        controller = Controller.getInstance();
        controller.setPolicy(roundRobinImproved);

        Invoker iv1 = new Invoker(1000, "iv1");
        controller.addInvoker(iv1);
        Invoker iv2 = new Invoker(2000, "iv2");
        controller.addInvoker(iv2);

        iv1.addObserver(controller);
        iv2.addObserver(controller);

        Adder add = new Adder("add", 100, values);
        controller.addAction(add, 5);

        controller.distributeActions();
        controller.executeAssignedActions(); // the invokers have notified the controller sending metrics
        assertEquals(5, controller.getMetrics().size()); // there are 5 actions, so there are 5 metrics, one for action

        for (Metric metric : controller.getMetrics()) { // it checks that there are information on the metrics
            assertNotNull(metric.getActionId());
            assertNotNull(metric.getAssignedInvoker());
            assertNotNull(metric.getExecutionTime());
            assertNotNull(metric.getUsedMemory());
        }
    }

    @Test
    public void InvokerWithoutDecoratorTest() {
        Controller.resetInstance();
        controller = Controller.getInstance();
        values = new int[] { 1, 2, 3, 4 };

        Invoker iv1Regular = new Invoker(1000, "1");
        Invoker iv2Regular = new Invoker(2500, "2");

        controller.addInvoker(iv1Regular);
        controller.addInvoker(iv2Regular);

        Adder add1 = new Adder("add1", 1200, values);
        Adder add2 = new Adder("add2", 1300, values);
        controller.setPolicy(new GreedyGroup());
        controller.addAction(add1);
        controller.addAction(add2);

        assertEquals(iv1Regular.getTotalMemory(), 1000);
        assertEquals(iv2Regular.getTotalMemory(), 2500);

        assertTrue(controller.distributeActions());

        assertTrue(iv2Regular.getActions().contains(add1));
        assertTrue(iv2Regular.getActions().contains(add2));
        assertEquals(0, iv1Regular.getActions().size());
        assertEquals(2, iv2Regular.getActions().size());
        assertEquals(iv1Regular.getTotalMemory(), 1000);
        assertEquals(iv2Regular.getTotalMemory(), 0);

        controller.executeAssignedActions();

        assertEquals(0, iv1Regular.getActions().size());
        assertEquals(0, iv2Regular.getActions().size());
        assertEquals(iv1Regular.getTotalMemory(), 1000);
        assertEquals(iv2Regular.getTotalMemory(), 2500);
        assertEquals(new BigInteger("10"), controller.getActionById("add1").getResult());
        assertEquals(new BigInteger("10"), controller.getActionById("add2").getResult());

        controller.getActions().clear();
        Adder add6 = new Adder("add6", 1100, values);
        Multiplier mul1 = new Multiplier("mul1", 1000, values);
        controller.addAction(add6);
        controller.addAction(add2);
        controller.addAction(mul1);

        assertEquals(0, iv1Regular.getActions().size());
        assertEquals(0, iv2Regular.getActions().size());
        assertEquals(iv1Regular.getTotalMemory(), 1000);
        assertEquals(iv2Regular.getTotalMemory(), 2500);

        assertTrue(controller.distributeActions());
        assertEquals(1, iv1Regular.getActions().size());
        assertEquals(2, iv2Regular.getActions().size());
        assertEquals(iv1Regular.getTotalMemory(), 0);
        assertEquals(iv2Regular.getTotalMemory(), 100);

        // MULITPLY ESTA ASIGNADO?
        // assertTrue(iv2Regular.getActions().contains(mul1));
        assertTrue(iv2Regular.getActions().contains(add6));
        assertTrue(iv2Regular.getActions().contains(add2));

        controller.executeAssignedActions();
        assertEquals(0, iv1Regular.getActions().size());
        assertEquals(0, iv2Regular.getActions().size());
        assertEquals(iv1Regular.getTotalMemory(), 1000);
        assertEquals(iv2Regular.getTotalMemory(), 2500);
        assertEquals(new BigInteger("10"), controller.getActionById("add6").getResult());
        assertEquals(new BigInteger("24"), controller.getActionById("mul1").getResult());

        // new value set
        values = new int[] { 1, 2, 3 };
        Adder add3 = new Adder("add3", 100, values);
        controller.addAction(add3);

        assertEquals(0, iv1Regular.getActions().size());
        assertEquals(0, iv2Regular.getActions().size());
        assertEquals(iv1Regular.getTotalMemory(), 1000);
        assertEquals(iv2Regular.getTotalMemory(), 2500);

        assertTrue(controller.distributeActions());
        assertTrue(iv1Regular.getActions().contains(mul1));
        assertTrue(iv2Regular.getActions().contains(add2));
        assertTrue(iv2Regular.getActions().contains(add3));
        assertTrue(iv2Regular.getActions().contains(add6));

        assertEquals(1, iv1Regular.getActions().size());
        assertEquals(3, iv2Regular.getActions().size());
        assertEquals(iv1Regular.getTotalMemory(), 0);
        assertEquals(iv2Regular.getTotalMemory(), 0);

        controller.executeAssignedActions();
        assertEquals(0, iv1Regular.getActions().size());
        assertEquals(0, iv2Regular.getActions().size());
        assertEquals(iv1Regular.getTotalMemory(), 1000);
        assertEquals(iv2Regular.getTotalMemory(), 2500);
        assertEquals(new BigInteger("10"), controller.getActionById("add6").getResult());
        assertEquals(new BigInteger("6"), controller.getActionById("add3").getResult()); // resultado en cache guardado
                                                                                         // anteriormente (old values)

        Invoker iv3Regular = new Invoker(3000, "3");
        controller.addInvoker(iv3Regular);
        add1.setMemory(3000);
        controller.addAction(add1);

        assertEquals(0, iv1Regular.getActions().size());
        assertEquals(0, iv2Regular.getActions().size());
        assertEquals(0, iv3Regular.getActions().size());
        assertEquals(iv1Regular.getTotalMemory(), 1000);
        assertEquals(iv2Regular.getTotalMemory(), 2500);
        assertEquals(iv3Regular.getTotalMemory(), 3000);

        assertTrue(controller.distributeActions());
        assertEquals(1, iv1Regular.getActions().size());
        assertEquals(3, iv2Regular.getActions().size());
        assertEquals(1, iv3Regular.getActions().size()); // el 3 lo hace porque no esta en su cache (y directamente no
                                                         // entra en los otros)

        assertEquals(iv1Regular.getTotalMemory(), 0);
        assertEquals(iv2Regular.getTotalMemory(), 0);
        assertEquals(iv3Regular.getTotalMemory(), 0);

        controller.executeAssignedActions();
        assertEquals(0, iv1Regular.getActions().size());
        assertEquals(0, iv2Regular.getActions().size());
        assertEquals(iv1Regular.getTotalMemory(), 1000);
        assertEquals(iv2Regular.getTotalMemory(), 2500);
        assertEquals(iv3Regular.getTotalMemory(), 3000);
    }

    @Test
    public void InvokerCacheDecoratorTest() {
        Controller.resetInstance();
        controller = Controller.getInstance();
        values = new int[] { 1, 2, 3, 4 };

        Invoker iv2Regular = new Invoker(2500, "2");
        InvokerCacheDecorator iv1Decorator = new InvokerCacheDecorator(new Invoker(1000, "1"));
        InvokerCacheDecorator iv2Decorator = new InvokerCacheDecorator(iv2Regular);

        controller.addInvoker(iv1Decorator);
        controller.addInvoker(iv2Decorator);

        Adder add1 = new Adder("add1", 1200, values);
        Adder add2 = new Adder("add2", 1300, values);
        controller.setPolicy(new GreedyGroup());
        controller.addAction(add1);
        controller.addAction(add2);

        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);

        assertTrue(controller.distributeActions());

        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(2, iv2Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 0);

        controller.executeAssignedActions();

        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);
        assertEquals(new BigInteger("10"), controller.getActionById("add1").getResult());
        assertEquals(new BigInteger("10"), controller.getActionById("add2").getResult());

        controller.getActions().clear();
        Adder add6 = new Adder("add6", 1100, values);
        Multiplier mul1 = new Multiplier("mul1", 1300, values);
        controller.addAction(add6);
        controller.addAction(add2);// este no lo hace
        controller.addAction(mul1);

        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);

        assertTrue(controller.distributeActions());
        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(2, iv2Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 100);

        controller.executeAssignedActions();
        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);
        assertEquals(new BigInteger("10"), controller.getActionById("add6").getResult());
        assertEquals(new BigInteger("24"), controller.getActionById("mul1").getResult());

        // new value set
        values = new int[] { 1, 2, 3 };
        Adder add3 = new Adder("add3", 1100, values);
        controller.addAction(add3);
        controller.addAction(add1);

        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);

        assertTrue(controller.distributeActions());
        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(1, iv2Decorator.getActions().size()); // 1 because add1, has old values (already in cache)
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 1400);

        controller.executeAssignedActions();
        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);
        assertEquals(new BigInteger("6"), controller.getActionById("add3").getResult());
        assertEquals(new BigInteger("10"), controller.getActionById("add1").getResult()); // resultado en cache guardado
                                                                                          // anteriormente (old values)

        Invoker iv3Decorator = new InvokerCacheDecorator(new Invoker(3000, "3"));
        controller.addInvoker(iv3Decorator);
        add1.setMemory(3000);
        controller.addAction(add1);

        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(0, iv3Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);
        assertEquals(iv3Decorator.getTotalMemory(), 3000);

        assertTrue(controller.distributeActions());
        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(1, iv3Decorator.getActions().size()); // el 3 lo hace porque no esta en su cache (y directamente no
                                                           // entra en los otros)

        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);
        assertEquals(iv3Decorator.getTotalMemory(), 0);

        controller.executeAssignedActions();
        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);
        assertEquals(iv3Decorator.getTotalMemory(), 3000);

        controller.addAction(add1);

        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(0, iv3Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);
        assertEquals(iv3Decorator.getTotalMemory(), 3000);

        assertFalse(controller.distributeActions());
        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(0, iv3Decorator.getActions().size()); // el 3 lo hace porque no esta en su cache (y directamente no
                                                           // entra en los otros)

        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);
        assertEquals(iv3Decorator.getTotalMemory(), 3000);

        controller.executeAssignedActions();
        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);
        assertEquals(iv3Decorator.getTotalMemory(), 3000);
        assertEquals(new BigInteger("10"), controller.getActionById("add1").getResult());

        add1.setValues(new int[] { 1, 2 });
        controller.addAction(add1);

        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(0, iv3Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);
        assertEquals(iv3Decorator.getTotalMemory(), 3000);

        assertTrue(controller.distributeActions());
        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(1, iv3Decorator.getActions().size());

        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);
        assertEquals(iv3Decorator.getTotalMemory(), 0);

        controller.executeAssignedActions();
        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);
        assertEquals(iv3Decorator.getTotalMemory(), 3000);
        assertEquals(new BigInteger("3"), controller.getActionById("add1").getResult());
    }

    @Test
    public void InvokerCacheAndChronoDecoratorTest() {
        Controller.resetInstance();
        controller = Controller.getInstance();
        values = new int[] { 1, 2, 3, 4 };

        InvokerChronometerDecorator iv1Decorator = new InvokerChronometerDecorator(
                new InvokerCacheDecorator(
                        new Invoker(1000, "1")));
        InvokerChronometerDecorator iv2Decorator = new InvokerChronometerDecorator(
                new InvokerCacheDecorator(
                        new Invoker(2500, "2")));

        controller.addInvoker(iv1Decorator);
        controller.addInvoker(iv2Decorator);

        Adder add1 = new Adder("add1", 1200, values);
        Adder add2 = new Adder("add2", 1300, values);
        controller.setPolicy(new GreedyGroup());
        controller.addAction(add1);
        controller.addAction(add2);

        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);

        assertTrue(controller.distributeActions());

        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(2, iv2Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 0);

        controller.executeAssignedActions();

        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);
        assertEquals(new BigInteger("10"), controller.getActionById("add1").getResult());
        assertEquals(new BigInteger("10"), controller.getActionById("add2").getResult());

        controller.getActions().clear();
        Adder add6 = new Adder("add6", 1100, values);
        Multiplier mul1 = new Multiplier("mul1", 1300, values);
        controller.addAction(add6);
        controller.addAction(add2);// este no lo hace
        controller.addAction(mul1);

        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);

        assertTrue(controller.distributeActions());
        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(2, iv2Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 100);

        controller.executeAssignedActions();
        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);
        assertEquals(new BigInteger("10"), controller.getActionById("add6").getResult());
        assertEquals(new BigInteger("24"), controller.getActionById("mul1").getResult());

        // new value set
        values = new int[] { 1, 2, 3 };
        Adder add3 = new Adder("add3", 1100, values);
        controller.addAction(add3);
        controller.addAction(add1);

        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);

        assertTrue(controller.distributeActions());
        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(1, iv2Decorator.getActions().size()); // 1 because add1, has old values (already in cache)
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 1400);

        controller.executeAssignedActions();
        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);
        assertEquals(new BigInteger("6"), controller.getActionById("add3").getResult());
        assertEquals(new BigInteger("10"), controller.getActionById("add1").getResult()); // resultado en cache guardado
                                                                                          // anteriormente (old values)

        Invoker iv3Decorator = new InvokerCacheDecorator(new Invoker(3000, "3"));
        controller.addInvoker(iv3Decorator);
        add1.setMemory(3000);
        controller.addAction(add1);

        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(0, iv3Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);
        assertEquals(iv3Decorator.getTotalMemory(), 3000);

        assertTrue(controller.distributeActions());
        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(1, iv3Decorator.getActions().size()); // el 3 lo hace porque no esta en su cache (y directamente no
                                                           // entra en los otros)

        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);
        assertEquals(iv3Decorator.getTotalMemory(), 0);

        controller.executeAssignedActions();
        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);
        assertEquals(iv3Decorator.getTotalMemory(), 3000);

        controller.addAction(add1);

        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(0, iv3Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);
        assertEquals(iv3Decorator.getTotalMemory(), 3000);

        assertFalse(controller.distributeActions());
        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(0, iv3Decorator.getActions().size()); // el 3 lo hace porque no esta en su cache (y directamente no
                                                           // entra en los otros)

        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);
        assertEquals(iv3Decorator.getTotalMemory(), 3000);

        controller.executeAssignedActions();
        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);
        assertEquals(iv3Decorator.getTotalMemory(), 3000);
        assertEquals(new BigInteger("10"), controller.getActionById("add1").getResult());

        add1.setValues(new int[] { 1, 2 });
        controller.addAction(add1);

        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(0, iv3Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);
        assertEquals(iv3Decorator.getTotalMemory(), 3000);

        assertTrue(controller.distributeActions());
        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(1, iv3Decorator.getActions().size());

        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);
        assertEquals(iv3Decorator.getTotalMemory(), 0);

        controller.executeAssignedActions();
        assertEquals(0, iv1Decorator.getActions().size());
        assertEquals(0, iv2Decorator.getActions().size());
        assertEquals(iv1Decorator.getTotalMemory(), 1000);
        assertEquals(iv2Decorator.getTotalMemory(), 2500);
        assertEquals(iv3Decorator.getTotalMemory(), 3000);
        assertEquals(new BigInteger("3"), controller.getActionById("add1").getResult());

        Multiplier mul2 = new Multiplier("mul2", 1300, values);
        assertTrue(iv1Decorator.chronometer(add1) != "");
        assertTrue(iv1Decorator.chronometer(add2) != "");
        assertTrue(iv1Decorator.chronometer(add3) != "");
        assertTrue(iv1Decorator.chronometer(add6) != "");
        assertTrue(iv1Decorator.chronometer(mul1) != "");
        assertTrue(iv1Decorator.chronometer(mul2) == "");

    }

    @Test
    public void executeThisAction() {
        Controller.resetInstance();
        controller = Controller.getInstance();
        values = new int[] { 1, 2, 3, 4 };

        Invoker iv1Decorator = new Invoker(1000, "1");
        Invoker iv2Decorator = new Invoker(1000, "2");

        controller.addInvoker(iv1Decorator);
        controller.addInvoker(iv2Decorator);

        Adder a = new Adder("a", 100, values);
        Adder b = new Adder("b", 100, values);
        Adder c = new Adder("c", 100, values);

        controller.addAction(a);
        controller.addAction(b);
        controller.addAction(c);

        assertTrue(controller.distributeActions());
        controller.executeActionById("a");

        assertEquals(new BigInteger("10"), controller.getActionById("a").getResult());

        assertEquals(null, controller.getActionById("b").getResult());
        assertEquals(null, controller.getActionById("c").getResult());

        controller.executeAssignedActions();

        assertEquals(new BigInteger("10"), controller.getActionById("a").getResult());
        assertEquals(new BigInteger("10"), controller.getActionById("b").getResult());
        assertEquals(new BigInteger("10"), controller.getActionById("c").getResult());
    }

    @Test
    public void ActionDynamicProxyTest() {
        Controller.resetInstance();
        controller = Controller.getInstance();
        values = new int[] { 1, 2, 3, 4 };

        InvokerChronometerDecorator iv1Decorator = new InvokerChronometerDecorator(
                new InvokerCacheDecorator(
                        new Invoker(1000, "1")));
        InvokerChronometerDecorator iv2Decorator = new InvokerChronometerDecorator(
                new InvokerCacheDecorator(
                        new Invoker(2500, "2")));

        controller.addInvoker(iv1Decorator);
        controller.addInvoker(iv2Decorator);

        Adder adder = new Adder("add", 100, values);
        Multiplier mul = new Multiplier("mul", 100, values);

        InterfaceAction t = (InterfaceAction) Proxy.newProxyInstance(
                Action.class.getClassLoader(),
                new Class<?>[] { InterfaceAction.class },
                new DynamicProxy(adder));
        t.operation();
        //buscar en controller
        InterfaceAction action = ActionProxy.invoke(mul);
        action.operation();
        
        assertEquals(new BigInteger("10"), t.getResult());
        assertEquals(new BigInteger("24"), action.getResult());
    }
}