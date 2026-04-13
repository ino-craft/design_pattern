package hiroshi.state.problem;

public class UrgentState implements State {
    private static UrgentState singleton = new UrgentState();

    private UrgentState() {
    }

    public static State getInstance() {
        return singleton;
    }

    public void doClock(Context context, int hour) {
    }

    public void doUse(Context context) {
        context.callSecurityCenter("Use(UrgentState)!!!!");
    }

    public void doAlarm(Context context) {
        context.callSecurityCenter("Alarm(UrgentState)");
    }

    public void doPhone(Context context) {
        context.callSecurityCenter("Phone(UrgentState)");
    }

    public String toString() {
        return "[UrgentState]";
    }
}
