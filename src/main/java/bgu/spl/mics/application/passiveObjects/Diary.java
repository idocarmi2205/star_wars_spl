package bgu.spl.mics.application.passiveObjects;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {
    private AtomicInteger totalAttacks;
    private long HanSoloFinish;
    private long C3P0Finish;
    private long R2D2Deactivate;
    private long LeiaTerminate;
    private long HanSoloTerminate;
    private long C3P0Terminate;
    private long R2D2Terminate;
    private long LandoTerminate;

    private Diary(){
        totalAttacks=new AtomicInteger(0);
    }

    /**
     * @return only instance of Ewoks
     */
    public static Diary getInstance() { return Diary.DiaryHolder.instance; }

    public AtomicInteger getNumberOfAttacks() {
        return totalAttacks;
    }

    public long getC3POFinish() {
        return getC3POFinish();
    }

    public long getHanSoloFinish() {
        return HanSoloFinish;
    }

    public long getR2D2Deactivate() {
        return R2D2Deactivate;
    }

    public long getHanSoloTerminate() {
        return HanSoloTerminate;
    }

    public long getC3POTerminate() {
        return C3P0Terminate;
    }

    public long getLandoTerminate() {
        return LandoTerminate;
    }

    public long getR2D2Terminate() {
        return R2D2Terminate;
    }

    public void resetNumberAttacks() {
        totalAttacks.set(0);
    }

    private static class DiaryHolder{
        private static final Diary instance = new Diary();
    }
    public static void Terminate(String name){

    }


    public static void setHanSoloFinish(long hanSoloFinish) {
        getInstance().HanSoloFinish = hanSoloFinish;
    }

    public static void setC3P0Finish(long c3P0Finish) {
        getInstance().C3P0Finish = c3P0Finish;
    }

    public static void setR2D2Deactivate(long r2D2Deactivate) {
        getInstance().R2D2Deactivate = r2D2Deactivate;
    }

    public static void setLeiaTerminate(long leiaTerminate) {
        getInstance().LeiaTerminate = leiaTerminate;
    }

    public static void setHanSoloTerminate(long hanSoloTerminate) {
        getInstance().HanSoloTerminate = hanSoloTerminate;
    }

    public static void setC3P0Terminate(long c3P0Terminate) {
        getInstance().C3P0Terminate = c3P0Terminate;
    }

    public static void setR2D2Terminate(long r2D2Terminate) {
        getInstance().R2D2Terminate = r2D2Terminate;
    }

    public static void setLandoTerminate(long landoTerminate) {
        getInstance().LandoTerminate = landoTerminate;
    }

    public static void addAttack() {
        getInstance().totalAttacks.incrementAndGet();
    }

}
