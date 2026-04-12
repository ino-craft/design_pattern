package hiroshi.mediatorProblem;
import java.awt.Button;

public class ColleagueButton extends Button implements Colleague {
    private Mediator mediator;
    public ColleagueButton(String caption) {
        super(caption);
    }
    public void setMediator(Mediator mediator) {            // Mediator을 저장
        this.mediator = mediator;
    }
    public void setColleagueEnabled(boolean enabled) {      // Mediator에서 유효/무효를 지시
        setEnabled(enabled);
    }
}
