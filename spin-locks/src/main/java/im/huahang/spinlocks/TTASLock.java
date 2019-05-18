package im.huahang.spinlocks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class TTASLock implements Lock {
    private AtomicBoolean state = new AtomicBoolean(false);

    public void lock() {
        while (true) {
            while (state.get());
            if (!state.getAndSet(true)) {
                return;
            }
        }
    }

    public void lockInterruptibly() throws InterruptedException {

    }

    public boolean tryLock() {
        return false;
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    public void unlock() {
        state.set(false);
    }

    public Condition newCondition() {
        return null;
    }
}
