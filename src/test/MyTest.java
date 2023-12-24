package test;

import main.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

public class MyTest {

    Controller controller = new Controller();
    RoundRobinImproved roundRobinImproved = new RoundRobinImproved();

    @Test
    public void addition() {

        controller.setPolicy(roundRobinImproved);

        Invoker iv1 = new Invoker(1000);
        controller.addInvoker(iv1);
        Invoker iv2 = new Invoker(2000);
        controller.addInvoker(iv2);

        assertEquals(iv1.getTotalMemory(), 1000, "iv1 ok" + iv1.toString());
        assertEquals(iv2.getTotalMemory(), 2000, "iv2 ok" + iv2.toString());
    }
}