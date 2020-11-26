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

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {
        MessageBusImpl bus;
        @BeforeAll
        void setUpClass(){

        }
    @BeforeEach
    void setUp() {
        bus=new MessageBusImpl();
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
        Future<Boolean> future=new Future<>();
        HanSoloMicroservice service=new HanSoloMicroservice();
        bus.subscribeEvent(AttackEvent.class,service);
        bus.sendEvent(new AttackEvent(future));
        assertTrue(future.get());
    }

    /**
     *
     * i dont know how we need this for implementation...
     * maybe some kind of counter for total number of attacks
     * that can only be recorded by han and c3p0
     */
    @Test
    void subscribeBroadcast() {
        Future<Integer> future=new Future<>();
        LinkedList<MicroService> services=new LinkedList<MicroService>() {
        };
        for (int i = 0; i < 5; i++) {
            services.add(new IntCounterMicroservice());
            bus.subscribeBroadcast(TestBroadcastEvent.class,services.getLast());
        }
        bus.sendBroadcast(new TestBroadcastEvent());
        assertEquals(5,(int) future.get() );
    }

    @Test
    void complete() {
    }

    @Test
    void sendBroadcast() {
    }

    @Test
    void sendEvent() {
    }

    @Test
    void register() {
    }

    @Test
    void unregister() {
    }

    @Test
    void awaitMessage() {
    }
}