package es.eucm.cytochallenge.view.widgets;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.graphics.Color;

/**
 * A button with an icon
 */
public class IconButton extends Button {

    protected Image iconImage;

    private IconButtonStyle style;

    /**
     * @param icon
     *            the identifier of the icon drawable inside the given skin
     * @param skin
     *            the skin
     */
    public IconButton(String icon, Skin skin) {
        this(icon, 0, skin);
    }

    /**
     * @param icon
     *            the identifier of the icon drawable inside the given skin
     * @param padding
     *            padding of the icon inside the button
     * @param skin
     *            the skin
     */
    public IconButton(String icon, float padding, Skin skin) {
        this(skin.getDrawable(icon), padding, skin);
    }

    public IconButton(String name, String icon, float padding, Skin skin) {
        this(name, skin.getDrawable(icon), padding, skin);
    }

    public IconButton(String icon, float padding, Skin skin, String styleName) {
        this(icon, skin.getDrawable(icon), padding, skin, styleName);
    }

    public IconButton(String icon, Skin skin, String styleName) {
        this(icon, icon, 0, skin, styleName);
    }

    public IconButton(String name, String icon, float padding, Skin skin,
                      String styleName) {
        this(name, skin.getDrawable(icon), padding, skin, styleName);
    }

    /**
     * @param icon
     *            the drawable with the icon
     * @param skin
     *            the skin
     */
    public IconButton(Drawable icon, Skin skin) {
        this(icon, 0, skin);
    }

    /**
     * @param icon
     *            the drawable with the icon
     * @param padding
     *            padding of the icon inside the button
     * @param skin
     *            the skin
     */
    public IconButton(Drawable icon, float padding, Skin skin) {
        this(null, icon, padding, skin, "default");
    }

    public IconButton(String name, Drawable icon, float padding, Skin skin) {
        this(name, icon, padding, skin, "default");
    }

    /**
     * @param icon
     *            the drawable with the icon
     * @param padding
     *            padding of the icon inside the button
     * @param skin
     *            the skin
     * @param styleName
     *            the button style name
     */
    public IconButton(String name, Drawable icon, float padding, Skin skin,
                      String styleName) {
        super(skin);
        setStyle(skin.get(styleName, IconButtonStyle.class));
        init(icon, padding, skin);
        setName(name);
    }

    public IconButton(Drawable drawable, float padding, Skin skin,
                      String styleName) {
        this(null, drawable, padding, skin, styleName);
    }

    protected void init(Drawable icon, float padding, Skin skin) {
        iconImage = new Image(icon);
        iconImage.setScaling(Scaling.fit);
        iconImage.setTouchable(Touchable.disabled);
        add(iconImage).pad(padding);
        setDisabled(false);
    }

    /**
     * Change the {@link Button#isDisabled} attribute and accordingly the color
     * of the image.
     * <p/>
     * The background color when disabled is managed with
     * {@link ButtonStyle#disabled} attribute in {@link Button#draw} method.
     *
     * @param isDisabled
     */
    public void setDisabled(boolean isDisabled) {
        super.setDisabled(isDisabled);
        if (isDisabled) {
            if (style.disabledColor != null) {
                iconImage.setColor(style.disabledColor);
            }
        } else {
            if (style.color != null) {
                iconImage.setColor(style.color);
            }
        }
    }

    @Override
    public void setChecked(boolean isChecked) {
        super.setChecked(isChecked);
        if (isChecked && style.checkedColor != null) {
            iconImage.setColor(style.checkedColor);
        } else {
            iconImage.setColor(style.color);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        // iconImage does not restore the batch color, and its color is
        // transmitted
        batch.setColor(Color.WHITE);
    }

    public void setStyle(IconButtonStyle style) {
        super.setStyle(style);
        this.style = style;
    }

    /**
     * The style for {@link IconButton} See also {@link ButtonStyle}
     */

    public static class IconButtonStyle extends ButtonStyle {

        /**
         * {@link IconButtonStyle#disabledColor} is used to change the
         * {@link Color} of the image used in {@link IconButton} when the button
         * is disabled (it does not include an alternative image).
         */
        public Color disabledColor;

        /**
         * {@link IconButtonStyle#color} is used to change the {@link Color} of
         * the image used in {@link IconButton} when the button is activated (it
         * does not include an alternative image).
         */
        public Color color;

        public Color checkedColor;

        /**
         * Default constructor used for reflection
         */
        public IconButtonStyle() {
        }

        public IconButtonStyle(Color color, Color disabledColor) {
            this.color = color;
            this.disabledColor = disabledColor;
        }

        public IconButtonStyle(IconButtonStyle iconButtonStyle) {
            super(iconButtonStyle);
            this.color = iconButtonStyle.color;
            this.disabledColor = iconButtonStyle.disabledColor;
        }

    }

    public Image getIcon() {
        return iconImage;
    }
}
