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
