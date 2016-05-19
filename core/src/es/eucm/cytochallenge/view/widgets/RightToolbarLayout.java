package es.eucm.cytochallenge.view.widgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;


public class RightToolbarLayout extends AbstractWidget {

    private Actor rightToolbar;
    private Actor container;

    @Override
    public void layout() {
        float toolbarWidth = getPrefWidth(rightToolbar);

        setBounds(rightToolbar, getWidth() - toolbarWidth, 0,
                toolbarWidth, getHeight());

        if (rightToolbar instanceof Layout) {
            ((Layout) rightToolbar).validate();
        }
        float width = rightToolbar.getWidth();

        Drawable background = null;

        if (rightToolbar instanceof ScrollPane) {
            ScrollPane rightToolbar = (ScrollPane) this.rightToolbar;
            if (rightToolbar.getWidget() instanceof Table) {
                Table rightToolbarWidget = (Table) rightToolbar.getWidget();
                background = rightToolbarWidget.getBackground();
            } else {
                background = rightToolbar.getStyle().background;
            }
        } else if (rightToolbar instanceof Table) {
            Table rightToolbar = (Table) this.rightToolbar;
            background = rightToolbar.getBackground();
        }

        if (background != null) {
            width -= background.getLeftWidth();
        }

        setBounds(container, 0, 0,
                getWidth() - width, getHeight());
    }

    public void setRightToolbar(Actor rightToolbar) {
        this.rightToolbar = rightToolbar;
        addActor(rightToolbar);
    }

    public void setContainer(Actor container) {
        this.container = container;
        addActorAt(0, container);
    }

    public Actor getContainer() {
        return container;
    }
}
