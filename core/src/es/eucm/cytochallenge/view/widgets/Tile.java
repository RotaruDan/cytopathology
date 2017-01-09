package es.eucm.cytochallenge.view.widgets;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class Tile extends AbstractWidget {

    protected Actor background, bottom, marker;

    protected Label label;

    protected Table labelContainer;

    private TileStyle style;

    private ClickListener clickListener = new ClickListener();

    public Tile(Skin skin) {
        this(skin.get(TileStyle.class));
    }

    public Tile(TileStyle tileStyle) {
        label = new Label("", tileStyle.labelStyle);
        labelContainer = new Table();
        labelContainer.add(label).expandX().fillX();
        label.setEllipsis(true);
        this.style = tileStyle;
        labelContainer.setBackground(tileStyle.labelBackground);
        labelContainer.pad(WidgetBuilder.dpToPixels(8));
        labelContainer.left();
        addActor(labelContainer);
        addListener(clickListener);

        if (tileStyle.background != null) {
            Image bg = new Image(tileStyle.background);
            setBackground(bg);
        }
    }

    public void setBackground(Actor actor) {
        if (this.background != null) {
            this.background.remove();
        }
        if (actor != null) {
            addActorAt(0, actor);
        }
        this.background = actor;
    }

    public void setBottom(Actor actor) {
        if (bottom == actor) {
            return;
        }
        if (this.bottom != null) {
            this.bottom.remove();
        }
        if (actor != null) {
            addActorAt(2, actor);
        }
        this.bottom = actor;
    }

    public void setMarker(Actor actor) {
        if (this.marker != null) {
            this.marker.remove();
        }

        if (actor != null) {
            addActor(actor);
        }
        this.marker = actor;
    }

    public Actor getBackground() {
        return background;
    }

    public void setText(String text) {
        if (text == null || text.isEmpty()) {
            labelContainer.setVisible(false);
        } else {
            labelContainer.setVisible(true);
            label.setText(text);
        }
    }

    @Override
    public float getPrefWidth() {
        return getPrefWidth(background);
    }

    @Override
    public float getPrefHeight() {
        return getPrefHeight(background);
    }

    @Override
    protected void drawChildren(Batch batch, float parentAlpha) {
        super.drawChildren(batch, parentAlpha);
        if (clickListener.isPressed() && style.pressed != null) {
            style.pressed.draw(batch, 0, 0, getWidth(), getHeight());
        }
    }

    @Override
    public void layout() {
        float width = getWidth();
        float height = getHeight();
        if (background != null) {
            setBounds(background, 0, 0, width, height);
        }
        setBounds(labelContainer, 0, 0, width, getPrefHeight(labelContainer));
        //labelContainer.getCell(label).width(labelContainer.getWidth() * .95f);
        if (bottom != null) {
            float topPrefH = getPrefHeight(bottom);
            setBounds(bottom, 0, 0, width, topPrefH);
        }
        if (marker != null) {
            float markerPrefW = getPrefWidth(marker);
            float markerPrefH = getPrefHeight(marker);
            setBounds(marker, width - markerPrefW, height - markerPrefH,
                    markerPrefW, markerPrefH);
        }
    }

    public static class TileStyle {

        public Drawable labelBackground;

        public Drawable pressed;

        public LabelStyle labelStyle;

        public Drawable background;

    }
}