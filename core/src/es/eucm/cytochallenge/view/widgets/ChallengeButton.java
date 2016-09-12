package es.eucm.cytochallenge.view.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Scaling;
import es.eucm.cytochallenge.model.Challenge;
import es.eucm.cytochallenge.model.Difficulty;
import es.eucm.cytochallenge.model.TextChallenge;
import es.eucm.cytochallenge.model.control.InteractiveZoneControl;
import es.eucm.cytochallenge.model.control.MultipleAnswerControl;
import es.eucm.cytochallenge.model.control.MultipleImageAnswerControl;
import es.eucm.cytochallenge.model.control.TextControl;
import es.eucm.cytochallenge.model.control.draganddrop.DragAndDropControl;
import es.eucm.cytochallenge.model.control.filltheblank.FillTheBlankControl;
import es.eucm.cytochallenge.utils.Grades;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.screens.BaseScreen;


public class ChallengeButton extends TextButton {

    private final Image image;
    private final Label label;

    public ChallengeButton(Skin skin, I18NBundle i18n) {
        this(null, skin, i18n);
    }

    public ChallengeButton(Challenge challenge, Skin skin, I18NBundle i18n) {
        this(challenge, skin, false, "default", i18n);
    }

    public ChallengeButton(Challenge challenge, Skin skin, boolean isCourse, String styleName, I18NBundle i18n) {
        super("", skin, styleName);


        TextControl textControl = ((TextChallenge) challenge).getTextControl();
        setText(textControl.getText());

        label = getLabel();
        label.setEllipsis(true);
        label.setEllipsis("...");
        label.setAlignment(Align.left);

        String imageIcon = "";
        if (textControl instanceof MultipleAnswerControl) {
            imageIcon = SkinConstants.IC_MCQ;
        } else if (textControl instanceof DragAndDropControl) {
            imageIcon = SkinConstants.IC_DND;
        } else if (textControl instanceof FillTheBlankControl) {
            imageIcon = SkinConstants.IC_FTB;
        } else if (textControl instanceof MultipleImageAnswerControl) {
            imageIcon = SkinConstants.IC_MICQ;
        } else if (textControl instanceof InteractiveZoneControl) {
            imageIcon = SkinConstants.IC_ARROW;
        }
        image = new Image(skin.getDrawable(imageIcon));
        image.setScaling(Scaling.fit);

        clearChildren();
        float defaultPad = WidgetBuilder.dpToPixels(24f);

        pad(defaultPad);
        left();
        defaults().space(defaultPad);

        Label difficulty = WidgetBuilder.difficulty(challenge.getDifficulty(), i18n);

        float finalScore = BaseScreen.prefs.getChallengeScore(challenge.getId());
        Label score = new Label(Grades.getGrade(finalScore) + "", skin);

        add(image).width(image.getWidth());
        add(difficulty).width(Math.max(difficulty.getWidth(), Gdx.graphics.getWidth() * .12f));
        add(label).expandX().left();
        add(score).width(image.getWidth());
        setSize(getPrefWidth(), getPrefHeight());
    }

    @Override
    public void layout() {
        super.layout();
        if (label.getWidth() + label.getX() + image.getWidth()* 1.8f >= getWidth()) {
            label.setWidth(getWidth() - label.getX() - image.getWidth() * 1.8f);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isPressed())
            image.setColor(Color.WHITE);
        else
            image.setColor(SkinConstants.COLOR_BUTTON);
        super.draw(batch, parentAlpha);
    }
}
