package es.eucm.cytochallenge.view.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import es.eucm.cytochallenge.utils.Actions2;


public class ChatDialog extends Container {

    private String text;
    private Label textLabel;
    private float textSpeed, stringCompleteness = 0;

    public ChatDialog(Skin skin) {
        this(skin.get("default", ChatDialogStyle.class));
    }

    public ChatDialog(ChatDialogStyle style) {
        background(style.background);
        setActor(textLabel = new Label("", style.textStyle));
        textLabel.setWrap(true);

        fill();

        textSpeed = 25f;
        pad(WidgetBuilder.dp16ToPixels());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // Then, do this every update
        stringCompleteness += textSpeed * delta;
        // and this when you draw
        int charCountThisFrame = (int) stringCompleteness;
        // Don't go over the length of the string!
        if (charCountThisFrame > text.length()) {
            charCountThisFrame = text.length();
        }

        if(textLabel.getText().length() != charCountThisFrame) {
            textLabel.setText(text.substring(0, charCountThisFrame));
        }
    }

    public void show() {
        stringCompleteness = 0f;
        textLabel.setText("");
        clearActions();
        getColor().a = 0f;
        addAction(Actions.fadeIn(.5f));
    }

    @Override
    public void layout() {
        super.layout();

        if(getPrefHeight() > getHeight()) {
            setHeight(getPrefHeight());
        }
    }

    @Override
    public float getPrefHeight() {
        return super.getPrefHeight();
    }

    public void hide() {
        clearActions();
        addAction(Actions.fadeOut(.5f));
    }

    public void setText(String text) {
        this.text = text;
    }

    public static class ChatDialogStyle {

        public Drawable background;

        public Label.LabelStyle textStyle;


    }
}
