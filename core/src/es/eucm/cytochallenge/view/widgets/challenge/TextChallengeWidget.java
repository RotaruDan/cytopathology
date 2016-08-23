package es.eucm.cytochallenge.view.widgets.challenge;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.*;
import es.eucm.cytochallenge.model.*;
import es.eucm.cytochallenge.model.control.InteractiveZoneControl;
import es.eucm.cytochallenge.model.control.MultipleAnswerControl;
import es.eucm.cytochallenge.model.control.MultipleImageAnswerControl;
import es.eucm.cytochallenge.model.control.TextControl;
import es.eucm.cytochallenge.model.control.draganddrop.DragAndDropAnswer;
import es.eucm.cytochallenge.model.control.draganddrop.DragAndDropControl;
import es.eucm.cytochallenge.model.control.filltheblank.FillTheBlankControl;
import es.eucm.cytochallenge.model.control.filltheblank.FillTheBlankStatement;
import es.eucm.cytochallenge.utils.ChallengeResourceProvider;
import es.eucm.cytochallenge.utils.Grades;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.screens.BaseScreen;
import es.eucm.cytochallenge.view.widgets.IconButton;
import es.eucm.cytochallenge.view.widgets.LinearLayout;
import es.eucm.cytochallenge.view.widgets.RightToolbarLayout;
import es.eucm.cytochallenge.view.widgets.TopToolbarLayout;
import es.eucm.cytochallenge.view.widgets.challenge.filltheblank.FillTheBlankText;
import es.eucm.cytochallenge.view.widgets.challenge.result.*;
import es.eucm.cytochallenge.view.widgets.slide.SlideEditor;

import java.util.*;


public class TextChallengeWidget implements WidgetBuilder<TextChallenge> {

    public static final float HIDE_TIME = .5f;
    private Table root;
    private TextChallenge challenge;

    private CompletedListener completedListener;
    private ChallengeResourceProvider challengeResourceProvider;
    private I18NBundle i18n;
    private Skin skin;

    // MCQ
    private ButtonGroup<TextButton> group = new ButtonGroup<TextButton>();

    // MICQ
    private ButtonGroup<Button> imageGroup = new ButtonGroup<Button>();

    // DnD
    private Array<WidgetGroup> dragContainers = new Array<WidgetGroup>();

    // FtB
    private Array<FillTheBlankText> ftbTexts = new Array<FillTheBlankText>();

    // ZoneInteraction
    private Array<Button> markers = new Array<Button>();

    public TextChallengeWidget(Skin skin, I18NBundle bundle) {
        root = new Table();
        this.skin = skin;
        i18n = bundle;
    }

    public void setChallengeResourceProvider(ChallengeResourceProvider challengeResourceProvider) {
        this.challengeResourceProvider = challengeResourceProvider;
    }

    private class ImageResourceCallback implements ChallengeResourceProvider.ResourceProvidedCallback<Texture> {

        private final Image image;

        public ImageResourceCallback(Image image) {
            this.image = image;
        }

        @Override
        public void loaded(Texture resource) {
            resource.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            image.setDrawable(new TextureRegionDrawable(
                    new TextureRegion(resource)));
            image.pack();
        }

        @Override
        public void failed() {

        }
    }

    @Override
    public void init(TextChallenge challenge) {
        root.clear();

        this.challenge = challenge;

        TextControl textControl = challenge.getTextControl();
        Label text = new Label(textControl.getText(), skin, SkinConstants.STYLE_TOAST);
        text.setWrap(true);

        Container imageContainer;

        float defaultPad = es.eucm.cytochallenge.view.widgets.WidgetBuilder.dpToPixels(24f);

        if (textControl instanceof MultipleAnswerControl) {

            Table right = new Table();
            right.pad(defaultPad);
            right.add(text).fillX().expandX();
            final Image image = new Image();
            final SlideEditor slideEditor = new SlideEditor(skin);
            slideEditor.setRootActor(image);
            challengeResourceProvider.getTexture(challenge.getImagePath(), new ImageResourceCallback(image) {
                @Override
                public void loaded(Texture resource) {
                    super.loaded(resource);
                    slideEditor.setRootActor(image);
                }
            });
            image.setScaling(Scaling.fit);

            MultipleAnswerControl multipleAnswerControl = ((MultipleAnswerControl) textControl);
            String[] answers = multipleAnswerControl.getAnswers();
            final LinearLayout answersLayout = new LinearLayout(false);

            group.clear();
            group.setMaxCheckCount(1);

            for (int i = 0; i < answers.length; i++) {
                String answer = answers[i];
                final TextButton answerLabel = new TextButton((i + 1) + " - " + answer, skin,
                        SkinConstants.STYLE_MULTIPLE_CHOICE);
                answerLabel.setUserObject(answer);
                answerLabel.pad(defaultPad);
                answerLabel.getLabel().setAlignment(Align.left);
                answersLayout.add(answerLabel).expandX();

                group.add(answerLabel);
            }

            right.row();
            right.add(answersLayout).expand();


            imageContainer = new Container();
            imageContainer.setActor(slideEditor);
            imageContainer.fill();
            imageContainer.setClip(true);

            Table rootTable = new Table();
            rootTable.pad(defaultPad);
            rootTable.defaults().expand().fill().prefWidth(Value.percentWidth(0.5f, root));
            rootTable.add(imageContainer);
            rootTable.add(right);


            root.add(rootTable).expand().fill();

        }


        // MICQ
        else if (textControl instanceof MultipleImageAnswerControl) {
            imageGroup.clear();

            MultipleImageAnswerControl miaControl = (MultipleImageAnswerControl) textControl;
            String[] answers = miaControl.getAnswers();

            final Table rootTable = new Table();

            imageGroup.setMaxCheckCount(answers.length);

            final SlideEditor slideEditor = new SlideEditor(skin);
            slideEditor.setRootActor(rootTable);

            for (int i = 0; i < answers.length; i++) {
                String answer = answers[i];

                final Button imageButton = new Button(skin.get(SkinConstants.STYLE_CHECK,
                        Button.ButtonStyle.class));
                final Image imageActor = new Image();

                final float pad8 = es.eucm.cytochallenge.view.widgets.WidgetBuilder.dpToPixels(Gdx.graphics.getHeight() * .1f);
                imageButton.add(imageActor).expand().fill().pad(pad8);

                challengeResourceProvider.getTexture(answer, new ImageResourceCallback(imageActor) {
                    @Override
                    public void loaded(Texture resource) {
                        super.loaded(resource);

                        rootTable.setSize(rootTable.getPrefWidth(), rootTable.getPrefHeight());

                        slideEditor.setRootActor(rootTable);
                    }
                });

                imageActor.setScaling(Scaling.fit);

                imageButton.setClip(true);
                imageButton.setUserObject(false);

                imageGroup.add(imageButton);

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

            imageContainer = new Container();
            imageContainer.setActor(slideEditor);
            imageContainer.fill();
            imageContainer.setClip(true);

            Table topTable = new Table();
            topTable.background(skin.getDrawable(SkinConstants.DRAWABLE_9P_TOOLBAR));
            topTable.setColor(SkinConstants.COLOR_TOOLBAR_TOP);

            Label label = new Label(textControl.getText(), skin,
                    SkinConstants.STYLE_TOOLBAR);

            ScrollPane horizontalScroll = new ScrollPane(label);
            horizontalScroll.setScrollingDisabled(false, true);

            Button icon = es.eucm.cytochallenge.view.widgets.WidgetBuilder.toolbarIcon(SkinConstants.IC_UNDO);
            icon.setColor(Color.CLEAR);
            topTable.add(icon);

            topTable.add(horizontalScroll)
                    .expandX();

            TopToolbarLayout layout = new TopToolbarLayout();
            layout.setTopToolbar(topTable);
            layout.setContainer(imageContainer);

            root.add(layout).expand().fill();

        }


        // DND
        else if (textControl instanceof DragAndDropControl) {
            dragContainers.clear();

            DragAndDropControl dndControl = (DragAndDropControl) textControl;

            DragAndDropAnswer[] answers = dndControl.getAnswers();

            final DragAndDrop dnd = new DragAndDrop();

            final Image imageActor = new Image();

            final Group imageGroup = new Group();
            imageGroup.addActor(imageActor);

            final SlideEditor slideEditor = new SlideEditor(skin);
            slideEditor.setRootActor(imageGroup);
            challengeResourceProvider.getTexture(challenge.getImagePath(), new ImageResourceCallback(imageActor) {
                @Override
                public void loaded(Texture resource) {
                    super.loaded(resource);

                    imageGroup.setSize(Math.max(imageActor.getWidth(), imageGroup.getWidth()), Math.max(imageActor.getHeight(), imageGroup.getHeight()));
                    slideEditor.setRootActor(imageGroup);
                    slideEditor.setAlign(Align.center);
                }

            });
            imageActor.setScaling(Scaling.fit);

            float maxWidth = 0f, maxHeight = 0f;
            float maxX = imageActor.getWidth(), maxY = imageActor.getHeight();
            float minX = 0f, minY = 0f;

            shuffleArray(answers);

            for (int i = 0; i < answers.length; i++) {
                final DragAndDropAnswer answer = answers[i];


                final WidgetGroup parentContainer = new WidgetGroup();
                parentContainer.setFillParent(true);
                parentContainer.setTouchable(Touchable.childrenOnly);
                dragContainers.add(parentContainer);

                // Correct Answer stored inside the parent
                parentContainer.setUserObject(answer.getText());

                final TextButton answerLabel = new TextButton(answer.getText(), skin, SkinConstants.STYLE_DRAGANDDROP);
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

            imageGroup.setBounds(minX, minY, maxX - minX, maxY - minY);


            for (int j = 0; j < dragContainers.size; j++) {
                WidgetGroup widgetGroup = dragContainers.get(j);
                TextButton first = (TextButton) widgetGroup.getChildren().first();
                first.setText("");
                first.setBounds(first.getX() + (first.getWidth() - maxWidth) * .5f,
                        first.getY() + (first.getHeight() - maxHeight) * .5f,
                        maxWidth, maxHeight);
            }


            imageContainer = new Container();
            imageContainer.setActor(slideEditor);
            imageContainer.fill();
            imageContainer.setClip(true);

            final Table panelTable = new Table();
            panelTable.pad(defaultPad);
            panelTable.defaults().space(defaultPad);

            for (int i = 0; i < answers.length; i++) {
                DragAndDropAnswer correctAnswer = answers[i];
                final TextButton answerLabel = new TextButton(correctAnswer.getText(), skin,
                        SkinConstants.STYLE_DRAGANDDROP);

                // start DnD
                dnd.addSource(new DragAndDrop.Source(answerLabel) {
                    private final DragAndDrop.Payload payload = new DragAndDrop.Payload();
                    private float x, y;

                    @Override
                    public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                        this.x = answerLabel.getX();
                        this.y = answerLabel.getY();

                        dnd.setDragActorPosition(-answerLabel.getWidth() * .5f, answerLabel.getHeight() * .5f);

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

                            if (payload.getObject() == panelTable) {
                                if (((TextButton) targetActor).getLabel().getText().toString().isEmpty()) {
                                    targetActor.remove();
                                } else {
                                    panelTable.add(targetActor);
                                    panelTable.row();
                                }
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
            panelTable.background(skin.getDrawable(SkinConstants.DRAWABLE_9P_PAGE_RIGHT));
            panelTable.setColor(SkinConstants.COLOR_PANEL_RIGHT);

            RightToolbarLayout layout = new RightToolbarLayout();
            layout.setRightToolbar(scroll);
            layout.setContainer(imageContainer);

            root.add(layout).expand().fill();
        }


        // FtB
        else if (textControl instanceof FillTheBlankControl) {
            ftbTexts.clear();

            FillTheBlankControl dndControl = (FillTheBlankControl) textControl;

            Table verticalLayout = new Table();
            verticalLayout.pad(es.eucm.cytochallenge.view.widgets.WidgetBuilder.dpToPixels(48f));
            FillTheBlankStatement[] statements = dndControl.getStatements();

            for (int i = 0; i < statements.length; i++) {
                FillTheBlankStatement statement = statements[i];

                FillTheBlankText fillText = new FillTheBlankText(skin);
                fillText.init(statement);
                ftbTexts.add(fillText);

                verticalLayout.add(fillText).expandX().left();
                verticalLayout.row();
            }

            ScrollPane scroll = new ScrollPane(verticalLayout);
            scroll.setScrollingDisabled(true, false);

            Table topTable = new Table();
            topTable.background(skin.getDrawable(SkinConstants.DRAWABLE_9P_TOOLBAR));
            topTable.setColor(SkinConstants.COLOR_TOOLBAR_TOP);

            Label label = new Label(textControl.getText(), skin,
                    SkinConstants.STYLE_TOOLBAR);

            ScrollPane horizontalScroll = new ScrollPane(label);
            horizontalScroll.setScrollingDisabled(false, true);

            Button icon = es.eucm.cytochallenge.view.widgets.WidgetBuilder.toolbarIcon(SkinConstants.IC_UNDO);
            icon.setColor(Color.CLEAR);
            topTable.add(icon);

            topTable.add(horizontalScroll)
                    .expandX();

            TopToolbarLayout layout = new TopToolbarLayout();
            layout.setTopToolbar(topTable);
            layout.setContainer(scroll);

            root.add(layout).expand().fill();

        }

        // InteractiveZone
        else if (textControl instanceof InteractiveZoneControl) {

            markers.clear();

            final Image imageActor = new Image();
            imageActor.setScaling(Scaling.fit);

            final Group imageGroup = new Group();
            imageGroup.addActor(imageActor);

            final SlideEditor slideEditor = new SlideEditor(skin);
            slideEditor.setRootActor(imageGroup);
            challengeResourceProvider.getTexture(challenge.getImagePath(), new ImageResourceCallback(imageActor) {
                @Override
                public void loaded(Texture resource) {
                    super.loaded(resource);

                    imageGroup.setBounds(0, 0, imageActor.getWidth(), imageActor.getHeight());
                    slideEditor.setRootActor(imageGroup);
                }
            });
            imageActor.addListener(new DragListener() {

                boolean dragged;

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    dragged = false;
                    return super.touchDown(event, x, y, pointer, button);
                }

                @Override
                public void dragStart(InputEvent event, float x, float y, int pointer) {
                    super.dragStart(event, x, y, pointer);
                    dragged = true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    if (!dragged) {
                        clicked(event, x, y);
                    }
                }

                public void clicked(InputEvent event, float x, float y) {
                    final IconButton mapMarker = es.eucm.cytochallenge.view.widgets.WidgetBuilder.toolbarIcon(SkinConstants.IC_MAPMARKER);
                    mapMarker.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            mapMarker.remove();
                            markers.removeValue(mapMarker, true);
                        }
                    });
                    imageGroup.addActor(mapMarker);
                    mapMarker.pack();
                    mapMarker.setOrigin(Align.center);
                    mapMarker.setPosition(x - mapMarker.getWidth() * .5f, y - mapMarker.getHeight() * .5f);
                    markers.add(mapMarker);
                }
            });
            imageContainer = new Container();
            imageContainer.setActor(slideEditor);
            imageContainer.fill();
            imageContainer.setClip(true);

            Table topTable = new Table();
            topTable.background(skin.getDrawable(SkinConstants.DRAWABLE_9P_TOOLBAR));
            topTable.setColor(SkinConstants.COLOR_TOOLBAR_TOP);

            Label label = new Label(textControl.getText(), skin,
                    SkinConstants.STYLE_TOOLBAR);

            ScrollPane horizontalScroll = new ScrollPane(label);
            horizontalScroll.setScrollingDisabled(false, true);

            Button icon = es.eucm.cytochallenge.view.widgets.WidgetBuilder.toolbarIcon(SkinConstants.IC_UNDO);
            icon.setColor(Color.CLEAR);
            topTable.add(icon);

            topTable.add(horizontalScroll)
                    .expandX();

            TopToolbarLayout layout = new TopToolbarLayout();
            layout.setTopToolbar(topTable);
            layout.setContainer(imageContainer);

            root.add(layout).expand().fill();

        }
    }

    // Implementing FisherYates shuffle
    static void shuffleArray(DragAndDropAnswer[] ar) {
        for (int i = ar.length - 1; i > 0; --i) {
            int index = MathUtils.random(i);
            // Simple swap
            DragAndDropAnswer a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }


    public void setUpScore() {


        TextControl textControl = challenge.getTextControl();
        if (textControl instanceof MultipleAnswerControl) {
            MultipleAnswerControl multipleAnswerControl = ((MultipleAnswerControl) textControl);

            Table actor = (Table) root.getChildren().get(0);
            actor.getChildren().get(1).setTouchable(Touchable.disabled);
            MultipleAnswerResult result = new MultipleAnswerResult(skin,
                    multipleAnswerControl, i18n, group, actor);

            root.clear();
            root.add(result).expand().fill();

            if(completedListener != null) {
                completedListener.completed(challenge.getId(), result.getScore());
            }
        }

        // MultipleImageAnswerControl
        else if (textControl instanceof MultipleImageAnswerControl) {


            TopToolbarLayout actor = (TopToolbarLayout) root.getChildren().get(0);
            Container container = (Container) actor.getContainer();

            SlideEditor editor = (SlideEditor) container.getActor();
            Table rootRightTable = (Table) editor.getRootActor();
            rootRightTable.setTouchable(Touchable.disabled);

            MultipleImageAnswerResult result = new MultipleImageAnswerResult(skin,
                    (MultipleImageAnswerControl) textControl, i18n, imageGroup, editor);

            root.clear();
            root.add(result).expand().fill();

            if(completedListener != null) {
                completedListener.completed(challenge.getId(), result.getScore());
            }
        }

        // DragAndDropControl
        else if (textControl instanceof DragAndDropControl) {
            RightToolbarLayout actor = (RightToolbarLayout) root.getChildren().get(0);
            Container container = (Container) actor.getContainer();

            SlideEditor editor = (SlideEditor) container.getActor();
            Group rootRightTable = (Group) editor.getRootActor();
            rootRightTable.setTouchable(Touchable.disabled);

            DragAndDropResult result = new DragAndDropResult(skin,
                    (DragAndDropControl) textControl, i18n, dragContainers, editor);

            root.clear();
            root.add(result).expand().fill();

            if(completedListener != null) {
                completedListener.completed(challenge.getId(), result.getScore());
            }
        }

        // FillTheBlankControl
        else if (textControl instanceof FillTheBlankControl) {
            FillTheBlankControl multipleAnswerControl = ((FillTheBlankControl) textControl);

            TopToolbarLayout actor = (TopToolbarLayout) root.getChildren().get(0);
            ScrollPane container = (ScrollPane) actor.getContainer();
            container.getWidget().setTouchable(Touchable.disabled);
            FillTheBlankResult result = new FillTheBlankResult(skin,
                    multipleAnswerControl, i18n, ftbTexts, container);

            root.clear();
            root.add(result).expand().fill();

            if(completedListener != null) {
                completedListener.completed(challenge.getId(), result.getScore());
            }
        }

        // InteractiveZone
        else if (textControl instanceof InteractiveZoneControl) {
            TopToolbarLayout actor = (TopToolbarLayout) root.getChildren().get(0);
            Container container = (Container) actor.getContainer();

            SlideEditor editor = (SlideEditor) container.getActor();
            Group rootRightTable = (Group) editor.getRootActor();
            rootRightTable.setTouchable(Touchable.disabled);

            InteractiveZoneResult result = new InteractiveZoneResult(skin,
                    (InteractiveZoneControl) textControl, i18n, markers, editor);

            root.clear();
            root.add(result).expand().fill();

            if(completedListener != null) {
                completedListener.completed(challenge.getId(), result.getScore());
            }
        }
    }

    @Override
    public Actor getWidget() {
        return root;
    }

    public TextChallenge getChallenge() {
        return challenge;
    }

    public interface CompletedListener {
         void completed(String challengeId, float score);
    }

    public void setCompletedListener(CompletedListener completedListener) {
        this.completedListener = completedListener;
    }
}
