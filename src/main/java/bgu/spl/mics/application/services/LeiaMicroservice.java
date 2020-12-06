package bgu.spl.mics.application.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackCompleteEvent;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.passiveObjects.Attack;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {

	private Map<Attack,Boolean> attacks;
	
    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");

		this.attacks=new HashMap<Attack,Boolean>();
		for (Attack attack:attacks)
        {
            //used put if absent if the fuckers send us 2 of the same attack
            this.attacks.putIfAbsent(attack,false);
        }
    }

    @Override
    protected void initialize() {
        subscribeEvent(AttackCompleteEvent.class,callback->{
            attacks.put(callback.getAttack().getAttack(),true);
    });

        attacks.forEach((attack,completed)->{
            AttackEvent event=new AttackEvent(attack);
            sendEvent(event);
        });

        while(attacks.containsValue(false)){
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        sendEvent(new DeactivationEvent());
    }
}
