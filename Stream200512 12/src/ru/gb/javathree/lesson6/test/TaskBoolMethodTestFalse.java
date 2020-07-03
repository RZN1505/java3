package ru.gb.javathree.lesson6.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.gb.javathree.lesson6.Main;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TaskBoolMethodTestFalse {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new int[]{1, 1, 1, 0}},
                {new int[]{4, 0, 4, 4}},
                {new int[]{4, 1, 2, 3}}
        });
    }

    private int[] in;

    public TaskBoolMethodTestFalse(int[] in) {
        this.in = in;
    }

    private Main main;

    @Before
    public void startTest() {
        main = new Main();
    }

    @Test
    public void testBoolMethod() {
        Assert.assertTrue(Main.boolMethod(in));
    }
}
