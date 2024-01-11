package main.reflection;

import java.lang.reflect.*;

/**
 * Dynamic Proxy class that implements the InvocationHandler interface for method invocation.
 */
public class DynamicProxy implements InvocationHandler {
    private Object target = null;

    /**
     * Creates a new instance of DynamicProxy with the specified target.
     *
     * @param target The target object for which the proxy is created.
     */
    public DynamicProxy(Object target) {
        this.target = target;
    }

    /**
     * Creates a new instance of a proxy for the specified target class.
     *
     * @param target The target object for which the proxy is created.
     * @return A proxy object implementing the interfaces of the target class.
     */
    public static Object newInstance(Object target) {
        Class<?> targetClass = target.getClass();
        return (Object) Proxy.newProxyInstance(targetClass.getClassLoader(), targetClass.getInterfaces(),
                new DynamicProxy(target));
    }

    /**
     * Invokes a method on the target object and performs additional actions before and after the invocation.
     *
     * @param proxy  The proxy object.
     * @param method The method to be invoked.
     * @param args   The arguments to be passed to the method.
     * @return The result of the method invocation.
     * @throws Throwable If an error occurs during the method invocation.
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object invocationResult = null;
        try {
            System.out.println("Method invoked from DynamicProxy: " + method.getName());
            invocationResult = method.invoke(this.target, args);
        } catch (InvocationTargetException ite) {
            throw ite.getTargetException();
        } catch (Exception e) {
            System.err.println("Invocation of " + method.getName() + " failed");
        }
        return invocationResult;
    }
}
