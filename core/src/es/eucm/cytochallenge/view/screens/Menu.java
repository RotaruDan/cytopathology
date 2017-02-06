package es.eucm.cytochallenge.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.transitions.Fade;
import es.eucm.cytochallenge.view.widgets.WidgetBuilder;

public class Menu extends BaseScreen {

    @Override
    public void create() {
        super.create();

        Image logo = new Image(skin, SkinConstants.DRAWABLE_LOGO);
        logo.setScaling(Scaling.fit);

        final Button play, settings;
        play = WidgetBuilder.playButton();
        float yOffset = WidgetBuilder.dp24ToPixels();
        play.addAction(Actions.forever(Actions.sequence(Actions.moveBy(0, -yOffset, 4f, Interpolation.exp10), Actions.moveBy(0, yOffset, 5f, Interpolation.circle))));

        Container<Button> settingsContainer = new Container<Button>();
        settingsContainer.setFillParent(true);
        settings = WidgetBuilder.toolbarIcon(SkinConstants.IC_SETTINGS);
        settingsContainer.setActor(settings);
        settingsContainer.top().right();
        settingsContainer.setTouchable(Touchable.childrenOnly);

        root.addActor(settingsContainer);

        root.defaults().expand();
        root.add(logo);
        root.row();
        root.add(play);

        // Logic control
        // Creating the transition listener
        ClickListener mTransitionListener = new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                final BaseScreen next = getNextScreen(event.getListenerActor());
                if (next == null) {
                    return;
                }

                game.changeScreen(next, Fade.init(1f));
            }

            private BaseScreen getNextScreen(Actor target) {
                BaseScreen next = null;
                if (target == settings) {

                } else if (target == play) {
                    next = lab;
                }
                return next;
            }
        };

        play.addListener(mTransitionListener);
        settings.addListener(mTransitionListener);
    }

    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public void onBackPressed() {
        Gdx.app.exit();
    }

}