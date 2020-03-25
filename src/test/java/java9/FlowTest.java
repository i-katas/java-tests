package java9;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

/**
 * @author i-katas
 * @since 1.0
 */
public class FlowTest {
    public static final int waitMillis = 500;
    private final SubmissionPublisher<String> publisher = new SubmissionPublisher<>(Executors.newFixedThreadPool(5), 2);
    private final BlockingItemsCollector<String> subscriber = new BlockingItemsCollector<>(waitMillis);

    @Before
    public void setUp() {
        publisher.subscribe(subscriber);
        subscriber.request(Integer.MAX_VALUE);
    }

    @Test
    public void receivesMessagesSequentially() throws InterruptedException {
        publisher.submit("first");
        publisher.submit("last");

        subscriber.hasReceivedItem(equalTo("first"));
        subscriber.hasReceivedItem(equalTo("last"));
        subscriber.hasReceivedNoItems();
    }

    @Test
    public void notifyAllSubscribers() throws InterruptedException {
        BlockingItemsCollector<String> subscriber2 = new BlockingItemsCollector<>(waitMillis);
        publisher.subscribe(subscriber2);
        String aMessage = "a message";

        subscriber2.request(Integer.MAX_VALUE);
        publisher.submit(aMessage);

        subscriber.hasReceivedItem(equalTo("a message"));
        subscriber.hasReceivedNoItems();
        subscriber2.hasReceivedItem(equalTo("a message"));
        subscriber2.hasReceivedNoItems();
    }

    @Test
    public void subscriberLostMessageBeforeSubscribing() throws InterruptedException {
        publisher.submit("a message");

        BlockingItemsCollector<String> subscriber2 = new BlockingItemsCollector<>(waitMillis);
        publisher.subscribe(subscriber2);

        subscriber.hasReceivedItem(equalTo("a message"));
        subscriber2.hasReceivedNoItems();
    }

    @Test
    public void submitMessageBlockedWhenExceedsBufferSize() {
        publisher.consume(delayed(waitMillis * 2));

        for (int i = publisher.getMaxBufferCapacity(); i >= 0; --i) {
            publisher.submit("message-" + i);
        }

        CompletableFuture<?> pushing = CompletableFuture.runAsync(() -> publisher.submit("blocked"));
        assertThrows(TimeoutException.class, () -> pushing.get(waitMillis, MILLISECONDS));
    }

    private static Consumer<? super String> delayed(int milliSeconds) {
        return unused -> {
            try {
                Thread.sleep(milliSeconds);
            } catch (InterruptedException ignored) {/**/}
        };
    }

    @Test
    public void consumeUntilPublisherClosed() throws InterruptedException {
        publisher.submit("a message");

        subscriber.hasReceivedItem(equalTo("a message"));
        subscriber.hasReceivedNoItems();
        assertFalse("done", subscriber.awaitCompletedUntilTimeout());

        publisher.close();
        assertTrue("done", subscriber.awaitCompletedUntilTimeout());
    }

    @Test
    public void skipIssuesMessageIfSubscriberNotRequested() throws InterruptedException {
        BlockingItemsCollector<String> subscriber = new BlockingItemsCollector<>(waitMillis);
        publisher.subscribe(subscriber);

        publisher.submit("a message");
        subscriber.hasReceivedNoItems();

        subscriber.request(1);
        subscriber.hasReceivedItem(equalTo("a message"));
    }

    @After
    public void tearDown() {
        publisher.close();
    }

    private static class BlockingItemsCollector<T> implements Flow.Subscriber<T> {
        private final BlockingQueue<T> items;
        private final CountDownLatch completed;
        private final long timeoutMillis;
        private volatile int preRequests;
        private volatile Flow.Subscription subscription;

        private BlockingItemsCollector(long timeoutMillis) {
            this(3, timeoutMillis);
        }

        public BlockingItemsCollector(int capacity, long timeoutMillis) {
            this.items = new ArrayBlockingQueue<>(capacity);
            this.completed = new CountDownLatch(1);
            this.timeoutMillis = timeoutMillis;
        }

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            request(preRequests);
        }

        @Override
        public void onNext(T item) {
            try {
                items.put(item);
            } catch (InterruptedException e) {
                onError(e);
            }
        }

        public synchronized void request(int n) {
            if (subscription != null) {
                if (n > 0) {
                    subscription.request(n);
                    preRequests = 0;
                }
            } else {
                preRequests = n;
            }
        }

        @Override
        public void onError(Throwable throwable) {
            completed.countDown();
        }

        @Override
        public void onComplete() {
            completed.countDown();
        }

        public void hasReceivedNoItems() throws InterruptedException {
            hasReceivedItem(nullValue());
        }

        public void hasReceivedItem(Matcher<? super T> itemMatcher) throws InterruptedException {
            assertThat(items.poll(timeoutMillis, MILLISECONDS), itemMatcher);
        }

        public boolean awaitCompletedUntilTimeout() throws InterruptedException {
            return completed.await(timeoutMillis, MILLISECONDS);
        }
    }
}
