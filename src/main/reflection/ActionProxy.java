package main.reflection;

import main.Action;
import main.Controller;
import main.interfaces.InterfaceAction;
import java.lang.reflect.*;

public class ActionProxy {
    public static InterfaceAction invoke(Action action) {
        Controller controller = Controller.getInstance();
        controller.addAction(action);
        
        return (InterfaceAction) Proxy.newProxyInstance(
                Action.class.getClassLoader(),
                new Class<?>[] { InterfaceAction.class },
                new DynamicProxy(action));
    }
}
