package main.reflection;

import java.lang.reflect.*;

public class DynamicProxy implements InvocationHandler {
    private Object target = null;

    public static Object newInstance(Object target) {
        Class<?> targetClass = target.getClass();
        return (Object) Proxy.newProxyInstance(targetClass.getClassLoader(), targetClass.getInterfaces(), new DynamicProxy(target));
    }
    

    public DynamicProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object invocationResult = null;
        // Lógica de asignación al Controller
        try {
            System.out.println("Before method " + method.getName());
            invocationResult = method.invoke(this.target, args);
            System.out.println("After method " + method.getName());
        } catch (InvocationTargetException ite) {
            throw ite.getTargetException();
        } catch (Exception e) {
            System.err.println("Invocation of " + method.getName() + " failed");
        }
        return invocationResult;
    }
}
