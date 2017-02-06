package es.eucm.cytochallenge.view.widgets;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * A scrollable gallery of items
 */
public class Gallery extends ScrollPane {

    private Grid grid;

    private GalleryStyle style;

    /**
     * @param rows
     *            the number rows to show in the gallery. No need to be a whole
     *            number
     * @param columns
     *            number of columns of the gallery
     */
    public Gallery(float rows, int columns, Skin skin) {
        this(rows, columns, skin.get(GalleryStyle.class));
    }

    /**
     * @param rows
     *            the number rows to show in the gallery. No need to be a whole
     *            number
     * @param columns
     *            number of columns of the gallery
     */
    public Gallery(float rows, int columns, GalleryStyle style) {
        super(null);
        this.style = style;
        setWidget(grid = new Grid(rows, columns));
        getStyle().background = style.background;
        setScrollingDisabled(true, false);
        setOverscroll(false, false);
    }

    /*
     * @param pad pad between items
     */
    public void pad(float pad) {
        grid.pad(pad);
    }

    public Cell add(Actor actor) {
        Cell cell = new Cell(actor, style);
        grid.addActor(cell);
        return cell;
    }

    public Cell addSpace() {
        Cell cell = new Cell(new Actor());
        cell.usePrefHeight();
        grid.addActor(cell);
        return cell;
    }

    public Cell addOriginal(Actor actor) {
        Cell cell = new Cell(actor);
        cell.usePrefHeight();
        grid.addActor(cell);
        return cell;
    }

    @Override
    public void clearChildren() {
        grid.clearChildren();
    }

    /**
     * Sets the number of columns for the gallery
     */
    public void setColumns(int columns) {
        if (grid.columns != columns) {
            grid.columns = columns;
            grid.invalidate();
        }
    }

    /**
     * Unchecks all cells
     */
    public void uncheckAll() {
        for (Actor cell : grid.getChildren()) {
            ((Cell) cell).checked = false;
        }
    }

    public void setRows(float rows) {
        if (grid.rows != rows) {
            grid.rows = rows;
            grid.invalidate();
        }
    }

    /**
     * @return Preferred cell width, in real pixels, calculated according to
     *         current number of columns in the grid and screen width
     */
    public int getPreferredCellWidth() {
        return Math.round(grid.getColumnWidth());
    }

    /**
     * @return Preferred cell height, in real pixels, calculated according to
     *         current number of rows in the grid and screen height
     */
    public int getPreferredCellHeight() {
        float prefHeight = grid.getPrefRowHeight();
        return Math.round(Math.max(prefHeight, grid.rowHeight(0, prefHeight)));
    }

    @Override
    public void layout() {
        super.layout();
        System.out.println("layout");
    }

    public static class Grid extends AbstractWidget {

        private int columns;

        private float rows;

        private float rowHeight;

        private float pad = WidgetBuilder.dp8ToPixels();

        Grid(float rows, int columns) {
            this.columns = columns;
            this.rows = rows;
            this.rowHeight = -1;
        }

        public float getColumnWidth() {
            return (getWidth() - pad) / columns;
        }

        @Override
        public void layout() {
            float columnWidth = getColumnWidth();
            float prefRowHeight = getPrefRowHeight();
            float y = Math.max(getPrefHeight(), getHeight() - prefRowHeight)
                    - pad;
            int count = 0;
            float rowHeight = 0;
            for (Actor actor : getChildren()) {
                if (count % columns == 0) {
                    rowHeight = rowHeight(count, prefRowHeight);
                    y -= rowHeight;
                }
                setBounds(actor, pad + (count % columns) * columnWidth,
                        y + pad, columnWidth - pad, rowHeight - pad);
                count++;
            }
        }

        public void setRowHeight(float height) {
            this.rowHeight = height;
        }

        public float getRows() {
            return rows;
        }

        public float rowHeight(int fromIndex, float prefRowHeight) {
            float currentRowHeight = 0;
            for (int i = fromIndex; i < fromIndex + columns
                    && i < getChildren().size; i++) {
                Cell cell = (Cell) getChildren().get(i);
                float actorHeight;
                if (cell.usePrefHeight) {
                    actorHeight = getPrefHeight(cell.actor);
                } else {
                    actorHeight = prefRowHeight;
                }
                currentRowHeight = Math.max(currentRowHeight, actorHeight);
            }
            return currentRowHeight;
        }

        public float getPrefRowHeight() {
            return rowHeight == -1 ? (getParent().getHeight() - pad) / rows
                    : rowHeight;
        }

        @Override
        public float getPrefWidth() {
            return getParent().getWidth();
        }

        @Override
        public float getPrefHeight() {
            float height = 0;
            int counter = 0;
            float currentRowHeight = 0;
            for (Actor cell : getChildren()) {
                if (counter % columns == 0) {
                    height += currentRowHeight;
                    currentRowHeight = 0;
                }

                float actorHeight;
                if (((Cell) cell).usePrefHeight) {
                    actorHeight = getPrefHeight(((Cell) cell).actor);
                } else {
                    actorHeight = getPrefRowHeight();
                }
                currentRowHeight = Math.max(currentRowHeight, actorHeight);
                counter++;
            }
            height += currentRowHeight;

            System.out.println("getParent().getHeight() = " + getParent().getHeight());
            System.out.println("height + pad = " + height + pad);
            return Math.max(getParent().getHeight(), height + pad);
        }

        public void pad(float pad) {
            this.pad = pad;
        }
    }

    public static class Cell extends AbstractWidget {

        private Actor actor;

        private boolean usePrefHeight;

        private boolean checked;

        private Drawable pressedForeground;

        private Drawable checkedForeground;

        private ClickListener clickListener = new ClickListener();

        Cell(Actor actor, GalleryStyle galleryStyle) {
            this.actor = actor;
            this.checkedForeground = galleryStyle.checked;
            this.pressedForeground = galleryStyle.pressed;
            addActor(actor);
            addListener(clickListener);
        }

        Cell(Actor actor) {
            this.actor = actor;
            addActor(actor);
        }

        public boolean isChecked() {
            return checked;
        }

        public Cell checked(boolean checked) {
            this.checked = checked;
            return this;
        }

        public Cell usePrefHeight() {
            this.usePrefHeight = true;
            return this;
        }

        public Actor getActor() {
            return actor;
        }

        @Override
        protected void drawChildren(Batch batch, float parentAlpha) {
            super.drawChildren(batch, parentAlpha);
            if (pressedForeground != null && clickListener.isPressed()) {
                pressedForeground.draw(batch, 0, 0, getWidth(), getHeight());
            } else if (checked && checkedForeground != null) {
                checkedForeground.draw(batch, 0, 0, getWidth(), getHeight());
            }
        }

        @Override
        public void layout() {
            setBounds(actor, 0, 0, getWidth(), getHeight());
            actor.getColor().a = 0.0f;
            actor.clearActions();
            actor.addAction(Actions.parallel(
                    Actions.moveTo(0, 0, 0.2f, Interpolation.exp5Out),
                    Actions.alpha(1.0f, 0.5f, Interpolation.exp10Out)));
        }

    }

    public static class GalleryStyle {

        public GalleryStyle() {
        }

        public GalleryStyle(Drawable background, Drawable checked) {
            this.background = background;
            this.checked = checked;
        }

        /**
         * Background for the gallery
         */
        public Drawable background;

        /**
         * Foreground when the cell is pressed
         */
        public Drawable pressed;

        /**
         * Foreground when the cell is selected
         */
        public Drawable checked;

    }
}
