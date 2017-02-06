package es.eucm.cytochallenge.view.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
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
    private Image image, star1, star2, star3;
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
        float defaultPad = WidgetBuilder.dp16ToPixels();

        pad(defaultPad);
        left();
        defaults().space(defaultPad);

        Label difficulty = WidgetBuilder.difficulty(challenge.getDifficulty(), i18n);

        float finalScore = BaseScreen.prefs.getChallengeScore(challenge.getId());
        score = new Label(Grades.getGrade(finalScore) + "", skin);
        time = null;

        boolean disabled = true;
        long timeValue = BaseScreen.prefs.getChallengeTime(challenge.getId());
        if (timeValue > 0l) {
            disabled = false;
        }
        if (isCourse) {
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
        setDisabled(disabled);
        if(disabled) {
            difficulty.setColor(Color.LIGHT_GRAY);
            score.setColor(Color.LIGHT_GRAY);
            label.setColor(Color.LIGHT_GRAY);
            if (time != null) {
                time.setColor(Color.LIGHT_GRAY);
            }
        }

        add(image).width(image.getWidth());
        add(difficulty).width(Math.max(difficulty.getWidth(), Gdx.graphics.getWidth() * .10f));
        add(label).expandX().left().expandX();
        add(score).width(image.getWidth());
        if (time != null) {
            add(time).width(image.getWidth() * 2f);
        }
        setSize(getPrefWidth(), getPrefHeight());

        setUpStars(skin, finalScore);
    }

    private void setUpStars(Skin skin, float finalScore) {

        Drawable starDrawable = skin.getDrawable(SkinConstants.DRAWABLE_SMALL_STAR);
        int stars = Grades.getStars(finalScore);

        if (stars > 2) {
            star3 = new Image(starDrawable);
            star3.setColor(Color.YELLOW);
            star3.setOrigin(Align.center);
            star3.setX(-100f);
            star3.addAction(Actions.sequence(Actions.delay(1.2f), Actions.moveTo(star3.getWidth() * 2f, 0, .75f, Interpolation.swingOut)));
            addActor(star3);

        }
        if (stars > 1) {
            star2 = new Image(starDrawable);
            star2.setColor(Color.YELLOW);
            star2.setOrigin(Align.center);
            star2.setX(-100f);
            star2.addAction(Actions.sequence(Actions.delay(.6f), Actions.moveTo(star2.getWidth(), 0, .75f, Interpolation.swingOut)));
            addActor(star2);

        }
        if (stars > 0) {
            star1 = new Image(starDrawable);
            star1.setColor(Color.YELLOW);
            star1.setOrigin(Align.center);
            star1.setX(-100f);
            star1.addAction(Actions.moveTo(0, 0, .75f, Interpolation.swingOut));
            addActor(star1);

        }
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
        if (!isDisabled()) {
            if (isPressed())
                image.setColor(Color.WHITE);
            else
                image.setColor(SkinConstants.COLOR_BUTTON);
        } else {
            image.setColor(Color.LIGHT_GRAY);
        }
        super.draw(batch, parentAlpha);
    }

    @Override
    public void setDisabled(boolean isDisabled) {
        super.setDisabled(isDisabled);
    }
}
