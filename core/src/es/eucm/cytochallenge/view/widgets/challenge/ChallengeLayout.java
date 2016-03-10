package es.eucm.cytochallenge.view.widgets.challenge;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import es.eucm.cytochallenge.view.widgets.AbstractWidget;

public class ChallengeLayout extends AbstractWidget {

    Button backButton, checkButton;
    Actor content;

    @Override
    public void layout() {
        float backHeight = getPrefHeight(backButton);

        setBounds(backButton, 0, getHeight() - backHeight,
                getPrefWidth(backButton), backHeight);

        float checkWidth = getPrefWidth(checkButton);
        setBounds(checkButton, getWidth() - checkWidth, 0,
                checkWidth, getPrefHeight(checkButton));

        setBounds(content, 0, 0, getWidth(), getHeight());

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
    }
}
