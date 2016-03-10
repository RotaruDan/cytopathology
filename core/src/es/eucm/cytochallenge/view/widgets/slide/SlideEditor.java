package es.eucm.cytochallenge.view.widgets.slide;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import es.eucm.cytochallenge.utils.Actions2;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.widgets.AbstractWidget;
import es.eucm.cytochallenge.view.widgets.WidgetBuilder;
import es.eucm.cytochallenge.view.widgets.slide.input.EditStateMachine;


public class SlideEditor extends AbstractWidget {

    public static final float ZOOM_TIME = 0.3f;

    public static final Vector2 tmp = new Vector2();

    public static final float NEAR_CM = 1.0f;

    private SlideEditorStyle style;

    private Actor rootActor;

    private AbstractWidget slideContainer;

    private Image slideBackground;

    private ImageButton fitButton;

    protected float zoom = 1.0f, fitZoom, maxZoom, minZoom;

    public SlideEditor(Skin skin) {
        this(skin.get(SlideEditorStyle.class));
    }

    public SlideEditor(SlideEditorStyle style) {
        this.style = style;

        addActor(slideContainer = new AbstractWidget());

        slideBackground = new Image(style.slideBackground);
        slideContainer.addActor(slideBackground);

        TouchRepresentation touchRepresentation = new TouchRepresentation(
                style.touch);
        addActor(touchRepresentation);

        addListener(touchRepresentation);


        fitButton = WidgetBuilder.imageButton(SkinConstants.IC_FIT,
                SkinConstants.STYLE_SECONDARY_CIRCLE);
        fitButton.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                event.cancel();
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                fit(true);
                fitButton.setVisible(false);
            }
        });
        fitButton.setVisible(false);
        addActor(fitButton);

        addListener(new EditStateMachine(this));
    }

    /**
     * Sets the group being edited
     */
    public void setRootActor(Actor rootActor) {
        if (this.rootActor != null) {
            this.rootActor.remove();
        }
        this.rootActor = rootActor;
        if (rootActor != null) {

            slideBackground.setBounds(
                    -style.slideBackground.getLeftWidth(),
                    -style.slideBackground.getBottomHeight(),
                    rootActor.getWidth() + style.slideBackground.getLeftWidth()
                            + style.slideBackground.getRightWidth(),
                    rootActor.getHeight()
                            + style.slideBackground.getTopHeight()
                            + style.slideBackground.getBottomHeight());

            slideContainer.addActorAfter(slideBackground, rootActor);
            invalidate();
        }
    }

    public void enterFullScreen() {
        Gdx.app.log(getClass().getSimpleName(), "enterFullScreen");
    }

    public void exitFullScreen() {
        Gdx.app.log(getClass().getSimpleName(), "exitFullScreen");
    }

    public void fireContainerUpdated() {

    }

    // Invoke by UI (Requires fireContainerUpdated)

    /**
     * Sets the root group in its initial position, fitting the view
     */
    public void fit(boolean animated) {
        float x = (getWidth() - rootActor.getWidth() * fitZoom) * .5f;
        float y = (getHeight() - rootActor.getHeight() * fitZoom) * .5f;
        if (animated) {
            slideContainer.addAction(new SequenceAction(new ParallelAction(
                    Actions.moveTo(x, y,
                            ZOOM_TIME,
                            Interpolation.exp5Out), Actions
                    .scaleTo(fitZoom, fitZoom, ZOOM_TIME,
                            Interpolation.exp5Out))));
        } else {
            slideContainer.setPosition(x, y);
            slideContainer.setScale(fitZoom, fitZoom);
        }
        zoom = fitZoom;
    }

    // Invoked programmatically

    public void pan(float deltaX, float deltaY, boolean animated) {
        panToX(slideContainer.getX() + deltaX, animated);
        panToY(slideContainer.getY() + deltaY, animated);
        fitButton.setVisible(true);
    }

    public void panToX(float x, boolean animated) {
        if (animated) {
            slideContainer.addAction(Actions2.moveToX(x, ZOOM_TIME,
                    Interpolation.exp5Out));
        } else {
            slideContainer.setX(x);
        }
    }

    public void panToY(float y, boolean animated) {
        if (animated) {
            slideContainer.addAction(Actions2.moveToY(y, ZOOM_TIME,
                    Interpolation.exp5Out));
        } else {
            slideContainer.setY(y);
        }
    }

    public float getZoom() {
        return zoom;
    }

    public void zoomIn() {
        zoom(getWidth() / 2.0f, getHeight() / 2.0f, zoom + 0.25f, true);
    }

    public void zoomOut() {
        zoom(getWidth() / 2.0f, getHeight() / 2.0f, zoom - 0.25f, true);
    }

    /**
     * Changes the zoom level
     */
    public void zoom(float centerX, float centerY, float newZoom,
                     boolean animate) {

        if (MathUtils.isEqual(zoom, newZoom, 0.001f)) {
            return;
        }

        this.zoom = Math.min(maxZoom, Math.max(minZoom, newZoom));
        Vector2 center = tmp.set(centerX, centerY);
        localToDescendantCoordinates(slideContainer, center);

        float oldScale = slideContainer.getScaleX();
        slideContainer.setScale(zoom, zoom);

        slideContainer.localToAscendantCoordinates(this, center);

        float newX = slideContainer.getX() + (centerX - center.x);
        float newY = slideContainer.getY() + (centerY - center.y);

        if (animate) {
            slideContainer.setScale(oldScale, oldScale);
            slideContainer.getActions().clear();
            slideContainer.addAction(Actions.sequence(Actions.parallel(
                    Actions.scaleTo(zoom, zoom, ZOOM_TIME, Interpolation.exp5Out),
                    Actions.moveTo(newX, newY, ZOOM_TIME, Interpolation.exp5Out))));
        } else {
            slideContainer.setPosition(newX, newY);
        }
        fitButton.setVisible(true);
    }

    public void setZoom(float zoom, boolean animate) {
        this.zoom = zoom;
        if (animate) {
            slideContainer.addAction(Actions.scaleTo(zoom, zoom, ZOOM_TIME,
                    Interpolation.exp5Out));
        } else {
            slideContainer.setScale(zoom, zoom);
        }
    }

    @Override
    public void layout() {
        Vector2 fitSize = Scaling.fit.apply(rootActor.getWidth(),
                rootActor.getHeight(), getWidth(), getHeight());
        this.fitZoom = fitSize.x / rootActor.getWidth();
        this.maxZoom = fitZoom * 50f;
        this.minZoom = fitZoom * 0.5f;
        fitButton.pack();
        float offset = WidgetBuilder.dpToPixels(16);
        setPosition(fitButton, offset,
                offset);
        fit(false);
    }

    @Override
    protected void drawChildren(Batch batch, float parentAlpha) {
        batch.setColor(Color.WHITE);
        if (style.background != null) {
            style.background.draw(batch, 0, 0, getWidth(), getHeight());
        }
        super.drawChildren(batch, parentAlpha);
    }

    public AbstractWidget getSlideContainer() {
        return slideContainer;
    }

    public Actor getRootActor() {
        return rootActor;
    }

    public static class SlideEditorStyle {

        public Drawable background;

        public Drawable slideBackground;

        public Drawable touch;

    }
}
