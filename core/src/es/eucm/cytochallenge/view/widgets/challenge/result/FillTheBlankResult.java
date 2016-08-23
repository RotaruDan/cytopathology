package es.eucm.cytochallenge.view.widgets.challenge.result;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import es.eucm.cytochallenge.model.control.MultipleAnswerControl;
import es.eucm.cytochallenge.model.control.filltheblank.FillTheBlankControl;
import es.eucm.cytochallenge.model.control.filltheblank.FillTheBlankStatement;
import es.eucm.cytochallenge.utils.Grades;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.widgets.TopToolbarLayout;
import es.eucm.cytochallenge.view.widgets.challenge.filltheblank.FillTheBlankText;

public class FillTheBlankResult extends ResultLayout<FillTheBlankControl> {


    public FillTheBlankResult(Skin skin,
                              FillTheBlankControl control,
                              I18NBundle i18n,
                              Array<FillTheBlankText> ftbTexts, Actor right) {
        super(skin, control, i18n, ftbTexts, right);
    }

    // FillTheBlankControl
    private int checkFtBResult(Array<FillTheBlankText> ftbTexts) {
        int correctCount = 0;
        for (int i = 0; i < ftbTexts.size; i++) {
            FillTheBlankText child = ftbTexts.get(i);

            correctCount += child.getCorrectAnswers();
        }
        return correctCount;
    }

    private int checkFtBTotalCount(Array<FillTheBlankText> ftbTexts) {
        int correctCount = 0;
        for (int i = 0; i < ftbTexts.size; i++) {
            FillTheBlankText child = ftbTexts.get(i);

            correctCount += child.getTotalAnswers();
        }
        return correctCount;
    }

    @Override
    protected Label buildLabel(FillTheBlankControl control, Object... args) {
        Array<FillTheBlankText> ftbTexts = (Array<FillTheBlankText>) args[0];
        int results = checkFtBResult(ftbTexts);
        int total = checkFtBTotalCount(ftbTexts);
        score = results / (float) total * 100;

        Label resultsLabel = new Label(Grades.getGrade(score) +
                "    " + results + "/" + total, getSkin(), SkinConstants.STYLE_TOAST);
        resultsLabel.setAlignment(Align.center);
        return resultsLabel;
    }

    @Override
    protected Actor[] buildTabs(FillTheBlankControl control, Object... args) {
        Actor right = (Actor) args[1];

        Table verticalLayout = new Table();
        verticalLayout.setTouchable(Touchable.disabled);
        verticalLayout.pad(es.eucm.cytochallenge.view.widgets.WidgetBuilder.dpToPixels(48f));
        FillTheBlankStatement[] statements = control.getStatements();

        for (int i = 0; i < statements.length; i++) {
            FillTheBlankStatement statement = statements[i];

            FillTheBlankText fillText = new FillTheBlankText(getSkin());
            fillText.init(statement);
            fillText.setCorrectAnswers();

            verticalLayout.add(fillText).expandX().left();
            verticalLayout.row();
        }

        ScrollPane scroll = new ScrollPane(verticalLayout);
        scroll.setScrollingDisabled(true, false);

        Label label = new Label(control.getText(), getSkin(),
                SkinConstants.STYLE_TOOLBAR);
        label.setAlignment(Align.center);

        ScrollPane horizontalScroll = new ScrollPane(label);
        horizontalScroll.setScrollingDisabled(false, true);

        addDescription(horizontalScroll);

        return new Actor[]{
                right,
                scroll
        };
    }
}
