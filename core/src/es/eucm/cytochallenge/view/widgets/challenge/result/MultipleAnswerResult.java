package es.eucm.cytochallenge.view.widgets.challenge.result;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import es.eucm.cytochallenge.model.control.MultipleAnswerControl;
import es.eucm.cytochallenge.utils.Grades;
import es.eucm.cytochallenge.view.SkinConstants;

public class MultipleAnswerResult extends ResultLayout<MultipleAnswerControl> {


    public MultipleAnswerResult(Skin skin,
                                MultipleAnswerControl control,
                                I18NBundle i18n,
                                ButtonGroup selectedGroup, Actor right) {
        super(skin, control, i18n, selectedGroup, right);
    }

    @Override
    protected Label buildLabel(MultipleAnswerControl control, Object... args) {
        score = 0;
        String correctAnswer = null;
        String selectedAnswer = null;
        if (control.getCorrectAnswer() <= control.getAnswers().length - 1) {
            correctAnswer = control.getAnswers()[control.getCorrectAnswer()];
        }
        Button checked = ((ButtonGroup) args[0]).getChecked();
        if (checked != null) {
            selectedAnswer = checked.getUserObject().toString();
        }

        if (correctAnswer != null && correctAnswer.equals(selectedAnswer)) {
            score = 100;
        }

        String answer = Grades.getGrade(score);
        Label resultsLabel = new Label(answer, getSkin(), SkinConstants.STYLE_TOAST);
        resultsLabel.setAlignment(Align.center);
        return resultsLabel;
    }

    @Override
    protected Actor[] buildTabs(MultipleAnswerControl control, Object... args) {
        Actor right = (Actor) args[1];
        return new Actor[]{
                right
        };
    }
    protected String[] buildTabNames(I18NBundle i18n) {
        return new String[]{
                i18n.get("result")
        };
    }
}
