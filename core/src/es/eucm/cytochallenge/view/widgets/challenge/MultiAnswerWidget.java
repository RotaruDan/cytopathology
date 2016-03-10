package es.eucm.cytochallenge.view.widgets.challenge;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import es.eucm.cytochallenge.model.control.MultipleAnswerControl;

/**
 * Created by dan on 04/03/2016.
 */
public class MultiAnswerWidget implements WidgetBuilder<MultipleAnswerControl> {


    private Table root;


    public MultiAnswerWidget() {
        root = new Table();
    }

    @Override
    public void init(MultipleAnswerControl control) {
        root.clear();


    }

    @Override
    public Actor getWidget() {
        return root;
    }
}
