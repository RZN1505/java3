package ru.gb.lesson7;


public class TestClass {
    public static  void method1() {
        System.out.println("met1");
    }
    @BeforeSuite
    public static void start() {
        System.out.println("startMethod");
    }
    @Test(priority = 4)
    public static  void method5() {
        System.out.println("met5");
    }
    @Test(priority = 3)
    public static  void method2() {
        System.out.println("met2");
    }
    @Test(priority = 2)
    public static  void method3() {
        System.out.println("met3");
    }
    @Test(priority = 1)
    public static  void method4() {
        System.out.println("met4");
    }
    @AfterSuite
    public static void finishMethod() {
        System.out.println("finishMethod");
    }
}

