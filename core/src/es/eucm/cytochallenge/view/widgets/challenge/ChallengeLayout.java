package es.eucm.cytochallenge.view.widgets.challenge;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.TimerWidget;
import es.eucm.cytochallenge.view.widgets.*;
import es.eucm.cytochallenge.view.widgets.WidgetBuilder;

public class ChallengeLayout extends AbstractWidget {

    Button backButton, nextChallenge, checkButton, showProgressBar;
    Actor content;
    TimerWidget timer;

    @Override
    public void layout() {
        float backHeight = getPrefHeight(backButton);

        setBounds(backButton, 0, getHeight() - backHeight,
                getPrefWidth(backButton), backHeight);

        float checkWidth = getPrefWidth(checkButton);
        if(nextChallenge != null) {

            float nextWidth = getPrefWidth(nextChallenge);
            float offset = es.eucm.cytochallenge.view.widgets.WidgetBuilder.dpToPixels(16);
            setBounds(nextChallenge, getWidth() - nextWidth - offset, offset * 2 + checkWidth,
                    nextWidth, getPrefHeight(nextChallenge));
        }

        float offset = es.eucm.cytochallenge.view.widgets.WidgetBuilder.dpToPixels(16);
        setBounds(checkButton, getWidth() - checkWidth - offset, offset,
                checkWidth, getPrefHeight(checkButton));

        if(content != null) {
            setBounds(content, 0, 0, getWidth(), getHeight());
        }

        if(timer != null) {
            float timerPrefHeight = getPrefHeight(timer);
            setBounds(timer, 0, backButton.getY() - timerPrefHeight, getPrefWidth(timer), timerPrefHeight);
        }

        if(showProgressBar != null) {
            setBounds(showProgressBar, 0, 0, getPrefWidth(showProgressBar), getPrefHeight(showProgressBar));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public Button getBackButton() {
        return backButton;
    }

    public void setBackButton(Button backButton) {
        this.backButton = backButton;
        addActor(backButton);
    }

    public Button getCheckButton() {
        return checkButton;
    }

    public void setCheckButton(Button checkButton) {
        this.checkButton = checkButton;
        addActor(checkButton);
    }

    public void setNextChallenge(Button nextChallenge) {
        if(this.nextChallenge != null) {
            this.nextChallenge.remove();
        }
        this.nextChallenge = nextChallenge;
        if(nextChallenge != null) {
            addActor(nextChallenge);
        }
    }

    public Actor getContent() {
        return content;
    }

    public void setContent(Actor content) {
        if(this.content != null) {
            removeActor(this.content);
        }
        this.content = content;
        addActor(content);
        content.toBack();
        invalidateHierarchy();
    }

    public void setTimer(TimerWidget timer) {
        if(this.timer != null) {
            this.timer.remove();
        }
        this.timer = timer;
        if(timer != null) {
            addActor(timer);
        }
    }

    public void setShowProgressBar(Button showProgressBar) {
        if(this.showProgressBar != null) {
            this.showProgressBar.remove();
        }
        this.showProgressBar = showProgressBar;
        if(showProgressBar != null) {
            addActor(showProgressBar);
        }
    }

    public TimerWidget getTimer() {
        return timer;
    }
}
