package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


import static org.junit.jupiter.api.Assertions.*;


public class FutureTest {

    private Future<String> future;

    @BeforeEach
    public void setUp() {
        future = new Future<>();
    }

    @Test
    public void testResolve() {
        String str = "someResult";
        future.resolve(str);
        assertTrue(future.isDone());
        assertTrue(str.equals(future.get()));
    }

    @Test
    public void testIsDone() {
        String str = "someResult";
        assertTrue(!future.isDone());
        future.resolve(str);
        assertTrue(future.isDone());
    }

    @Test
    public void testEmptyGet() {
        String str = "someResult";
        Thread newThread = new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            future.resolve(str);

        });
        Instant startTime = Instant.now();
        newThread.start();
        String answer = future.get();
        Instant endTime=Instant.now();
        assertEquals(answer, str);
        assertEquals(3, endTime.getEpochSecond()-startTime.getEpochSecond() );
    }

    @Test
    public void testTimedGet() {
        String str = "someResult";

        String answer;
        Thread newThread = new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            future.resolve(str);

        });
        newThread.start();
        answer = future.get(2,TimeUnit.SECONDS);
        assertEquals(answer, null);
        answer=future.get(2,TimeUnit.SECONDS);
        assertEquals(answer,str);
    }

}
