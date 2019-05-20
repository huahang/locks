package im.huahang.spinlocks;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTests {
    private static volatile long counter = 0;

    public static void main(final String[] args) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("TASLock.csv")))) {
            emit("n,duration,counter", writer);
            for (int n = 1; n <= 20; ++n) {
                runTest(n, TASLock.class, writer);
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("TTASLock.csv")))) {
            emit("n,duration,counter", writer);
            for (int n = 1; n <= 20; ++n) {
                runTest(n, TTASLock.class, writer);
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("ReentrantLock.csv")))) {
            emit("n,duration,counter", writer);
            for (int n = 1; n <= 20; ++n) {
                runTest(n, ReentrantLock.class, writer);
            }
        }
    }

    private static void emit(final String line, final BufferedWriter writer) throws IOException {
        System.out.println(line);
        writer.write(line);
        writer.newLine();
    }

    private static void runTest(int n, Class<? extends Lock> lockClass, BufferedWriter writer) throws Exception {
        final Lock lock = lockClass.getDeclaredConstructor().newInstance();
        counter = 0;
        long begin = System.nanoTime();
        Thread[] threads = new Thread[n];
        for (int i = 0; i < n; ++i) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 65536; ++j) {
                    lock.lock();
                    try {
                        counter++;
                    } finally {
                        lock.unlock();
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
        emit(n + "," + duration + "," + counter, writer);
    }
}
