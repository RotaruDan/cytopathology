package es.eucm.cytochallenge.utils;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

/**
 * Some additional actions
 */
public class Actions2 {

    static public MoveUnidimensional moveToX(float x, float duration,
                                             Interpolation interpolation) {
        MoveUnidimensional action = Actions.action(MoveUnidimensional.class);
        action.setPosition(x);
        action.setHorizontal(true);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    static public MoveUnidimensional moveToY(float y, float duration,
                                             Interpolation interpolation) {
        MoveUnidimensional action = Actions.action(MoveUnidimensional.class);
        action.setPosition(y);
        action.setHorizontal(false);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    static public ScaleUnidimensional scaleToX(float scaleX, float duration,
                                               Interpolation interpolation) {
        ScaleUnidimensional action = Actions.action(ScaleUnidimensional.class);
        action.setScale(scaleX);
        action.setHorizontal(true);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    static public ScaleUnidimensional scaleToY(float scaleY, float duration,
                                               Interpolation interpolation) {
        ScaleUnidimensional action = Actions.action(ScaleUnidimensional.class);
        action.setScale(scaleY);
        action.setHorizontal(false);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    public static class MoveUnidimensional extends TemporalAction {
        private boolean horizontal;
        private float start;
        private float end;

        public void setHorizontal(boolean horizontal) {
            this.horizontal = horizontal;
        }

        protected void begin() {
            start = horizontal ? actor.getX() : actor.getY();
        }

        protected void update(float percent) {
            float value = start + (end - start) * percent;
            if (horizontal) {
                actor.setX(value);
            } else {
                actor.setY(value);
            }
        }

        public void setPosition(float value) {
            end = value;
        }
    }

    public static class ScaleUnidimensional extends TemporalAction {
        private boolean horizontal;
        private float start;
        private float end;

        public void setHorizontal(boolean horizontal) {
            this.horizontal = horizontal;
        }

        protected void begin() {
            start = horizontal ? actor.getScaleX() : actor.getScaleY();
        }

        protected void update(float percent) {
            float value = start + (end - start) * percent;
            if (horizontal) {
                actor.setScaleX(value);
            } else {
                actor.setScaleY(value);
            }
        }

        public void setScale(float value) {
            end = value;
        }
    }
}
