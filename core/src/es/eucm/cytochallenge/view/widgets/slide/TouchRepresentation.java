package es.eucm.cytochallenge.view.widgets.slide;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import es.eucm.cytochallenge.view.widgets.AbstractWidget;

public class TouchRepresentation extends AbstractWidget implements
        EventListener {

    private Image touch1;

    private Image touch2;

    private InputListener inputListener = new InputListener() {

        @Override
        public boolean touchDown(InputEvent event, float x, float y,
                                 int pointer, int button) {
            if (pointer < 2) {
                Image touch = pointer == 0 ? touch1 : touch2;
                touch.clearActions();
                touch.setScale(0, 0);
                touch.setVisible(true);
                touch.addAction(Actions.sequence(Actions.alpha(1.0f), Actions
                        .scaleTo(1.0f, 1.0f, 0.25f, Interpolation.exp5Out)));
                touch.setPosition(x - touch.getWidth() / 2.0f,
                        y - touch.getHeight() / 2.0f);
            }
            return pointer < 2;
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            if (pointer < 2) {
                Image touch = pointer == 0 ? touch1 : touch2;
                touch.setPosition(x - touch.getWidth() / 2.0f,
                        y - touch.getHeight() / 2.0f);
            }
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer,
                            int button) {
            if (pointer < 2) {
                Image touch = pointer == 0 ? touch1 : touch2;
                touch.addAction(Actions.sequence(
                        Actions.alpha(0, 0.5f, Interpolation.exp5Out),
                        Actions.hide()));
            }
        }
    };

    public TouchRepresentation(Drawable touch) {
        setTouchable(Touchable.disabled);
        addActor(touch1 = new Image(touch));
        addActor(touch2 = new Image(touch));
        touch1.setVisible(false);
        touch1.setTouchable(Touchable.disabled);
        touch1.pack();
        touch1.setOrigin(touch1.getWidth() / 2.0f, touch1.getHeight() / 2.0f);
        touch2.setVisible(false);
        touch2.setTouchable(Touchable.disabled);
        touch2.pack();
        touch2.setOrigin(touch2.getWidth() / 2.0f, touch2.getHeight() / 2.0f);
    }

    @Override
    public boolean handle(Event event) {
        return inputListener.handle(event);
    }
}
