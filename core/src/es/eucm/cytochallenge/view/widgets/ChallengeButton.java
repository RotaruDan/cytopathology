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
import es.eucm.cytochallenge.utils.Resolver;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.screens.BaseScreen;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ChallengeButton extends TextButton {

    private Label score, time;
    private Image image;
    private Label label;

    public ChallengeButton(Challenge challenge, Skin skin, I18NBundle i18n, Resolver resolver) {
        this(challenge, skin, false, "default", i18n, resolver);
    }

    public ChallengeButton(Challenge challenge, Skin skin, boolean isCourse, String styleName, I18NBundle i18n, Resolver resolver) {
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
        score = new Label(Grades.getGrade(finalScore) + "", skin);
        time = null;
        if (isCourse) {
            long timeValue = BaseScreen.prefs.getChallengeTime(challenge.getId());
            String timeRes;
            if (timeValue == 0l) {
                timeRes = i18n.get("time");
            } else {
                Date date = new Date(timeValue);
                String dateFormatted = resolver.format("mm:ss", date);
                timeRes = dateFormatted;
            }
            time = new Label(timeRes + "", skin);
        }

        add(image).width(image.getWidth());
        add(difficulty).width(Math.max(difficulty.getWidth(), Gdx.graphics.getWidth() * .10f));
        add(label).expandX().left(). expandX();
        add(score).width(image.getWidth());
        if(time != null) {
            add(time).width(image.getWidth() * 2f);
        }
        setSize(getPrefWidth(), getPrefHeight());
    }

    @Override
    public void layout() {
        super.layout();
        float scoreWidth = image.getWidth() * (time == null ? 1.8f : 4.5f);
        if (label.getWidth() + label.getX() + scoreWidth >= getWidth()) {
            label.setWidth(getWidth() - label.getX() - scoreWidth);
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
