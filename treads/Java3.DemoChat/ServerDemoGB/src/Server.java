public class Server {

    public static void main(String[] args) {
        Thread t1 = new Thread(new Homework.WaitNotify('A', 'B'));
        Thread t2 =  new Thread(new Homework.WaitNotify('B', 'C'));
        Thread t3 = new Thread(new Homework.WaitNotify('C', 'A'));
        t1.start();
        t2.start();
        t3.start();
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        new MyServer();
    }
}
