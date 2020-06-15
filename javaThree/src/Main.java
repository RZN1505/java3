
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.util.Collections.*;

class Fruit {
    float weight;

    public Fruit(float weight) {
        this.weight = weight;
    }

    public float getWeight() {
      return this.weight;
    }
}

class Apple extends Fruit {

    public Apple() {
     super(1.0f);
    }
}

class Orange extends Fruit {

    public Orange() {
        super(1.5f);
    }
}

class Box <T extends Fruit>{
    ArrayList <T> fruits = new ArrayList<>();
    float weight;

    public void add(T fruit) {
      this.fruits.add(fruit);
      weight = weight + fruit.getWeight();
    }

    public float getWeight() {
        return this.weight;
    }

    public void diff (Box<?> another) {
        if (Math.abs(this.getWeight() - another.getWeight()) < 0.0001) {
            System.out.println("равны");
        } else {
            System.out.println("отличаются");
        }
    }

    public void toAnother (Box<T> another) {
        another.fruits.addAll(this.fruits);
        this.fruits.clear();
        this.weight = 0;
    }


}

public class Main {

    public static <T> T[] reverse(T[] arr1, int i1, int i2) {
        T[] arr = Arrays.copyOf(arr1, arr1.length);
        T sw = arr[i1];
        arr[i1]=arr[i2];
        arr[i2]=sw;
        return arr;
    }

    public static <T> ArrayList<T> toArrList(T[] arr1) {
        ArrayList<T> arr = new ArrayList<>();
        addAll(arr, arr1);
        return arr;
    }

    public static void main(String[] args) {
        Integer arr1[] = {1, 2, 3};
        String arr2[] = {"a", "b", "c"};
        System.out.println("reverse arr1: " + Arrays.toString(reverse(arr1, 0, arr1.length - 1)));
        System.out.println("reverse arr2: " + Arrays.toString(reverse(arr2, 0, arr2.length - 1)));

        System.out.println("arr1 toArrList: " + toArrList(arr1));
        System.out.println("arr2 toArrList: " + toArrList(arr2));

        Box<Orange> box1 = new Box<>();
        Box<Orange> box2 = new Box<>();
        Box<Apple> box3 = new Box<>();
        box1.add(new Orange());
        box2.add(new Orange());
        box3.add(new Apple());
        box3.add(new Apple());

        System.out.println("Box 1 getWeight: "+box1.getWeight());
        System.out.println("Box 2 getWeight: "+box2.getWeight());
        System.out.println("Box 3 getWeight: "+box3.getWeight());

        System.out.println("Box 1 diff box 3: ");
        box1.diff(box3);
        System.out.println("Box 2 diff box 1: ");
        box2.diff(box1);

        box1.toAnother(box2);

        System.out.println("Box 1 afterToAnother getWeight: "+box1.getWeight());
        System.out.println("Box 2 afterToAnother getWeight: "+box2.getWeight());
    }
}