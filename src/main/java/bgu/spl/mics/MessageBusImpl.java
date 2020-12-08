package bgu.spl.mics;


//import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

    Map<MicroService, BlockingQueue<Message>> microServiceQueues;
    Map<Class<? extends Event>, BlockingQueue<MicroService>> eventQueues;
    Map<Class<? extends Broadcast>, BlockingQueue<MicroService>> broadcastLists;
    Map<Event, Future> futures;

    /**
     * private constructor to enable creating a single instance of MessageBusImpl
     */
    private MessageBusImpl() {
        microServiceQueues = new ConcurrentHashMap<>();
        eventQueues = new ConcurrentHashMap<>();
        broadcastLists = new ConcurrentHashMap<>();
        futures = new ConcurrentHashMap<>();
    }

    /**
     * @return only instance of MessageBusImpl
     */
    public static MessageBusImpl getInstance() {
        return MessageBusHolder.instance;
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
        synchronized (type) {
            eventQueues.putIfAbsent(type, new LinkedBlockingQueue<>());
            eventQueues.get(type).add(m);
        }
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        synchronized (type) {
            broadcastLists.putIfAbsent(type, new LinkedBlockingQueue<>());
            broadcastLists.get(type).add(m);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void complete(Event<T> e, T result) {
        futures.get(e).resolve(result);
        futures.remove(e);
    }

    @Override
    public void sendBroadcast(Broadcast b) {
        if (broadcastLists.containsKey((b.getClass()))) {
            for (MicroService m : broadcastLists.get(b.getClass())) {
                microServiceQueues.get(m).add(b);
            }
        }
    }


    @Override
    /**
     * send the event to the next available microservice
     * returns the future
     * if no microservices are subscribed to this type of event return null
     */
    public <T> Future<T> sendEvent(Event<T> e) {
        synchronized (e.getClass()) {
            if (eventQueues.containsKey(e.getClass()) && !eventQueues.get(e.getClass()).isEmpty()) {
                MicroService curr = eventQueues.get(e.getClass()).poll();
                Future<T> f = new Future<>();
                futures.put(e, f);
                microServiceQueues.get(curr).add(e);
                eventQueues.get(e.getClass()).add(curr);

                return f;
            }
            return null;
        }

    }

    @Override
    public void register(MicroService m) {
        if (!microServiceQueues.containsKey(m)) {
            microServiceQueues.put(m, new LinkedBlockingQueue<>());
        }
    }

    @Override
    public void unregister(MicroService m) {
        microServiceQueues.remove(m);
        for (Class<? extends Event> e : eventQueues.keySet()){
            eventQueues.get(e).remove(m);
        }
        for (Class<? extends Broadcast> b : broadcastLists.keySet()){
            broadcastLists.get(b).remove(m);
        }

    }

    @Override
    /**
     * return the first message in the microservice's queue
     * if no message exists, wait
     */
    public Message awaitMessage(MicroService m) throws InterruptedException {
        return microServiceQueues.get(m).take();
    }

    /**
     * private static class which will hold the single instance of MessageBusImpl
     */
    private static class MessageBusHolder {
        private static final MessageBusImpl instance = new MessageBusImpl();
    }
}
