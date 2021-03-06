package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.FinishedAttacksBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


/**
 * abstract class for attackMicroservices: HanSolo ans c3po.
 * both microservices act the same, so we can write some function for both of them
 */
public abstract class AttackMicroservice extends MicroService {

    Ewoks ewoks;
    CountDownLatch count;


    /**
     * @param name the micro-service name (used mainly for debugging purposes -
     *             does not have to be unique)
     */
    public AttackMicroservice(String name, CountDownLatch count) {
        super(name);
        this.count = count;
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
//            System.out.println("Attack " +attackToMake.getSerials()+" Completed By "+getName());
            Diary.addAttack();
        });
        count.countDown();
        subscribeBroadcast(FinishedAttacksBroadcast.class, callback->{
            setFinishTime();
        });


    }

    protected abstract void setFinishTime();

}
