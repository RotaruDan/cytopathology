package es.eucm.cytochallenge.view.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
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
        setTouchable(Touchable.enabled);
        background(style.background);
        backgroundColor(style.color);
        this.setColor(style.color);
    }

    @Override
    protected void drawChildren(Batch batch, float parentAlpha) {
        super.drawChildren(batch, parentAlpha);
        batch.setColor(Color.WHITE);
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