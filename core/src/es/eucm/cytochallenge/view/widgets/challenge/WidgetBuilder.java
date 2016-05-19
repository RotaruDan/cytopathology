package es.eucm.cytochallenge.view.widgets.challenge;

import com.badlogic.gdx.scenes.scene2d.Actor;
import es.eucm.cytochallenge.model.Challenge;

/**
 * Created by dan on 02/03/2016.
 */
public interface WidgetBuilder<T> {

    void init(T control);

    Actor getWidget();
}
