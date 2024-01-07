package main.interfaces;

import java.math.BigInteger;

public interface InterfaceAction {
    void setId(String id);

    double getMemory();

    void setMemory(double memory);

    int[] getValues();

    void setValues(int[] values);

    BigInteger getResult();

    void setResult(BigInteger result);

    InterfaceInvoker getInvoker();

    String toString();

    void operation();
}
