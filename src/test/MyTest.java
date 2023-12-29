package test;

import main.*;
import main.policy.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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

        Invoker iv1 = new Invoker(1000, 1);
        Invoker iv2 = new Invoker(2000, 2);

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
        // Configurar System.out para capturar la salida de la consola
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Invoker iv1 = new Invoker(1000, 1);
        Invoker iv2 = new Invoker(2000, 2);

        controller.addInvoker(iv1);
        controller.addInvoker(iv2);

        Adder add1 = new Adder("add1", 1500, values);

        controller.addAction(add1);
        controller.distributeActions();

        assertEquals(controller.distributeActions(), false);

        // Restaurar System.out
        System.setOut(new PrintStream(System.out));

        // Verificar si el mensaje de error esperado se encuentra en la salida de la
        // consola
        assertTrue(outContent.toString().contains("Not enough memory to take the action"));
    }

    @Test
    public void addActionsandLastInsufficientMemoryActions() {
        Controller.resetInstance();
        controller = Controller.getInstance();
        // Configurar System.out para capturar la salida de la consola
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Invoker iv1 = new Invoker(1000, 1);
        Invoker iv2 = new Invoker(2000, 2);

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

        // Restaurar System.out
        System.setOut(new PrintStream(System.out));

        // Verificar si el mensaje de error esperado se encuentra en la salida de la
        // consola
        assertTrue(outContent.toString().contains("Not enough memory to take the action"));
    }

    @Test
    public void distributeActionsTestRoundRobin() {
        Controller.resetInstance();
        controller = Controller.getInstance();
        Invoker iv1 = new Invoker(1000, 1);
        Invoker iv2 = new Invoker(2000, 2);

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
        Invoker iv1 = new Invoker(1000, 1);
        Invoker iv2 = new Invoker(2000, 2);

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
        Invoker iv1 = new Invoker(1000, 1);
        Invoker iv2 = new Invoker(2000, 2);

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
        Invoker iv1 = new Invoker(1000, 1);
        Invoker iv2 = new Invoker(2000, 2);

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
        Invoker iv1 = new Invoker(1000, 1);
        controller.addInvoker(iv1);
        Invoker iv2 = new Invoker(2000, 2);
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
        Invoker iv1 = new Invoker(1000, 1);
        controller.addInvoker(iv1);
        Invoker iv2 = new Invoker(2000, 2);
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
        Invoker iv1 = new Invoker(1000, 1);
        controller.addInvoker(iv1);
        Invoker iv2 = new Invoker(2000, 2);
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
        int[] expectedResults = new int[controller.getActions().size()];
        int i = 0;
        for (Action action : results) {
            if (action.getResult() != 0) {
                expectedResults[i] = action.getResult();
                i++;
            }
        }
        assertArrayEquals(expectedResults, new int[] { 10, 24, 10, 10, 10, 10 });
    }

    @Test
    public void GreedyGroupTest() {
        Controller.resetInstance();
        controller = Controller.getInstance();

        Invoker iv1 = new Invoker(3000, 1);
        Invoker iv2 = new Invoker(1000, 2);
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

        Adder add6 = new Adder("add6", 3100, values);

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

        // Test 3______________________________
        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);

        controller.getActions().clear();
        controller.addAction(add1, 2);

        assertTrue(controller.distributeActions());

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

        iv1 = new Invoker(3000, 1);
        iv2 = new Invoker(1000, 2);
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

        iv1 = new Invoker(90, 1);
        iv2 = new Invoker(10, 2);
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

        Invoker iv3 = new Invoker(1500, 3);
        Invoker iv4 = new Invoker(2000, 4);
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
    public void testUniformGroupDistribution() throws InsufficientMemoryException {
        Controller.resetInstance();
        controller = Controller.getInstance();
        // 3 actions 2 invokers
        UniformGroup uniformGroup = new UniformGroup(); // -> 2 actions 1 invoker
        controller.setPolicy(uniformGroup); // -> 1 action 1 invoker

        Invoker iv1 = new Invoker(1000, 1);
        controller.addInvoker(iv1);
        Invoker iv2 = new Invoker(2000, 2);
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

        Invoker iv1 = new Invoker(1000, 1);
        controller.addInvoker(iv1);
        Invoker iv2 = new Invoker(2000, 2);
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
        Invoker iv1 = new Invoker(1000, 1);
        controller.addInvoker(iv1);
        Invoker iv2 = new Invoker(2000, 2);
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

        Invoker iv1 = new Invoker(1000, 1);
        controller.addInvoker(iv1);
        Invoker iv2 = new Invoker(1500, 2);
        controller.addInvoker(iv2);
        Invoker iv3 = new Invoker(2000, 3);
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

        Invoker iv1 = new Invoker(2000, 1);
        Invoker iv2 = new Invoker(5000, 2);

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

        Invoker iv1 = new Invoker(1500, 1);
        Invoker iv2 = new Invoker(1000, 2);
        Invoker iv3 = new Invoker(3000, 3);

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

        Invoker iv1 = new Invoker(1400, 1);
        Invoker iv2 = new Invoker(2000, 2);
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

        Invoker iv1 = new Invoker(0, 1);
        Invoker iv2 = new Invoker(0, 2);
        controller.addInvoker(iv1);
        controller.addInvoker(iv2);

        Adder add1 = new Adder("add1", 1000, values);
        controller.addAction(add1, 12);

        assertFalse(controller.distributeActions());
        assertEquals(0, iv1.getActions().size());
        assertEquals(0, iv2.getActions().size());
    }
}