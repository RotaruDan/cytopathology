package es.eucm.cytochallenge.view.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import es.eucm.cytochallenge.model.control.MultipleAnswerControl;
import es.eucm.cytochallenge.model.control.MultipleImageAnswerControl;
import es.eucm.cytochallenge.model.control.TextControl;
import es.eucm.cytochallenge.model.control.draganddrop.DragAndDropControl;
import es.eucm.cytochallenge.model.control.filltheblank.FillTheBlankControl;
import es.eucm.cytochallenge.view.SkinConstants;


public class ChallengeButton extends TextButton {

    private final Image image;
    private final Label label;

    public ChallengeButton(TextControl textControl, Skin skin) {
        super(textControl.getText(), skin);

        label = getLabel();
        label.setEllipsis(true);
        label.setEllipsis("...");
        label.setAlignment(Align.left);

        String imageIcon = "";
        if (textControl instanceof MultipleAnswerControl) {
            imageIcon = SkinConstants.IC_MCQ;
        } else if (textControl instanceof DragAndDropControl) {
            imageIcon = SkinConstants.IC_DND;
        } else if (textControl instanceof FillTheBlankControl) {
            imageIcon = SkinConstants.IC_FTB;
        } else if (textControl instanceof MultipleImageAnswerControl) {
            imageIcon = SkinConstants.IC_MICQ;
        }
        image = new Image(skin.getDrawable(imageIcon));
        image.setScaling(Scaling.fit);

        clearChildren();
        float defaultPad = WidgetBuilder.dpToPixels(24f);

        pad(defaultPad);
        left();
        defaults().space(defaultPad);

        add(image).width(image.getWidth());
        add(label);
        setSize(getPrefWidth(), getPrefHeight());
    }

    @Override
    public void layout() {
        super.layout();
        if(label.getWidth() + label.getX() >= getWidth()) {
            label.setWidth(getWidth() - label.getX());
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isPressed())
            image.setColor(Color.WHITE);
        else
            image.setColor(SkinConstants.COLOR_BUTTON);
        super.draw(batch, parentAlpha);
    }
}
