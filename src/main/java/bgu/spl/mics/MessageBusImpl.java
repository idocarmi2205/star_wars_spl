package bgu.spl.mics;


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

	Map<MicroService,BlockingQueue<Message>> microServiceQueues;
	Map<Class<? extends Event>,BlockingQueue<MicroService>> eventQueues;
	Map<Class<? extends Event>,List<MicroService>> broadcastLists;

	/**
	 * private constructor to enable creating a single instance of MessageBusImpl
	 */
	private MessageBusImpl(){
		microServiceQueues=new ConcurrentHashMap<>();
		eventQueues=new ConcurrentHashMap<>();
		broadcastLists=new ConcurrentHashMap<>();
	}

	/**
	 * @return only instance of MessageBusImpl
	 */
	public static MessageBusImpl getInstance(){
		return MessageBusHolder.instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		if(!eventQueues.containsKey(type)){
			eventQueues.put(type,new LinkedBlockingQueue<>());
		}
		eventQueues.get(type).add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		
    }

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		
        return null;
	}

	@Override
	public void register(MicroService m) {
		if(!microServiceQueues.containsKey(m)){
			microServiceQueues.put(m,new LinkedBlockingQueue<>());
		}
	}

	@Override
	public void unregister(MicroService m) {
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		
		return null;
	}

	/**
	 * private static class which will hold the single instance of MessageBusImpl
	 */
	private static class MessageBusHolder{
		private static final MessageBusImpl instance=new MessageBusImpl();
	}
}
