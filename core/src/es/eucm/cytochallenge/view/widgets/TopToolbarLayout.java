package es.eucm.cytochallenge.view.widgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import es.eucm.cytochallenge.view.widgets.AbstractWidget;


public class TopToolbarLayout extends AbstractWidget {

    private Table topToolbar;
    private Actor container;
    private Actor check;

    @Override
    public void layout() {
        float toolbarHeight = getPrefHeight(topToolbar);

        setBounds(topToolbar, 0, getHeight() - toolbarHeight,
                getWidth(), toolbarHeight);
        topToolbar.validate();

        Drawable background = topToolbar.getBackground();

        float height = topToolbar.getHeight();
        if(background != null) {
            height -= background.getBottomHeight();
        }

        setBounds(container, 0, 0,
                getWidth(), getHeight() - height);

        if(check != null) {
            float offset = es.eucm.cytochallenge.view.widgets.WidgetBuilder.dpToPixels(16);
            setBounds(check, getWidth() - getPrefWidth(check) - offset, offset,
                    getPrefWidth(check), getPrefHeight(check));
        }
    }

    public void setTopToolbar(Table topToolbar) {
        this.topToolbar = topToolbar;
        addActor(topToolbar);
    }

    public void setContainer(Actor container) {
        this.container = container;
        addActorAt(0, container);
    }

    public void setCheckButton(Button checkButton) {
        this.check = checkButton;
        addActor(checkButton);
    }

    public Actor getContainer() {
        return container;
    }
}
