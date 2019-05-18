package im.huahang.spinlocks;

import java.util.concurrent.locks.Lock;

public class LockTests {
    private static long counter = 0;

    public static void main(final String[] args) throws Exception {
        System.out.println("TASLock");
        System.out.println("n,duration,counter");
        for (int n = 1; n <= 100; ++n) {
            runTest(n, TASLock.class);
        }
        System.out.println("TTASLock");
        System.out.println("n,duration,counter");
        for (int n = 1; n <= 100; ++n) {
            runTest(n, TTASLock.class);
        }
    }

    private static void runTest(int n, Class<? extends Lock> lockClass) throws Exception {
        final Lock lock = lockClass.getDeclaredConstructor().newInstance();
        long begin = System.nanoTime();
        Thread[] threads = new Thread[n];
        for (int i = 0; i < n; ++i) {
            threads[i] = new Thread(new Runnable() {
                public void run() {
                    for (int i = 0; i < 8192; ++i) {
                        lock.lock();
                        try {
                            counter++;
                        } finally {
                            lock.unlock();
                        }
                    }
                }
            });
        }
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
        long end = System.nanoTime();
        long duration = end - begin;
        System.out.println(n + "," + duration + "," + counter);
    }
}
