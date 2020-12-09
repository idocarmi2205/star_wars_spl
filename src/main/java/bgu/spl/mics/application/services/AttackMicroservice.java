package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.FinishedAttacksBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.concurrent.TimeUnit;

public abstract class AttackMicroservice extends MicroService {

    Ewoks ewoks;


    /**
     * @param name the micro-service name (used mainly for debugging purposes -
     *             does not have to be unique)
     */
    public AttackMicroservice(String name) {
        super(name);
    }

    @Override
    protected void initialize() {

        ewoks = Ewoks.getInstance();

        subscribeEvent(AttackEvent.class, callback -> {
            Attack attackToMake=callback.getAttack();
            ewoks.acquireEwoks(attackToMake.getSerials());
            try {
                TimeUnit.MILLISECONDS.sleep(attackToMake.getDuration());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ewoks.releaseEwoks(attackToMake.getSerials());
            complete(callback,true);
            System.out.println("Attack " +attackToMake.getSerials()+" Completed By "+getName());
            Diary.addAttack();
        });

        subscribeBroadcast(FinishedAttacksBroadcast.class, callback->{
            Diary.setC3P0Finish(System.currentTimeMillis());
        });


    }

}
