package main.reflection;

import main.Action;
import main.Controller;
import main.interfaces.InterfaceAction;

import java.lang.reflect.*;

/**
 * Proxy class for actions that adds actions to the controller and creates dynamic proxies using reflection.
 */
public class ActionProxy {

    /**
     * Invokes an action by adding it to the controller and creating a dynamic proxy using reflection.
     *
     * @param action The action to be invoked.
     * @return A dynamic proxy implementing the InterfaceAction interface.
     */
    public static InterfaceAction invoke(Action action) {
        Controller controller = Controller.getInstance();
        System.out.println("ActionProxy added action to controller: " + action.toString());
        controller.addAction(action);

        // Creates a DynamicProxy for the action using reflection
        return (InterfaceAction) Proxy.newProxyInstance(
                Action.class.getClassLoader(),
                new Class<?>[]{InterfaceAction.class},
                new DynamicProxy(action));
    }
}
