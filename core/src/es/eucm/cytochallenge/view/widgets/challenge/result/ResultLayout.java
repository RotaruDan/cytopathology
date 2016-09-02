package es.eucm.cytochallenge.view.widgets.challenge.result;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.I18NBundle;
import es.eucm.cytochallenge.model.control.TextControl;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.widgets.AbstractWidget;
import es.eucm.cytochallenge.view.widgets.IconButton;
import es.eucm.cytochallenge.view.widgets.Tabs;

public abstract class ResultLayout<T extends TextControl> extends AbstractWidget {

    private final IconButton icon;
    private Actor currentResult;
    private Actor[] results;
    private Table toolbar;
    protected Container<Actor> topRow;
    protected Tabs tabs;
    protected Container<Actor> content;
    protected float score;

    public ResultLayout(Skin skin, T control, I18NBundle i18n, Object... args) {
        toolbar = new Table(skin);

        icon = es.eucm.cytochallenge.view.widgets.WidgetBuilder.toolbarIcon(SkinConstants.IC_UNDO);
        icon.setColor(Color.CLEAR);
        toolbar.add(icon);
        topRow = new Container<Actor>(buildLabel(control, args));
        tabs = new Tabs(skin);
        addResultRow();
        toolbar.background(skin.getDrawable(SkinConstants.DRAWABLE_9P_TOOLBAR));
        toolbar.setColor(SkinConstants.COLOR_TOOLBAR_TOP);
        topRow.fill();
        addActor(toolbar);

        content = new Container<Actor>();
        content.fill();
        addActor(content);
        setTabs(buildTabNames(i18n), skin, buildTabs(control, args));
    }

    private void addResultRow() {
        toolbar.add(topRow).expandX().fillX();
        if (tabs != null) {
            toolbar.add(tabs).right();
        }
    }

    protected Skin getSkin() {
        return toolbar.getSkin();
    }

    private void setTabs(String[] tabName, Skin skin, Actor... tabWidget) {
        if (tabName != null) {
            results = tabWidget;
            tabs.setItems(tabName);
            tabs.addListener(new Tabs.TabListener() {

                @Override
                public void changed(Tabs.TabEvent event) {
                    changeTab();
                }
            });
            changeTab();
        }
    }

    private void changeTab() {
        if (currentResult != null) {
            currentResult.remove();
        }
        currentResult = results[tabs.getSelectedTabIndex()];
        content.setActor(currentResult);
    }

    @Override
    public void layout() {
        toolbar.pack();
        toolbar.setY(Math.max(toolbar.getY(), getHeight() - toolbar.getHeight()));
        toolbar.setWidth(getWidth());
        toolbar.toFront();

        content.setSize(getWidth(), getHeight() - toolbar.getHeight()
                * 0.85f);

    }

    protected void addDescription(Actor description) {
        toolbar.clear();
        Table top = new Table();
        top.add(icon);
        top.add(description).expandX().fillX();
        toolbar.add(top).colspan(2).expandX().fillX();
        toolbar.row();
        addResultRow();
    }

    protected abstract Label buildLabel(T control, Object... args);

    protected String[] buildTabNames(I18NBundle i18n) {
        return new String[]{
                i18n.get("currentAnswer"),
                i18n.get("correctAnswer")
        };
    }

    protected abstract Actor[] buildTabs(T control, Object... args);

    public float getScore() {
        return score;
    }
}
