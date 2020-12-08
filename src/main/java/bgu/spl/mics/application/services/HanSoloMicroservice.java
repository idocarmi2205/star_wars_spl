package bgu.spl.mics.application.services;


import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.FinishedAttacksBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.concurrent.TimeUnit;

/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {

    public HanSoloMicroservice() {
        super("Han");
    }


    Ewoks ewoks;

    @Override
    protected void initialize() {

        ewoks = Ewoks.getInstance();

        subscribeEvent(AttackEvent.class, callback -> {
            Attack attackToMake = callback.getAttack();
            ewoks.acquireEwoks(attackToMake.getSerials());
            try {
                TimeUnit.MILLISECONDS.sleep(attackToMake.getDuration());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ewoks.releaseEwoks(attackToMake.getSerials());
            complete(callback, true);
            Diary.addAttack();
        });

        subscribeBroadcast(FinishedAttacksBroadcast.class, callback->{
            Diary.setHanSoloFinish(System.currentTimeMillis());
        });

    }

    @Override
    protected void recordTerminationTime() {
        Diary.setHanSoloTerminate(System.currentTimeMillis());
    }
}
