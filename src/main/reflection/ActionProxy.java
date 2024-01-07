package main.reflection;

import main.Action;
import main.interfaces.InterfaceAction;
import java.lang.reflect.*;

public class ActionProxy {
    public static InterfaceAction invoke(Action action) {
        return (InterfaceAction) Proxy.newProxyInstance(
                Action.class.getClassLoader(),
                new Class<?>[] { InterfaceAction.class },
                new DynamicProxy(action));
    }
}
