package me.Oxygen.utils.timer;


public class Timer {
    private long lastMS;

    public void reset() {
        this.lastMS = this.getTime();
    }

    public boolean delay(float milliseconds) {
        if ((float)(this.getTime() - this.lastMS) >= milliseconds) {
            return true;
        }
        return false;
    }

    public long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public boolean isDelayComplete(long delay) {
        if (System.currentTimeMillis() - this.lastMS > delay) {
            return true;
        }
        return false;
    }
    
    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasReached(long milliseconds) {
        return this.getCurrentMS() - this.lastMS >= milliseconds;
    }
    
    public boolean hasReached(float timeLeft) {
        return (float)(this.getCurrentMS() - this.lastMS) >= timeLeft;
    }

    public boolean check(float milliseconds) {
        return getTime() >= milliseconds;
    }

	public boolean hasReached(double milliseconds) {
        if ((double)(this.getCurrentMS() - this.lastMS) >= milliseconds) {
            return true;
        }
        return false;
    }
}