package misc.lock;

import org.junit.Test;

import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.lang.Thread.currentThread;
import static java.util.concurrent.ForkJoinPool.commonPool;
import static java.util.concurrent.locks.LockSupport.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;

/**
 * @author i-katas
 * @since 1.0
 */
public class LockSupportTest {
    private volatile Thread thread;

    @Test
    public void threadOnParkBlockedUntilHavingAvailablePermits() throws InterruptedException, ExecutionException, TimeoutException {
        Future<?> blockedThread = startedThread(() -> {
            thread = currentThread();
            park(this);
        });

        assertThrows(TimeoutException.class, () -> awaitUntilTimeout(blockedThread));
        assertThat(getBlocker(thread), is(this));

        unpark(thread);
        awaitUntilTimeout(blockedThread);
    }

    @Test
    public void threadOnParkWillNotBlockIfHavingAvailablePermits() throws InterruptedException, ExecutionException, TimeoutException {
        awaitUntilTimeout(startedThread(() -> {
            unpark(currentThread());
            park();
        }));
    }

    private void awaitUntilTimeout(Future<?> task) throws InterruptedException, ExecutionException, TimeoutException {
        task.get(500, TimeUnit.MILLISECONDS);
    }

    private Future<?> startedThread(ThrowableRunnable task) {
        return commonPool().submit(task);
    }

    /**
     * an Adapter Functional Interface of <code>Runnable</code> that support throwing checked exceptions.
     */
    @FunctionalInterface
    private interface ThrowableRunnable extends Runnable {
        default void run() {
            try {
                execute();
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }

        void execute() throws Exception;
    }
}
