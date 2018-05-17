package com.jdanque.debounce;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class DebounceTest {

    private ExecutorService executorService;
    private Debounce debounce;

    private Stack<Integer> db; //a test db

    @Before
    public void setUp(){
        debounce = new Debounce();

        db = new Stack<>();
        db.push(1);
        db.push(2);
        db.push(3);
    }

    @Test(timeout = 300)
    public void shouldExecuteAfter() throws InterruptedException {

        long delay = 100;

        debounce.setId("Test");
        debounce.setDelay(delay);
        debounce.setTask(() -> db.pop());

        //before execute
        Assert.assertEquals(3, db.size());

        ExecutorService executor = debounce.execute();

        //assert right after
        //should still be the same as before but not after delay
        Assert.assertEquals(3, db.size());

        //assert after delay with somebuffer
        Thread.sleep(110);
        Assert.assertEquals(2, db.size());

        //delay to see result
        executor.awaitTermination(delay + 50, TimeUnit.MILLISECONDS);
    }

    @Test(timeout = 400)
    public void shouldExecuteAfter_twoCalls() throws InterruptedException {

        long delay = 100;

        debounce.setId("Test");
        debounce.setDelay(delay);
        debounce.setTask(() -> db.pop());

        //before execute
        Assert.assertEquals(3, db.size());

        ExecutorService executor = debounce.execute();

        //must not sleep >= delay
        Thread.sleep(delay/2);

        //call again after sleeping
        debounce.execute();

        //assert right after
        //should still be the same as before but not after delay
        Assert.assertEquals(3, db.size());

        //assert after delay with somebuffer
        Thread.sleep(110);
        Assert.assertEquals(2, db.size());

        //delay to see result
        executor.awaitTermination(delay + 50, TimeUnit.MILLISECONDS);
    }

     @Test(timeout = 300)
    public void shouldNotCancelAfter() throws InterruptedException {

        long delay = 100;

        debounce.setId("Test");
        debounce.setDelay(delay);
        debounce.setMode(DebounceMode.CANCEL);
        debounce.setTask(() -> db.pop());

        //before execute
        Assert.assertEquals(3, db.size());

        //execute once only
        ExecutorService executor = debounce.execute();

        //assert right after
        //should still be the same as before but not after delay
        Assert.assertEquals(3, db.size());

        //assert after delay with somebuffer
        //should not be the same since
        //debounce is only called once even with
        //DebounceMode.Cancel
        Thread.sleep(110);
        Assert.assertEquals(2, db.size());

        //delay to see result
        executor.awaitTermination(delay + 50, TimeUnit.MILLISECONDS);

    }

    @Test(timeout = 300)
    public void shouldCancelAfter_twoCall() throws InterruptedException {

        long delay = 100;

        debounce.setId("Test");
        debounce.setDelay(delay);
        debounce.setMode(DebounceMode.CANCEL);
        debounce.setTask(() -> db.pop());

        //before execute
        Assert.assertEquals(3, db.size());

        //execute twice to cancel
        ExecutorService executor = debounce.execute();
        debounce.execute();

        //assert right after
        //should still be the same as before but not after delay
        Assert.assertEquals(3, db.size());

        //assert after delay with somebuffer
        //should still be the same
        Thread.sleep(110);
        Assert.assertEquals(3, db.size());

        //delay to see result
        executor.awaitTermination(delay + 50, TimeUnit.MILLISECONDS);

    }

    @Test(timeout = 300)
    public void shouldCancelAfter_changingMode() throws InterruptedException {

        long delay = 100;

        debounce.setId("Test");
        debounce.setDelay(delay);
        debounce.setTask(() -> db.pop());

        //before execute
        Assert.assertEquals(3, db.size());

        //execute 1st with delay
        ExecutorService executor = debounce.execute();

        //execute 2nd time but new object
        // same id
        // and change mode
        Debounce debounce2 = new Debounce("Test",
                delay,
                DebounceMode.CANCEL,
                ()-> db.pop());

        debounce2.execute();

        //assert right after
        //should still be the same as before but not after delay
        Assert.assertEquals(3, db.size());

        //assert after delay with somebuffer
        //should still be the same
        Thread.sleep(110);
        Assert.assertEquals(3, db.size());

        //delay to see result
        executor.awaitTermination(delay + 50, TimeUnit.MILLISECONDS);

    }

}
