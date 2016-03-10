package es.eucm.cytochallenge.view.widgets.challenge;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
import com.badlogic.gdx.utils.*;
import es.eucm.cytochallenge.model.*;
import es.eucm.cytochallenge.model.control.MultipleAnswerControl;
import es.eucm.cytochallenge.model.control.MultipleImageAnswerControl;
import es.eucm.cytochallenge.model.control.TextControl;
import es.eucm.cytochallenge.model.control.draganddrop.DragAndDropAnswer;
import es.eucm.cytochallenge.model.control.draganddrop.DragAndDropControl;
import es.eucm.cytochallenge.model.control.filltheblank.FillTheBlankControl;
import es.eucm.cytochallenge.model.control.filltheblank.FillTheBlankStatement;
import es.eucm.cytochallenge.utils.Grades;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.screens.BaseScreen;
import es.eucm.cytochallenge.view.widgets.LinearLayout;
import es.eucm.cytochallenge.view.widgets.challenge.filltheblank.FillTheBlankText;
import es.eucm.cytochallenge.view.widgets.slide.SlideEditor;

import java.util.*;
import java.util.List;

/**
 * Created by dan on 02/03/2016.
 */
public class TextChallengeWidget implements WidgetBuilder<TextChallenge> {

    public static final float HIDE_TIME = .5f;
    private Table root;
    private TextChallenge challenge;

    private String challengePath;

    // MCQ
    private ButtonGroup<TextButton> group = new ButtonGroup<TextButton>();


    // MICQ
    private ButtonGroup<Button> imageGroup = new ButtonGroup<Button>();

    // DnD
    private Array<WidgetGroup> dragContainers = new Array<WidgetGroup>();

    // FtB
    private Array<FillTheBlankText> ftbTexts = new Array<FillTheBlankText>();

    public TextChallengeWidget() {
        root = new Table();
    }

    public void setChallengePath(String challengePath) {
        this.challengePath = challengePath;
    }

    @Override
    public void init(TextChallenge challenge) {
        root.clear();

        this.challenge = challenge;
        Texture texture = null;
        if (challenge.getImagePath() != null) {
            texture = new Texture(Gdx.files.internal(challengePath + challenge.getImagePath()));
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        TextControl textControl = challenge.getTextControl();
        Label text = new Label(textControl.getText(), BaseScreen.skin, SkinConstants.STYLE_TOAST);
        text.setWrap(true);

        Container imageContainer;

        float defaultPad = es.eucm.cytochallenge.view.widgets.WidgetBuilder.dpToPixels(20f);

        if (textControl instanceof MultipleAnswerControl) {

            Table right = new Table();
            right.add(text).fillX().expandX();
            final Image image = new Image(new TextureRegionDrawable(
                    new TextureRegion(texture)));
            image.setScaling(Scaling.fit);

            MultipleAnswerControl multipleAnswerControl = ((MultipleAnswerControl) textControl);
            String[] answers = multipleAnswerControl.getAnswers();
            final LinearLayout answersLayout = new LinearLayout(false);

            group.clear();
            group.setMaxCheckCount(1);

            for (int i = 0; i < answers.length; i++) {
                String answer = answers[i];
                final TextButton answerLabel = new TextButton((i + 1) + " - " + answer, BaseScreen.skin,
                        SkinConstants.STYLE_MULTIPLE_CHOICE);
                answerLabel.setUserObject(answer);
                answerLabel.pad(defaultPad);
                answerLabel.getLabel().setAlignment(Align.left);
                answersLayout.add(answerLabel).expandX();

                group.add(answerLabel);
            }

            right.row();
            right.add(answersLayout).expand();

            SlideEditor slideEditor = new SlideEditor(BaseScreen.skin);
            slideEditor.setRootActor(image);

            imageContainer = new Container();
            imageContainer.setActor(slideEditor);
            imageContainer.fill();
            imageContainer.setClip(true);

            root.defaults().expand().fill().pad(defaultPad).prefWidth(Value.percentWidth(0.5f, root));
            root.add(imageContainer);
            root.add(right);

        }


        // MICQ
        else if (textControl instanceof MultipleImageAnswerControl) {
            imageGroup.clear();

            MultipleImageAnswerControl miaControl = (MultipleImageAnswerControl) textControl;
            String[] answers = miaControl.getAnswers();

            Table rootTable = new Table();
            //rootTable.setFillParent(true);

            imageGroup.setMaxCheckCount(answers.length);

            for (int i = 0; i < answers.length; i++) {
                String answer = answers[i];
                Texture answerTexture = new Texture(Gdx.files.internal(challengePath + answer));
                answerTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

                Button imageButton = new Button(BaseScreen.skin.get(SkinConstants.STYLE_CHECK,
                        Button.ButtonStyle.class));
                Image imageActor = new Image(new TextureRegionDrawable(
                        new TextureRegion(answerTexture)));
                imageActor.setScaling(Scaling.fill);
                float pad8 = es.eucm.cytochallenge.view.widgets.WidgetBuilder.dpToPixels(imageActor.getHeight() * .1f);

                imageButton.add(imageActor).expand().fill().pad(pad8);
                imageButton.setClip(true);
                imageButton.setUserObject(false);

                imageGroup.add(imageButton);

                rootTable.pad(pad8);
                rootTable.defaults();
                rootTable.add(imageButton).expand().fill();
                if (i + i == answers.length / 2) {
                    rootTable.row();
                }
            }

            int[] correctAnswers = miaControl.getCorrectAnswers();
            for (int i = 0; i < correctAnswers.length; i++) {
                int correctAnswer = correctAnswers[i];

                imageGroup.getButtons().get(correctAnswer).setUserObject(true);
            }

            // TODO random shuffle

            rootTable.pack();

            SlideEditor slideEditor = new SlideEditor(BaseScreen.skin);
            slideEditor.setRootActor(rootTable);

            imageContainer = new Container();
            imageContainer.setActor(slideEditor);
            imageContainer.fill();
            imageContainer.setClip(true);

            root.defaults().expand().fill();
            root.add(imageContainer);

        }


        // DND
        else if (textControl instanceof DragAndDropControl) {
            dragContainers.clear();

            DragAndDropControl dndControl = (DragAndDropControl) textControl;

            DragAndDropAnswer[] answers = dndControl.getAnswers();

            final DragAndDrop dnd = new DragAndDrop();

            final Image imageActor = new Image(new TextureRegionDrawable(
                    new TextureRegion(texture)));
            imageActor.setScaling(Scaling.fit);

            Group imageGroup = new Group();
            imageGroup.addActor(imageActor);
            imageGroup.setBounds(imageActor.getX(), imageActor.getY(), imageActor.getWidth(), imageActor.getHeight());

            float meanWidth = 0f, meanHeight = 0f;

            for (int i = 0; i < answers.length; i++) {
                final DragAndDropAnswer answer = answers[i];

                final WidgetGroup parentContainer = new WidgetGroup();
                parentContainer.setFillParent(true);
                parentContainer.setTouchable(Touchable.childrenOnly);
                dragContainers.add(parentContainer);

                // Correct Answer stored inside the parent
                parentContainer.setUserObject(answer.getText());

                final TextButton answerLabel = new TextButton("", BaseScreen.skin, SkinConstants.STYLE_DRAGANDDROP);
                answerLabel.setPosition(answer.getX() + (answer.getWidth() - answerLabel.getPrefWidth()) * .5f,
                        answer.getY() + (answer.getHeight() - answerLabel.getPrefHeight()) * .5f);
                parentContainer.addActor(answerLabel);

                meanWidth += answer.getWidth();
                meanHeight += answer.getHeight();

                answerLabel.getLabel().setColor(Color.CLEAR);

                imageGroup.addActor(parentContainer);

                dnd.addTarget(new DragAndDrop.Target(answerLabel) {
                    @Override
                    public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                        getActor().setColor(Color.GREEN);
                        return true;
                    }

                    @Override
                    public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload) {
                        getActor().setColor(Color.WHITE);
                    }

                    @Override
                    public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {

                    }
                });
            }

            meanWidth = meanWidth / answers.length;
            meanHeight = meanHeight / answers.length;

            for (int j = 0; j < dragContainers.size; j++) {
                WidgetGroup widgetGroup = dragContainers.get(j);
                TextButton first = (TextButton) widgetGroup.getChildren().first();

                first.setBounds(first.getX() + (first.getWidth() - meanWidth) * .5f,
                        first.getY() + (first.getHeight() - meanHeight) * .5f,
                        meanWidth, meanHeight);
            }

            SlideEditor slideEditor = new SlideEditor(BaseScreen.skin);
            slideEditor.setRootActor(imageGroup);

            imageContainer = new Container();
            imageContainer.setActor(slideEditor);
            imageContainer.fill();
            imageContainer.setClip(true);

            root.defaults().expand().fill();
            root.add(imageContainer);

            final Table panelTable = new Table();

            for (int i = 0; i < answers.length; i++) {
                DragAndDropAnswer correctAnswer = answers[i];
                final TextButton answerLabel = new TextButton(correctAnswer.getText(), BaseScreen.skin,
                        SkinConstants.STYLE_DRAGANDDROP);

                // start DnD
                dnd.addSource(new DragAndDrop.Source(answerLabel) {
                    private final DragAndDrop.Payload payload = new DragAndDrop.Payload();
                    private float x, y;

                    @Override
                    public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                        this.x = answerLabel.getX();
                        this.y = answerLabel.getY();

                        dnd.setDragActorPosition(-answerLabel.getWidth() * .5f, answerLabel.getHeight());

                        payload.setObject(answerLabel.getParent());
                        payload.setDragActor(answerLabel);

                        return payload;
                    }

                    @Override
                    public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload,
                                         DragAndDrop.Target target) {
                        Actor dragActor = payload.getDragActor();
                        if (target == null) {
                            WidgetGroup parent = (WidgetGroup) payload.getObject();
                            if (parent == panelTable) {
                                panelTable.add(getActor());
                                panelTable.row();
                            } else {
                                getActor().setPosition(this.x, this.y);
                                parent.addActor(getActor());
                            }

                        } else {

                            Actor targetActor = target.getActor();

                            dragActor.setPosition(targetActor.getX() + (targetActor.getWidth() - dragActor.getWidth()) * .5f,
                                    targetActor.getY() + (targetActor.getHeight() - dragActor.getHeight()) * .5f);

                            targetActor.getParent().addActor(dragActor);

                            if (((TextButton) targetActor).getLabel().getText().toString().isEmpty() &&
                                    payload.getObject() == panelTable) {
                                targetActor.remove();
                            } else {

                                targetActor.addAction(Actions.moveTo(this.x + (dragActor.getWidth() - targetActor.getWidth()) * .5f,
                                        this.y + (dragActor.getHeight() - targetActor.getHeight()) * .5f, .5f,
                                        Interpolation.circle));
                                ((WidgetGroup) payload.getObject()).addActor(targetActor);
                            }
                        }
                    }

                });

                dnd.addTarget(new DragAndDrop.Target(answerLabel) {
                    @Override
                    public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                        if (getActor().getParent() == panelTable) {
                            return false;
                        }
                        answerLabel.setColor(Color.GREEN);
                        return true;
                    }

                    @Override
                    public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload) {
                        answerLabel.setColor(Color.WHITE);
                    }

                    @Override
                    public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {

                    }
                });

                panelTable.add(answerLabel);
                panelTable.row();
            }

            ScrollPane scroll = new ScrollPane(panelTable);
            panelTable.background(BaseScreen.skin.getDrawable(SkinConstants.DRAWABLE_9P_PAGE_RIGHT));
            panelTable.setColor(SkinConstants.COLOR_PANEL_RIGHT);
            panelTable.pack();
            Container panelCont = new Container(scroll);
            panelCont.setFillParent(true);
            panelCont.right().fillY();

            root.addActor(panelCont);

        }


        // FtB
        else if (textControl instanceof FillTheBlankControl) {
            ftbTexts.clear();

            FillTheBlankControl dndControl = (FillTheBlankControl) textControl;

            Table verticalLayout = new Table();

            Label mainTitle = new Label(dndControl.getText(), BaseScreen.skin, SkinConstants.STYLE_TOOLBAR);
            mainTitle.setAlignment(Align.center);
            mainTitle.setWrap(true);

            verticalLayout.add(mainTitle).expandX();
            verticalLayout.row();

            FillTheBlankStatement[] statements = dndControl.getStatements();

            for (int i = 0; i < statements.length; i++) {
                FillTheBlankStatement statement = statements[i];

                FillTheBlankText fillText = new FillTheBlankText(BaseScreen.skin);
                fillText.init(statement);
                ftbTexts.add(fillText);

                verticalLayout.add(fillText).expandX().left();
                verticalLayout.row();
            }

            ScrollPane scroll = new ScrollPane(verticalLayout);
            scroll.setScrollingDisabled(true, false);
            scroll.setFillParent(true);

            root.defaults().expand().fill();
            root.add(scroll);

        }
    }

    // DragAndDropControl
    private int checkDnDResult() {
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

    // FillTheBlankControl
    private int checkFtBResult() {
        int correctCount = 0;
        for (int i = 0; i < ftbTexts.size; i++) {
            FillTheBlankText child = ftbTexts.get(i);

            correctCount += child.getCorrectAnswers();
        }
        return correctCount;
    }

    private int checkFtBTotalCount() {
        int correctCount = 0;
        for (int i = 0; i < ftbTexts.size; i++) {
            FillTheBlankText child = ftbTexts.get(i);

            correctCount += child.getTotalAnswers();
        }
        return correctCount;
    }

    // MultipleImageAnswerControl
    private int checkMIAResult() {
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

    public void setUpScore() {

        root.clear();

        TextControl textControl = challenge.getTextControl();
        if (textControl instanceof MultipleAnswerControl) {
            MultipleAnswerControl multipleAnswerControl = ((MultipleAnswerControl) textControl);

            int score = 0;
            String correctAnswer = multipleAnswerControl.getAnswers()[multipleAnswerControl.getCorrectAnswer()];
            String selectedAnswer = group.getChecked().getUserObject().toString();

            if (correctAnswer.equals(selectedAnswer)) {
                score = 100;
            }

            String answer = Grades.getGrade(score) + "\n" + correctAnswer;
            Label resultsLabel = new Label(answer, BaseScreen.skin, SkinConstants.STYLE_TOAST);
            resultsLabel.setAlignment(Align.center);

            root.add(resultsLabel).expand().fill();

        }

        // MultipleImageAnswerControl
        else if (textControl instanceof MultipleImageAnswerControl) {
            MultipleImageAnswerControl multipleImageAnswerControl = ((MultipleImageAnswerControl) textControl);


            int results = Math.max(0, checkMIAResult());
            int total = multipleImageAnswerControl.getCorrectAnswers().length;

            Label resultsLabel = new Label(Grades.getGrade(results / (float) total * 100) +
                    "    " + results + "/" + total, BaseScreen.skin, SkinConstants.STYLE_TOAST);
            resultsLabel.setAlignment(Align.center);

            root.add(resultsLabel).expand().fill();

        }

        // DragAndDropControl
        else if (textControl instanceof DragAndDropControl) {

            int results = checkDnDResult();
            int total = dragContainers.size;

            Label resultsLabel = new Label(Grades.getGrade(results / (float) total * 100) +
                    "    " + results + "/" + total, BaseScreen.skin, SkinConstants.STYLE_TOAST);
            resultsLabel.setAlignment(Align.center);

            root.add(resultsLabel).expand().fill();
        }

        // FillTheBlankControl
        else if (textControl instanceof FillTheBlankControl) {
            int results = checkFtBResult();
            int total = checkFtBTotalCount();

            Label resultsLabel = new Label(Grades.getGrade(results / (float) total * 100) +
                    "    " + results + "/" + total, BaseScreen.skin, SkinConstants.STYLE_TOAST);
            resultsLabel.setAlignment(Align.center);

            root.add(resultsLabel).expand().fill();
        }
    }

    @Override
    public Actor getWidget() {
        return root;
    }
}
