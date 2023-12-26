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
        // Test 1______________________________
        controller.addAction(add1);
        controller.addAction(add2, 3);
        controller.addAction(add5);

        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);

        controller.distributeActions();

        assertEquals(iv1.getTotalMemory(), 700);
        assertEquals(iv2.getTotalMemory(), 200);

        controller.executeAssignedActions();

        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);

        // Test 2______________________________
        Adder add6 = new Adder("add6", 3100, values);

        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);

        controller.getActions().clear();
        controller.addAction(add6);

        controller.distributeActions();

        // testing it doesn't try to execute an unassigned action
        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);

        controller.executeAssignedActions();

        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);

        // Test 3______________________________
        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);

        controller.getActions().clear();
        controller.addAction(add1, 2);

        controller.distributeActions();

        assertEquals(iv1.getTotalMemory(), 1000);
        assertEquals(iv2.getTotalMemory(), 1000);

        controller.executeAssignedActions();
        // HA LIBERADO MEMORIA SIN QUE ESTE REALMENTE EJECUTADA (falta comprobacion de
        // ejecutar correctamente)
        // si no se ha asignado a ningun invoker porque entra, no se puede liberar

        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);
    }

    @Test
    public void GreedyGroupTest2() {
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

        /*
         * //Test 1______________________________
         * controller.addAction(add1);
         * controller.addAction(add2, 3);
         * controller.addAction(add5);
         * 
         * assertEquals(iv1.getTotalMemory(), 3000);
         * assertEquals(iv2.getTotalMemory(), 1000);
         * 
         * controller.distributeActions();
         * 
         * assertEquals(iv1.getTotalMemory(), 700);
         * assertEquals(iv2.getTotalMemory(), 200);
         * 
         * controller.executeAssignedActions();
         * 
         * assertEquals(iv1.getTotalMemory(), 3000);
         * assertEquals(iv2.getTotalMemory(), 1000);
         * 
         * //Test 2______________________________
         * Adder add6 = new Adder("add6", 3100, values);
         * 
         * assertEquals(iv1.getTotalMemory(), 3000);
         * assertEquals(iv2.getTotalMemory(), 1000);
         * 
         * controller.getActions().clear();
         * controller.addAction(add6);
         * 
         * controller.distributeActions();
         * 
         * //testing it doesn't try to execute an unassigned action
         * assertEquals(iv1.getTotalMemory(), 3000);
         * assertEquals(iv2.getTotalMemory(), 1000);
         * 
         * controller.executeAssignedActions();
         * 
         * assertEquals(iv1.getTotalMemory(), 3000);
         * assertEquals(iv2.getTotalMemory(), 1000);
         * 
         */

        // Test 3______________________________
        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);

        controller.getActions().clear();
        controller.addAction(add1, 2);

        controller.distributeActions();

        assertEquals(iv1.getTotalMemory(), 1000);
        assertEquals(iv2.getTotalMemory(), 1000);

        controller.executeAssignedActions();
        // HA LIBERADO MEMORIA SIN QUE ESTE REALMENTE EJECUTADA (falta comprobacion de
        // ejecutar correctamente)
        // si no se ha asignado a ningun invoker porque entra, no se puede liberar

        // el mismo action con mismo ID esta asignada al mismo invoker, por lo que si
        // una no entra, creera que si ha entrado,
        // porque la misma instancia esta asignada varias veces

        // aunque no deberia porque estan en el array (se supone en posiciones
        // diferentes)

        assertEquals(iv1.getTotalMemory(), 3000);
        assertEquals(iv2.getTotalMemory(), 1000);
    }
}