package bgu.spl.mics.application.services;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.Attack;

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


    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");

        this.attacks = new HashMap<>();
        for (Attack attack : attacks) {
            //used put if absent if the fuckers send us 2 of the same attack
            this.attacks.putIfAbsent(attack, null);
        }
    }

    @Override
    protected void initialize() {

        attacks.forEach((attack, completed) -> {
            AttackEvent event = new AttackEvent(attack);
            attacks.put(attack, sendEvent(event));
        });

        while(!areAllAttacksComplete()){
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean areAllAttacksComplete() {
        boolean toReturn = true;
        for (Future<Boolean> future : attacks.values()) {
            if (!future.isDone()) {
                toReturn = false;
                break;
            }
        }
        return toReturn;
    }
}
