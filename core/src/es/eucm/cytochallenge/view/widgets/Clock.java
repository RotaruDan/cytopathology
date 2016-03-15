package es.eucm.cytochallenge.view.widgets;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.screens.BaseScreen;

/**
 * Created by dan on 11/03/2016.
 */
public class Clock extends Image {

    private static final float ROTATION_STEP = 360 / 60f;
    private float rotation, elapsedTime;
    private TextureRegionDrawable axis;

    public Clock(Skin skin) {
        setDrawable(skin.get(SkinConstants.DRAWABLE_CLOCK, Drawable.class));
        axis = new TextureRegionDrawable(skin.get(SkinConstants.DRAWABLE_CLOCK_AXIS, TextureRegion.class));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float halfW = getWidth() * .5f;
        float halfH = getHeight() * .5f;
        axis.draw(batch, getX() + halfW, getY() + halfH,
                0f, 0f,
                axis.getRegion().getRegionWidth(),
                axis.getRegion().getRegionHeight(),
                1f, 1f,
                rotation);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        elapsedTime += delta;
        if (elapsedTime > 1f) {
            elapsedTime = 0f;
            rotation -= ROTATION_STEP;
            if (rotation <= -360) {
                rotation = 0;
            }
        }
    }
}
