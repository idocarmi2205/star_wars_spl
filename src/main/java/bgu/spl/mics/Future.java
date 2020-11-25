package bgu.spl.mics;

import java.sql.Time;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A Future object represents a promised result - an object that will
 * eventually be resolved to hold a result of some operation. The class allows
 * Retrieving the result once it is available.
 * <p>
 * Only private methods may be added to this class.
 * No public constructor is allowed except for the empty constructor.
 */
public class Future<T> {
    private final AtomicBoolean isDone;
    private T result;


    /**
     * This should be the the only public constructor in this class.
     */
    public Future() {
        isDone = new AtomicBoolean(false);

    }

    /**
     * retrieves the result the Future object holds if it has been resolved.
     * This is a blocking method! It waits for the computation in case it has
     * not been completed.
     * <p>
     *
     * @return return the result of type T if it is available, if not wait until it is available.
     */
    public T get() {
        while (!isDone.compareAndSet(true, false)) {
        }
        return result;
    }

    /**
     * Resolves the result of this Future object.
     */
    public void resolve(T result) {
        this.result = result;
        isDone.set(true);
    }

    /**
     * @return true if this object has been resolved, false otherwise
     */
    public boolean isDone() {
        return isDone.get();
    }

    /**
     * retrieves the result the Future object holds if it has been resolved,
     * This method is non-blocking, it has a limited amount of time determined
     * by {@code timeout}
     * <p>
     *
     * @param timeout the maximal amount of time units to wait for the result.
     * @param unit    the {@link TimeUnit} time units to wait.
     * @return return the result of type T if it is available, if not,
     * wait for {@code timeout} TimeUnits {@code unit}. If time has
     * elapsed, return null.
     */
    public T get(long timeout, TimeUnit unit) {
        long millis = unit.toMillis(timeout);
        while (!isDone.get() & millis > 0) {
            try {
                //for how long to sleep??
                //is there a better way to do this??
                TimeUnit.MILLISECONDS.sleep(10);
                millis-=10;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return isDone.get() ? result : null;
    }

}
