package es.eucm.cytochallenge.view.widgets;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pools;

/**
 * Tabs widget that fires {@link ChangeEvent}s when the selected item changes.
 *
 */
public class Tabs extends Table {

    private static final float SELECTION_ANIMATION = .2f;

    private TabsStyle style;

    private Image overlayImage;

    private TextButton selectedTab;

    public Tabs(Skin skin) {
        this(skin.get(TabsStyle.class));
    }

    public Tabs(TabsStyle style) {
        this.style = style;
        overlayImage = new Image(style.selectedDrawable);
        if (style.selectedDrawableColor != null) {
            overlayImage.setColor(style.selectedDrawableColor);
        }
    }

    public void setItems(String... items) {
        clearChildren();
        addActor(overlayImage);
        float verticalPad = WidgetBuilder.dpToPixels(16);
        float horizontalPad = WidgetBuilder.dpToPixels(24);
        for (int i = 0; i < items.length; ++i) {
            String item = items[i];
            final TextButton textButton = new TabButton(item);
            textButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    textButton.setChecked(true);
                }
            });
            textButton.setName(item);
            textButton.setUserObject(i);
            if (i == 0) {
                selectedTab = textButton;
                textButton.setChecked(true);
            } else {
                textButton.setChecked(false);
            }
            textButton.pad(verticalPad, horizontalPad, verticalPad,
                    horizontalPad);
            add(textButton);
        }
    }

    @Override
    public void layout() {
        super.layout();
        if (selectedTab != null) {
            overlayImage.setBounds(selectedTab.getX(), selectedTab.getY(),
                    selectedTab.getWidth(), overlayImage.getHeight());
        }
    }

    public TextButton getSelectedTab() {
        return selectedTab;
    }

    public int getSelectedTabIndex() {
        if(selectedTab == null) {
            return 0;
        }
        return (Integer) selectedTab.getUserObject();
    }

    /**
     * The style for {@link Tabs} widget.
     */
    static public class TabsStyle {

        public TextButtonStyle textButtonStyle;

        public Color activeTextColor, disablexTextColor, selectedDrawableColor;

        public Drawable selectedDrawable;

    }

    private class TabButton extends TextButton {

        public TabButton(String text) {
            super(text, style.textButtonStyle);
        }

        @Override
        public void setChecked(boolean isChecked) {
            if (overlayImage.getActions().size > 0) {
                return;
            }
            if (isChecked) {
                if (selectedTab == this) {
                    return;
                }
                selectedTab = this;
                fireTabChanged();
                for (Cell cell : Tabs.this.getCells()) {
                    TextButton btn = (TextButton) cell.getActor();
                    if (btn != this) {
                        btn.setChecked(false);
                    }
                }
            } else if (selectedTab == this) {
                return;
            }

            Color color;
            if (isChecked) {
                float targetX = getX();
                if (targetX < overlayImage.getX()) {
                    overlayImage.addAction(Actions.moveTo(targetX, getY(),
                            SELECTION_ANIMATION, Interpolation.pow2Out));
                    overlayImage.addAction(Actions.sequence(Actions.sizeTo(
                            getWidth() + overlayImage.getWidth(),
                            overlayImage.getHeight(), SELECTION_ANIMATION,
                            Interpolation.pow2Out), Actions.sizeTo(getWidth(),
                            overlayImage.getHeight(), SELECTION_ANIMATION,
                            Interpolation.pow2Out)));
                } else {
                    overlayImage.addAction(Actions.sequence(Actions.sizeTo(
                                    getWidth() + overlayImage.getWidth(),
                                    overlayImage.getHeight(), SELECTION_ANIMATION,
                                    Interpolation.pow2Out),
                            Actions.parallel(
                                    Actions.sizeTo(getWidth(),
                                            overlayImage.getHeight(),
                                            SELECTION_ANIMATION,
                                            Interpolation.pow2Out), Actions
                                            .moveTo(targetX, getY(),
                                                    SELECTION_ANIMATION,
                                                    Interpolation.pow2Out))));
                }
                color = style.activeTextColor;
            } else {
                color = style.disablexTextColor;
            }
            getLabel().setColor(color);
            super.setChecked(isChecked);
        }

        private void fireTabChanged() {
            TabEvent event = Pools.obtain(TabEvent.class);
            event.index = getSelectedTabIndex();
            fire(event);
            Pools.free(event);
        }
    }

    /**
     * Listener for {@link TabEvent}s.
     */
    abstract static public class TabListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            if (event instanceof TabEvent) {
                changed((TabEvent) event);
                return true;
            }
            return false;
        }

        /**
         * Invoked when the tab has changed its current selected tab.
         */
        abstract public void changed(TabEvent event);

    }

    /**
     * Fired when a {@link Tabs} widget has changed the selected tab.
     *
     */
    static public class TabEvent extends Event {

        private int index;

        @Override
        public void reset() {
            super.reset();
            index = 0;
        }

        public int getTabIndex() {
            return index;
        }
    }
}
