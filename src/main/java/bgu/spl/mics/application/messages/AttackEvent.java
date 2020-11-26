package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.Future;

public class AttackEvent implements Event<Boolean> {
    final Future<Boolean> future;
	public AttackEvent(Future<Boolean> future){
	    this.future=future;
    }
}
