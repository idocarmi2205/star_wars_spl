package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TestBroadcastEvent;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import bgu.spl.mics.application.services.IntCounterMicroservice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {
        MessageBusImpl bus;
    @BeforeEach
    void setUp() {
        bus=bus.getInstance();
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * subscribe han solo to an attack event
     * also sends an attack event
     * then asserts that the event is completed
     *
     * checks both the subscribe and send methods
     * can't test one without the other (no getters...)
     */
    @Test
    void subscribeAndSendEvent() {
        Future<Boolean> future= new Future<Boolean>();
        HanSoloMicroservice service=new HanSoloMicroservice();
        bus.register(service);
        bus.subscribeEvent(AttackEvent.class,service);
        Future<Boolean> f = bus.sendEvent(new AttackEvent());
        assertTrue(future.get());
    }

    /**
     *
     *tests subscribe and send broadcast
     * subscribes multiple services to same type of event
     * send broadcast of said event
     * tests that all services received the event and acted upon it
     */
    @Test
    void subscribeAndSendBroadcast() {
        Future<Integer> future=new Future<Integer>();
        LinkedList<MicroService> services=new LinkedList<MicroService>() {
        };
        for (int i = 0; i < 5; i++) {
            //test type of microservice that increases increment for an int future
            services.add(new IntCounterMicroservice());
            bus.register(services.getLast());
            bus.subscribeBroadcast(TestBroadcastEvent.class,services.getLast());
        }
        bus.sendBroadcast(new TestBroadcastEvent());
        //checks that all the services increased the int
        assertEquals(5,(int) future.get() );
    }


    /**
     * creates a microservice and registers and subscribes it to an event
     * uses await message in a different thread and checks that the message is received
     */
    @Test
    void awaitMessage(){
        Future<Boolean> future=new Future<Boolean>();
        final Message[] message = {null};
        HanSoloMicroservice service=new HanSoloMicroservice();
        bus.register(service);
        bus.subscribeEvent(AttackEvent.class,service);
        Thread newThread = new Thread(()->{
            try {
                message[0] =bus.awaitMessage(service);
            } catch (InterruptedException e) {
                //test should not pass if awaitMessage can be interrupted
                fail("awaitMessage was interrupted");
                e.printStackTrace();
            }
        });
        newThread.start();
        bus.sendEvent(new AttackEvent());
        assertNotNull(message[0]);
    }

    /**
     * creates an attack event
     * completes event
     * checks that the future is resolved and holds the correct value
     */
    @Test
    void complete(){
        Future<Boolean> future=new Future<Boolean>();
        AttackEvent event=new AttackEvent();
        bus.complete(event,true);
        //ensures that the event is resolved
        assertTrue(future.isDone());
        //ensures that the result is inserted into the future
        assertTrue(future.get());
    }
}