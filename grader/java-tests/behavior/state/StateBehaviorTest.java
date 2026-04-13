package hiroshi.state.problem;

public class StateBehaviorTest {
    private static class RecordingContext implements Context {
        private State state = NightState.getInstance();
        private final StringBuilder events = new StringBuilder();

        public void setClock(int hour) {
            state.doClock(this, hour);
        }

        public void changeState(State state) {
            this.state = state;
            events.append("state=").append(state).append('\n');
        }

        public void callSecurityCenter(String msg) {
            events.append("security=").append(msg).append('\n');
        }

        public void recordLog(String msg) {
            events.append("log=").append(msg).append('\n');
        }

        private String stateName() {
            return state.toString();
        }

        private String events() {
            return events.toString();
        }
    }

    public static void main(String[] args) {
        RecordingContext context = new RecordingContext();

        context.setClock(10);
        assertEquals("[Day time]", context.stateName(), "10:00 should switch to DayState");
        context.setClock(22);
        assertEquals("[Night time]", context.stateName(), "22:00 should switch to NightState");

        context.setClock(10);
        context.state.doUse(context);
        context.setClock(22);
        context.state.doPhone(context);

        assertContains(context.events(), "log=Use(Daytime)");
        assertContains(context.events(), "log=Recording Phone(Night time)");

        context.state = DayState.getInstance();
        context.state.doAlarm(context);
        assertStateClass("UrgentState", context.state);

        context.state.doUse(context);
        context.state.doAlarm(context);
        context.state.doPhone(context);

        assertContains(context.events(), "security=Use(UrgentState)!!!!");
        assertContains(context.events(), "security=Alarm(UrgentState)");
        assertContains(context.events(), "security=Phone(UrgentState)");

        System.out.println("PASS state urgent transitions");
    }

    private static void assertEquals(String expected, String actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + ": expected " + expected + ", got " + actual);
        }
    }

    private static void assertContains(String text, String expected) {
        if (!text.contains(expected)) {
            throw new AssertionError("Expected events to contain " + expected + " but got:\n" + text);
        }
    }

    private static void assertStateClass(String expectedSimpleName, State state) {
        if (!state.getClass().getSimpleName().equals(expectedSimpleName)) {
            throw new AssertionError("Expected state " + expectedSimpleName + ", got " + state.getClass().getName());
        }
    }
}
