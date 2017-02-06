package es.eucm.cytochallenge.view.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class Toast extends LinearLayout {

    private Label label;

    public Toast(Skin skin) {
        this(skin.get(ToastStyle.class));
    }

    public Toast(Skin skin, String style) {
        this(skin.get(style, ToastStyle.class));
    }

    public Toast(ToastStyle style) {
        super(true);
        add(label = new Label("", style.label)).centerY().centerX();
        setTouchable(Touchable.disabled);
        background(style.background);
        backgroundColor(style.color);
        this.setColor(style.color);
        pad(WidgetBuilder.dp8ToPixels());
    }

    @Override
    protected void drawChildren(Batch batch, float parentAlpha) {
        super.drawChildren(batch, parentAlpha);
        batch.setColor(Color.WHITE);
    }

    public void show() {
        pack();
        float y = 0;
        setY(-getHeight());
        setX((Gdx.graphics.getWidth() - getWidth()) * .5f);
        clearActions();
        addAction(Actions.sequence(Actions.moveTo(getX(), y, 0.33f, Interpolation.exp5Out), Actions.delay(2f, hide())));
    }

    private Action hide() {
        return Actions.sequence(Actions.moveTo(getX(), -getHeight(),
                0.33f, Interpolation.exp5Out), Actions.run(new Runnable() {
            @Override
            public void run() {
                remove();
            }
        }));
    }

    public void setText(String text) {
        label.setText(text);
    }

    public static class ToastStyle {

        public Drawable background;

        public Color color;

        public LabelStyle label;

    }

}