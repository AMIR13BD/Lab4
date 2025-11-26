package labapp;

public class LoginState {

    private int failedAttempts = 0;
    private boolean blocked = false;
    private long blockedUntilMillis = 0;

    private final int maxAttempts;
    private final int blockSeconds;

    public LoginState(int maxAttempts, int blockSeconds) {
        this.maxAttempts = maxAttempts;
        this.blockSeconds = blockSeconds;
    }


    public synchronized int registerFailureAndGetRemaining() {
        failedAttempts++;

        if (failedAttempts >= maxAttempts && !blocked) {
            blocked = true;
            blockedUntilMillis = System.currentTimeMillis() + blockSeconds * 1000L;
        }

        int remaining = maxAttempts - failedAttempts;
        return Math.max(0, remaining);
    }

    public synchronized void registerSuccess() {
        failedAttempts = 0;
        blocked = false;
        blockedUntilMillis = 0;
    }


    public synchronized boolean isBlocked() {
        if (!blocked) return false;

        long now = System.currentTimeMillis();
        if (now >= blockedUntilMillis) {
            // blocking time passed – unblock and reset attempts
            blocked = false;
            failedAttempts = 0;
            blockedUntilMillis = 0;
            return false;
        }

        return true;
    }

    public synchronized int getFailedAttempts() {
        return failedAttempts;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public int getBlockSeconds() {
        return blockSeconds;
    }
}
