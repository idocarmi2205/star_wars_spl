package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;

/**
 * will be sent by han solo and c3p0 to signal leia that the attack is complete
 */
public class AttackCompleteEvent implements Event<Boolean> {

    private AttackEvent attack;

    public AttackCompleteEvent(AttackEvent attack){
        this.attack = attack;
    }

    public AttackEvent getAttack(){
        return attack;
    }
}
