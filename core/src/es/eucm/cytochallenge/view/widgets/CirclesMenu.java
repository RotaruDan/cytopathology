package es.eucm.cytochallenge.view.widgets;


import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;


public class CirclesMenu extends Table {

    private static final float TIME = 0.22f;


    /**
     *
     * @param align
     *            It indicates where the menu aligns when is opened/closed.
     *            {@link Align#left} or {@link Align#right} will create an
     *            horizontal menu. {@link Align#top} and {@link Align#bottom}
     *            will create a vertical menu. Other values are not supported
     */
    public CirclesMenu(int align) {
        align(align);
    }

    public void show() {
        for (Actor child : getChildren()) {
            child.clearActions();
        }
        pack();
        layout();
        for (Actor child : getChildren()) {
            float x = child.getX();
            float y = child.getY();
                switch (getAlign()) {
                    case Align.right:
                        child.setPosition(getWidth() - child.getWidth(), 0);
                        break;
                    default:
                        child.setPosition(0, 0);
                        break;
                }
            child.addAction(Actions.moveTo(x, y, TIME, Interpolation.swingOut));
        }
    }

    public void hide(Runnable runnable) {
        for (Actor child : getChildren()) {
            child.clearActions();
            float x = 0;
            float y = 0;
            if (getAlign() == Align.right) {
                x = getWidth() - child.getWidth();
            }
            child.addAction(Actions.moveTo(x, y, TIME / 2.0f,
                    Interpolation.exp5Out));
        }

        if (runnable != null) {
            addAction(Actions.sequence(Actions.delay(TIME / 2.0f),
                    Actions.run(runnable)));
        }
    }


}