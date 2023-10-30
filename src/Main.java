import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
//every time we assign an action to an invoker we MUST invoke the method take memory. 
//every time that an action has finished we MUST give memory to the Invoker.
    Controller controller=new Controller();
    Invoker iv1=new Invoker(1000);
    controller.addInvoker(iv1);
    Invoker iv2=new Invoker(2000);
    controller.addInvoker(iv2);
    //we have to add the invokers to the controller enviroment

    int[] values={1,2,3,4};
    Adder add1=new Adder("add1", 150, values);
    controller.addAction(add1);
    Multiplier mltplr1=new Multiplier("mltplr1", 200, values);
    controller.addAction(mltplr1);
    //same with actions, we have to add this actions to the invoker

    System.out.println("The actual memory of iv1 is "+iv1.getTotalMemory());
    System.out.println("The actual memory of iv2 is "+iv2.getTotalMemory());

    iv1.setAction(add1);
    //iv1.takeMemory(add1.getMemory());
    System.out.println("Iv1 has 1 action");
    System.out.println("The actual memory of iv1 is "+iv1.getTotalMemory());

    iv2.setAction(mltplr1);
    //iv2.takeMemory(mltplr1.getMemory());
    System.out.println("Iv2 has 1 action");
    System.out.println("The actual memory of iv1 is "+iv2.getTotalMemory());



    controller.executeActions();
    System.out.println("Iv1 has 0 action");
    System.out.println("Iv2 has 0 action");

    //iv1.giveMemory(iv1.getAction().getMemory());
    //iv2.giveMemory(iv2.getAction().getMemory());
    System.out.println("The actual memory of iv1 is "+iv1.getTotalMemory());
    System.out.println("The actual memory of iv1 is "+iv2.getTotalMemory());

    System.out.println("Results of the actions: ");
    List<Action> results = controller.getActions();
    for (Action action : results) {
        action.operation(); 
        System.out.println("Result of the action " + action.getId() + ": " + action.getResult());
    }
}
}

