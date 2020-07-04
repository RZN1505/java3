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
public class MainTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new int[]{1,2,3,4,5,6,7}, new int[]{5,6,7}},
                {new int[]{1,4,3,4,5,6,7,15}, new int[]{5,6,7,15}},
                {new int[]{4,8,9}, new int[]{8,9}}
        });
    }

    private int[] a;
    private int[] res;

    public MainTest(int[] a, int[] res) {
        this.a = a;
        this.res = res;
    }

    private Main main;


    @Before
    public void startTest() {
        main = new Main();
    }

    @Test
    public void testFourMethod() {
        Assert.assertArrayEquals(res, Main.fourMethod(a));
    }

    @Test
    public void testBoolMethod() {
        Assert.assertArrayEquals(res, Main.fourMethod(a));
    }
}

