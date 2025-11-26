package labapp;

public class BlockWatcher extends Thread {

    private final LoginState state;

    public BlockWatcher(LoginState state) {
        this.state = state;
        setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            // this call auto-unblocks when the time passes
            state.isBlocked();

            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) { }
        }
    }
}
