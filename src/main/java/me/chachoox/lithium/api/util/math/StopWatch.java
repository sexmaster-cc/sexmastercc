package me.chachoox.lithium.api.util.math;

public class StopWatch implements Passable {
    private volatile long time;

    public boolean passed(double ms) {
        return System.currentTimeMillis() - time >= ms;
    }

    @Override
    public boolean passed(long ms) {
        return System.currentTimeMillis() - time >= ms;
    }

    public StopWatch reset() {
        time = System.currentTimeMillis();
        return this;
    }

    public boolean sleep(final long time) {
        if (getTime() >= time) {
            reset();
            return true;
        }
        return false;
    }

    public long getTime() {
        return System.currentTimeMillis() - time;
    }

    public void setTime(long ns) {
        time = ns;
    }

}
