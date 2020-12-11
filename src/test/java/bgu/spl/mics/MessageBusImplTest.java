package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TestBroadcastEvent;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.services.C3POMicroservice;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import bgu.spl.mics.application.services.IntCounterMicroservice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {
        MessageBus bus;
        Attack attack = new Attack(new LinkedList<Integer>(Arrays.asList(1,2)), 1000);
        CountDownLatch count = new CountDownLatch(2);



    @BeforeEach
    void setUp() {
        bus=MessageBusImpl.getInstance();
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
        Ewoks.getInstance().init(2);
        HanSoloMicroservice han=new HanSoloMicroservice(count);
        bus.register(han);
        bus.subscribeEvent(AttackEvent.class,han);
        Thread t = new Thread(han);
        Future<Boolean> f = bus.sendEvent(new AttackEvent(attack));
        t.start();
        assertTrue(f.get());
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
        Future<Integer> future=new Future<>();
        LinkedList<MicroService> services=new LinkedList<MicroService>() {
        };
        for (int i = 0; i < 5; i++) {
            //test type of microservice that increases increment for an int future
            services.add(new IntCounterMicroservice(""+ i));
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
        Future<Boolean> future=new Future<>();
        final Message[] message = {null};
        HanSoloMicroservice service=new HanSoloMicroservice(count);
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
        bus.sendEvent(new AttackEvent(attack));
        assertNotNull(message[0]);
    }

    /**
     * creates an attack event
     * completes event
     * checks that the future is resolved and holds the correct value
     */
    @Test
    void complete(){

        AttackEvent event=new AttackEvent(attack);
        C3POMicroservice c = new C3POMicroservice(count);
        bus.register(c);
        bus.subscribeEvent(AttackEvent.class,c);
        Future<Boolean> future= bus.sendEvent(event);
        bus.complete(event,true);
        //ensures that the event is resolved
        assertTrue(future.isDone());
        //ensures that the result is inserted into the future
        assertTrue(future.get());
    }

}