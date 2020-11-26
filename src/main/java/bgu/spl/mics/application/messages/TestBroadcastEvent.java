package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

import java.util.concurrent.atomic.AtomicInteger;

public class TestBroadcastEvent implements Broadcast {
    AtomicInteger counter;
    public TestBroadcastEvent(int cnt){
        counter=new AtomicInteger(cnt);
    }
    public TestBroadcastEvent(){
        counter=new AtomicInteger(0);
    }
}
