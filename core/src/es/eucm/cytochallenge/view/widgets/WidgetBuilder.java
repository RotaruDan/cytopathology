package es.eucm.cytochallenge.view.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Scaling;
import es.eucm.cytochallenge.model.Difficulty;
import es.eucm.cytochallenge.view.screens.BaseScreen;
import es.eucm.cytochallenge.view.SkinConstants;


public class WidgetBuilder {

    public static float dpToPixels(float dp) {
        return Gdx.graphics.getDensity() * dp;
    }

    public static IconButton toolbarIcon(String icon) {
        IconButton iconButton = icon(icon, SkinConstants.STYLE_TOOLBAR);
        return iconButton;
    }

    public static IconButton icon(String icon, String style) {
        IconButton iconButton = new IconButton(icon, BaseScreen.skin, style);
        return iconButton;
    }

    public static Button button(String style) {
        return new Button(BaseScreen.skin, style);
    }

    public static ImageButton imageButton(String style) {
        ImageButton button = new ImageButton(BaseScreen.skin, style);
        return button;
    }

    public static ImageButton imageButton(String icon, String buttonStyle) {
        ButtonStyle style = BaseScreen.skin.get(buttonStyle, ButtonStyle.class);
        ImageButtonStyle imageButtonStyle = new ImageButtonStyle(style);
        imageButtonStyle.imageUp = BaseScreen.skin.getDrawable(icon);
        return new ImageButton(imageButtonStyle);
    }

    public static Button button(String icon, String label, String style) {
        LinearLayout row = new LinearLayout(true);
        row.add(icon(icon, style));
        row.add(new Label(label, BaseScreen.skin, style));
        row.addSpace();
        row.pad(0, 0, dpToPixels(16), 0);
        row.setTouchable(Touchable.disabled);

        Button button = button(style);
        button.setName(icon);
        button.add(row).fillX().expandX();
        return button;
    }

    public static ImageButton circleButton(String icon) {
        ButtonStyle circleStyle = BaseScreen.skin.get(SkinConstants.STYLE_CIRCLE,
                ButtonStyle.class);
        ImageButtonStyle imageButtonStyle = new ImageButtonStyle(circleStyle);
        imageButtonStyle.imageUp = BaseScreen.skin.getDrawable(icon);
        ImageButton imageButton = new ImageButton(imageButtonStyle);
        imageButton.setName(icon);
        return imageButton;
    }

    public static ImageButton playButton() {
        ButtonStyle circleStyle = BaseScreen.skin.get(SkinConstants.STYLE_CELL_WITH_RECEPTORS,
                ButtonStyle.class);
        ImageButtonStyle imageButtonStyle = new ImageButtonStyle(circleStyle);
        imageButtonStyle.imageUp = BaseScreen.skin.getDrawable(SkinConstants.IC_PLAY);
        ImageButton imageButton = new ImageButton(imageButtonStyle);
        imageButton.getImage().setScaling(Scaling.none);
        imageButton.setName(SkinConstants.IC_PLAY_BIG);
        return imageButton;
    }

    public static Image image(String drawable) {
        return new Image(BaseScreen.skin, drawable);
    }

    /**
     * @return a label with the given text and style, with ellipsis for the
     * label set to true
     */
    public static Label label(String text, String style) {
        Label label = new Label(text, BaseScreen.skin, style);
        label.setEllipsis(true);
        return label;
    }

    public static TextButton textButton(String text) {
        TextButton button = textButton(text, "default");
        button.setColor(SkinConstants.COLOR_BUTTON);
        return button;
    }

    public static TextButton textButton(String text, String style) {
        return new TextButton(text, BaseScreen.skin, style);
    }


    public static TextButton dialogButton(String text, TextButton.TextButtonStyle style) {
        TextButton button = new TextButton(text.toUpperCase(), style);
        button.pad(dpToPixels(8)).padBottom(dpToPixels(10))
                .padTop(dpToPixels(10));
        return button;
    }

    public static Image image(String icon, String color) {
        Image image = new Image(BaseScreen.skin, icon);
        image.setColor(BaseScreen.skin.getColor(color));
        return image;
    }

    public static Label difficulty(Difficulty difficulty, I18NBundle i18n) {
        Color difficultyColor = null;
        String difficultyText = null;
        if (difficulty == Difficulty.EASY) {
            difficultyColor = Color.CHARTREUSE;
            difficultyText = "easy";
        } else if (difficulty == Difficulty.MEDIUM) {
            difficultyColor = Color.ORANGE;
            difficultyText = "medium";
        } else {
            difficultyColor = Color.RED;
            difficultyText = "hard";
        }
        Label difficultyLabel = new Label(i18n.get(difficultyText).toUpperCase(), BaseScreen.skin, "toast");
        difficultyLabel.setColor(difficultyColor);
        return difficultyLabel;
    }
}