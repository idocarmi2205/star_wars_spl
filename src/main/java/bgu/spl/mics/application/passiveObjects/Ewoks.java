package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.MessageBusImpl;

import java.util.*;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
    private Ewok[] ewoks;

    /**
     * private constructor to enable creating a single instance of Ewoks
     */
    private Ewoks() {
    }

    /**
     * @return only instance of Ewoks
     */
    public static Ewoks getInstance() {
        return Ewoks.EwoksHolder.instance;
    }

    public void init(int numOfEwoks) {
        ewoks = new Ewok[numOfEwoks];
        for (int i = 0; i < numOfEwoks; i++) {
            ewoks[i] = new Ewok(i + 1);
        }
    }

    public void acquireEwoks(List<Integer> serialNumbers) {
        synchronized (ewoks) {
            for (Integer serial : serialNumbers) {
                ewoks[serial - 1].acquire();
                System.out.println("ewok: "+serial + " aquired");
            }
        }
    }

    public void releaseEwoks(List<Integer> serialNumbers) {
        for (Integer serial : serialNumbers) {
                ewoks[serial - 1].release();
            System.out.println("ewok: "+serial + " released");
        }
    }

    /**
     * private static class which will hold the single instance of Ewoks
     */
    private static class EwoksHolder {
        private static final Ewoks instance = new Ewoks();
    }
}

