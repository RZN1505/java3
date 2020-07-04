package ru.gb.lesson7;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Main {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        beginTest(TestClass.class);
    }

    public  static void beginTest(Class c) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = c.getDeclaredMethods();
        List<Method> list = new ArrayList<Method>();
        for (Method m: methods) {
            if(m.isAnnotationPresent(Test.class)) {
                int priority = m.getAnnotation(Test.class).priority();
                if(priority < 1 || priority > 10) throw  new RuntimeException("Priority error!");
                list.add(m);
            }
        }
        list.sort((m1, m2) -> m2.getAnnotation(Test.class).priority() - m1.getAnnotation(Test.class).priority());
        for (Method m: methods) {
            if(m.isAnnotationPresent(BeforeSuite.class)) {
                if(list.get(0).isAnnotationPresent(BeforeSuite.class))
                    throw new RuntimeException("BeforeSuite error");
                list.add(0, m);
            }
            if(m.isAnnotationPresent(AfterSuite.class)) {
                if(list.get(list.size() - 1).isAnnotationPresent(AfterSuite.class))
                    throw new RuntimeException("AfterSuite error");
                list.add(m);
            }
        }
        for (Method m: list) {
            m.invoke(null);
        }
    }
}