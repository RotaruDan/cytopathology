package es.eucm.cytochallenge.view.widgets.challenge.result;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Scaling;
import es.eucm.cytochallenge.model.control.MultipleAnswerControl;
import es.eucm.cytochallenge.model.control.MultipleImageAnswerControl;
import es.eucm.cytochallenge.utils.Grades;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.widgets.TopToolbarLayout;
import es.eucm.cytochallenge.view.widgets.slide.SlideEditor;

public class MultipleImageAnswerResult extends ResultLayout<MultipleImageAnswerControl> {


    public MultipleImageAnswerResult(Skin skin,
                                     MultipleImageAnswerControl control,
                                     I18NBundle i18n,
                                     ButtonGroup selectedGroup, Actor right) {
        super(skin, control, i18n, selectedGroup, right);
    }

    // MultipleImageAnswerControl
    private int checkMIAResult(ButtonGroup imageGroup) {
        Array<Button> buttons = imageGroup.getButtons();

        int correctAnswers = 0;
        for (int i = 0; i < buttons.size; i++) {
            Button button = buttons.get(i);
            if ((Boolean) button.getUserObject()) {
                if (button.isChecked()) {
                    correctAnswers++;
                } else {
                    // correctAnswers--;
                }
            } else {
                if (button.isChecked()) {
                    correctAnswers--;
                }
            }
        }
        return correctAnswers;
    }

    @Override
    protected Label buildLabel(MultipleImageAnswerControl control, Object... args) {
        int results = Math.max(0, checkMIAResult((ButtonGroup) args[0]));
        int total = control.getCorrectAnswers().length;

        Label resultsLabel = new Label(Grades.getGrade(results / (float) total * 100) +
                "    " + results + "/" + total, getSkin(), SkinConstants.STYLE_TOAST);
        resultsLabel.setAlignment(Align.center);
        return resultsLabel;
    }

    @Override
    protected Actor[] buildTabs(MultipleImageAnswerControl control, Object... args) {
        Actor right = (Actor) args[1];

        SlideEditor editor = (SlideEditor) right;
        Table rootRightTable = (Table) editor.getRootActor();


        String[] answers = control.getAnswers();

        Table rootTable = new Table();


        for (int i = 0; i < answers.length; i++) {

            Button imageButton = new Button(getSkin().get(SkinConstants.STYLE_CHECK,
                    Button.ButtonStyle.class));
            Button originalButton = (Button)rootRightTable.getChildren().get(i);
            Image originalImage = (Image) originalButton.getChildren().first();

            Image imageActor = new Image(originalImage.getDrawable());
            imageActor.setScaling(Scaling.fit);
            float pad8 = es.eucm.cytochallenge.view.widgets.WidgetBuilder.dpToPixels(imageActor.getHeight() * .1f);

            imageButton.add(imageActor).expand().fill().pad(pad8);
            imageButton.setClip(true);
            imageButton.setUserObject(false);


            rootTable.pad(pad8);
            rootTable.defaults();
            rootTable.add(imageButton).expand().fill();
            if (i + i == answers.length / 2) {
                rootTable.row();
            }
        }

        int[] correctAnswers = control.getCorrectAnswers();
        for (int i = 0; i < correctAnswers.length; i++) {
            int correctAnswer = correctAnswers[i];

            Button button = (Button) rootTable.getChildren().get(correctAnswer);
            button.setChecked(true);
        }

        rootTable.pack();
        rootTable.setTouchable(Touchable.disabled);

        SlideEditor slideEditor = new SlideEditor(getSkin());
        slideEditor.setRootActor(rootTable);

        Label label = new Label(control.getText(), getSkin(),
                SkinConstants.STYLE_TOOLBAR);
        label.setAlignment(Align.center);

        ScrollPane horizontalScroll = new ScrollPane(label);
        horizontalScroll.setScrollingDisabled(false, true);

        Button icon = es.eucm.cytochallenge.view.widgets.WidgetBuilder.toolbarIcon(SkinConstants.IC_UNDO);
        icon.setColor(Color.CLEAR);

        addDescription(horizontalScroll);


        return new Actor[]{
                right,
                slideEditor
        };
    }
}
