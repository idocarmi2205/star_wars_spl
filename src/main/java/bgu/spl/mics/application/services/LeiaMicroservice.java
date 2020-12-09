package bgu.spl.mics.application.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.FinishedAttacksBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {

    private Map<Attack, Future<Boolean>> attacks;
    private CountDownLatch count;



    public LeiaMicroservice(Attack[] attacks, CountDownLatch count) {
        super("Leia");
        this.count = count;
        this.attacks = new HashMap<>();
        for (Attack attack : attacks) {
            //used put if absent if send us 2 of the same attack
            this.attacks.putIfAbsent(attack, null);
        }
    }

    @Override
    protected void initialize() {
        /**
         * wait for 5 milliseconds so that all the attack microservices have a chance to subscribe to the event
         */
//        try {
//            TimeUnit.MILLISECONDS.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        try {
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Set<Attack> attackSet = attacks.keySet();
        for ( Attack attack : attackSet){
            //if no subscribers yet for attackEvent returns null instead of future
            while (attacks.get(attack) == null) {
                AttackEvent event = new AttackEvent(attack);
                attacks.put(attack, sendEvent(event));
            }
        }

        sendBroadcast(new FinishedAttacksBroadcast());

        for (Future<Boolean> future : attacks.values()){
            future.get();
        }

        sendEvent(new DeactivationEvent());
    }

    @Override
    protected void recordTerminationTime() {
        Diary.setLeiaTerminate(System.currentTimeMillis());
    }


}
