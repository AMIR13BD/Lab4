package labapp;

public class AttemptsUpdater extends Thread {

    private final LoginState state;
    private int remainingAfter;  // attempts remaining AFTER this failure

    public AttemptsUpdater(LoginState state) {
        this.state = state;
    }

    @Override
    public void run() {
        // this increments attempts and returns how many are left
        remainingAfter = state.registerFailureAndGetRemaining();
    }

    public int getRemainingAfter() {
        return remainingAfter;
    }
}
