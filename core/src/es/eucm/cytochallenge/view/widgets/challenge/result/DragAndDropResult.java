package es.eucm.cytochallenge.view.widgets.challenge.result;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Scaling;
import es.eucm.cytochallenge.model.control.MultipleImageAnswerControl;
import es.eucm.cytochallenge.model.control.draganddrop.DragAndDropAnswer;
import es.eucm.cytochallenge.model.control.draganddrop.DragAndDropControl;
import es.eucm.cytochallenge.utils.Grades;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.screens.BaseScreen;
import es.eucm.cytochallenge.view.widgets.RightToolbarLayout;
import es.eucm.cytochallenge.view.widgets.TopToolbarLayout;
import es.eucm.cytochallenge.view.widgets.slide.SlideEditor;

public class DragAndDropResult extends ResultLayout<DragAndDropControl> {


    public DragAndDropResult(Skin skin,
                             DragAndDropControl control,
                             I18NBundle i18n,
                             Array<WidgetGroup> containers, Actor right) {
        super(skin, control, i18n, containers, right);
    }


    // DragAndDropControl
    private int checkDnDResult(Array<WidgetGroup> dragContainers) {
        int correctCount = 0;
        for (int i = 0; i < dragContainers.size; i++) {
            if (dragContainers.get(i) instanceof Group) {
                WidgetGroup child = dragContainers.get(i);

                String correctAnswer = (String) child.getUserObject();
                String currentAnswer = ((TextButton) child.getChildren().first()).getLabel().getText().toString();

                if (correctAnswer.equals(currentAnswer)) {
                    correctCount++;
                }
            }
        }
        return correctCount;
    }

    @Override
    protected Label buildLabel(DragAndDropControl control, Object... args) {
        Array containers = (Array) args[0];
        int results = checkDnDResult(containers);
        int total = containers.size;

        float finalGrade = results / (float) total * 100;
        score = finalGrade;

        Label resultsLabel = new Label(Grades.getGrade(finalGrade) +
                "    " + results + "/" + total, getSkin(), SkinConstants.STYLE_TOAST);
        resultsLabel.setAlignment(Align.center);
        return resultsLabel;
    }

    @Override
    protected Actor[] buildTabs(DragAndDropControl control, Object... args) {
        Actor right = (Actor) args[1];

        SlideEditor editor = (SlideEditor) right;
        Group rootRightImage = (Group) editor.getRootActor();



        DragAndDropAnswer[] answers = control.getAnswers();

        final Image imageActor = new Image(((Image)rootRightImage.getChildren().first())
                .getDrawable());
        imageActor.setScaling(Scaling.fit);

        Group imageGroup = new Group();
        imageGroup.addActor(imageActor);

        float maxWidth = 0f, maxHeight = 0f;
        float maxX = imageActor.getWidth(), maxY = imageActor.getHeight();
        float minX = 0f, minY = 0f;

        float defaultPad = es.eucm.cytochallenge.view.widgets.WidgetBuilder.dpToPixels(24f);

        for (int i = 0; i < answers.length; i++) {
            final DragAndDropAnswer answer = answers[i];


            final WidgetGroup parentContainer = new WidgetGroup();
            parentContainer.setFillParent(true);
            parentContainer.setTouchable(Touchable.childrenOnly);

            // Correct Answer stored inside the parent
            parentContainer.setUserObject(answer.getText());

            final TextButton answerLabel = new TextButton(answer.getText(), getSkin(), SkinConstants.STYLE_DRAGANDDROP);
            answerLabel.setPosition(answer.getX() + (answer.getWidth() - answerLabel.getPrefWidth()) * .5f,
                    answer.getY() + (answer.getHeight() - answerLabel.getPrefHeight()) * .5f);
            answerLabel.pack();
            parentContainer.addActor(answerLabel);

            maxWidth = Math.max(maxWidth, answerLabel.getWidth());
            maxHeight = Math.max(maxHeight, answerLabel.getHeight());

            // Max/min computing
            maxX = Math.max(answer.getX() + answerLabel.getWidth() + defaultPad, maxX);
            maxY = Math.max(answer.getY() + answerLabel.getHeight() + defaultPad, maxY);
            minX = Math.min(answer.getX(), minX);
            minY = Math.min(answer.getY(), minY);

            imageGroup.addActor(parentContainer);

        }

        imageGroup.setBounds(minX, minY, maxX - minX, maxY - minY);
        imageGroup.setTouchable(Touchable.disabled);

        SlideEditor slideEditor = new SlideEditor(getSkin());
        slideEditor.setRootActor(imageGroup);
        slideEditor.setAlign(Align.center);

        return new Actor[]{
                right,
                slideEditor
        };
    }
}
