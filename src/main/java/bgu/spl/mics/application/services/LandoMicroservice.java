package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

import java.util.concurrent.TimeUnit;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
        private long duration;

    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration = duration;
    }

    @Override
    protected void initialize() {
        subscribeEvent(BombDestroyerEvent.class, callback->{
            try {
                TimeUnit.MILLISECONDS.sleep(duration);
                sendBroadcast(new TerminationBroadcast());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


    }

    @Override
    protected void recordTerminationTime() {
        Diary.setLandoTerminate(System.currentTimeMillis());
    }
}
