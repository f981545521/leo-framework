package cn.acyou.leo.framework.commons;

import lombok.Getter;

import java.util.concurrent.Semaphore;

/**
 * @author youfang
 * @version [1.0.0, 2025/3/23]
 **/
public class StateSemaphore extends Semaphore {
    private final Object maxPermitsMutex = new Object();
    private final int maxPermits;

    @Getter
    private Object info;


    public StateSemaphore(int maxPermits, boolean fair) {
        super(maxPermits, fair);
        this.maxPermits = maxPermits;
    }

    public StateSemaphore(boolean fair) {
        super(1, fair);
        this.maxPermits = 1;
    }

    public boolean isRunning() {
        return availablePermits() < maxPermits;
    }

    public void setInfo(Object info) {
        synchronized (this.maxPermitsMutex) {
            this.info = info;
        }
    }

    @Override
    public void release() {
        super.release();
        setInfo(null);
    }
}
