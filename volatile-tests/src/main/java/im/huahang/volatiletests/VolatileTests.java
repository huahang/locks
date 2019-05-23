package im.huahang.volatiletests;

public class VolatileTests {
    static private long globalCounter = 0;
    static private volatile long volatileGlobalCounter = 0;
    static private boolean running = true;

    static private class Adder extends Thread {
        private volatile long localCounter = 0;
        @Override
        public void run() {
            while (running) {
                synchronized (VolatileTests.class) {
                    volatileGlobalCounter++;
                    localCounter++;
                    globalCounter++;
                }
            }
        }
    }

    static public void main(final String[] args) {
        Adder[] adders = new Adder[8];
        for (int i = 0; i < 8; ++i) {
            adders[i] = new Adder();
            adders[i].start();
        }
        Runtime.getRuntime().addShutdownHook(
            new Thread(
                () -> {
                    running = false;
                    try {
                        for (Adder adder : adders) {
                            adder.join();
                        }
                        System.out.println("Global Counter: " + globalCounter);
                        System.out.println("Volatile Global Counter: " + volatileGlobalCounter);
                        long sum = 0;
                        for (Adder adder : adders) {
                            sum += adder.localCounter;
                        }
                        System.out.println("Local Counters Sum: " + sum);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            )
        );
    }
}
