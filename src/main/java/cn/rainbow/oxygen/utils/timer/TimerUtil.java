package cn.rainbow.oxygen.utils.timer;

public class TimerUtil {
	private long lastMS;

	public TimerUtil() {
		this.lastMS = this.getCurrentMS();
	}

	private long getCurrentMS() {
		return System.nanoTime() / 1000000L;
	}

	public boolean hasReached(double milliseconds) {
		if ((double) (this.getCurrentMS() - this.lastMS) >= milliseconds) {
			return true;
		}
		return false;
	}

	public void reset() {
		this.lastMS = this.getCurrentMS();
	}

	public boolean delay(double delayValue) {
		if ((float) (getTime() - lastMS) >= delayValue) {
			return true;
		}
		return false;
	}

	public static long getTime() {
		return System.nanoTime() / 1000000L;
	}

	public boolean sleep(final long time) {
		if (this.getTime() >= time) {
			reset();
			return true;
		}
		return false;
	}

	public boolean hasTimeElapsed(double time, boolean reset) {
        if (getTime() >= time) {
            if (reset) {
                reset();
            }

            return true;
        }

        return false;
    }

	public boolean isDelayComplete(long delay) {
        if (System.currentTimeMillis() - this.lastMS > delay) {
            return true;
        }
        return false;
    }
}
