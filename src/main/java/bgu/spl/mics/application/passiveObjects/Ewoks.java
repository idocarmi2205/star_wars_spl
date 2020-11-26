package bgu.spl.mics.application.passiveObjects;


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
    private Ewok [] ewoks;

    public Ewoks(int numOfEwoks){
        ewoks = new Ewok[numOfEwoks];
        for (int i=0; i<numOfEwoks; i++){
            ewoks[0] = new Ewok(i+1);
        }
    }

    public void assignEwoks(List<Integer> serialNumbers){
        for (int i=0; i<serialNumbers.size(); i++){
            int sn = serialNumbers.get(i);
            if (!ewoks[sn-1].available)
                return false;
            ewoks[sn-1].available = false;
        }
        return true;
    }

    public void release
}
