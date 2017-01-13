package es.eucm.cytochallenge.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Scaling;
import es.eucm.cytochallenge.model.Challenge;
import es.eucm.cytochallenge.model.TextChallenge;
import es.eucm.cytochallenge.model.control.MultipleAnswerControl;
import es.eucm.cytochallenge.model.control.MultipleImageAnswerControl;
import es.eucm.cytochallenge.model.control.TextControl;
import es.eucm.cytochallenge.model.control.draganddrop.DragAndDropControl;
import es.eucm.cytochallenge.model.control.filltheblank.FillTheBlankControl;
import es.eucm.cytochallenge.model.course.Course;
import es.eucm.cytochallenge.utils.ChallengeResourceProvider;
import es.eucm.cytochallenge.utils.Grades;
import es.eucm.cytochallenge.utils.InternalFilesChallengeResourceProvider;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.transitions.Fade;
import es.eucm.cytochallenge.view.widgets.*;

public class ChallengeList extends BaseScreen {

    private InternalFilesChallengeResourceProvider challengeResourceProvider = new InternalFilesChallengeResourceProvider();

    private boolean showCourseInfo = false;
    private Course currentCourse;
    private Table layout;
    private Label title;
    private CourseInfoDialog courseDialog;
    private Button courseInfo;

    public void setCurrentCourse(Course currentCourse) {
        this.currentCourse = currentCourse;
        challenges.setCurrentCourse(currentCourse);
    }

    @Override
    public void create() {
        super.create();

        layout = new Table();
        courseDialog = new CourseInfoDialog(skin, i18n, resolver);

        ScrollPane scroll = new ScrollPane(layout, BaseScreen.skin, "verticalScroll");
        scroll.setScrollingDisabled(true, false);

        Table topTable = new Table();
        topTable.background(BaseScreen.skin.getDrawable(SkinConstants.DRAWABLE_9P_TOOLBAR));
        topTable.setColor(SkinConstants.COLOR_TOOLBAR_TOP);

        title = new Label("", BaseScreen.skin,
                SkinConstants.STYLE_TOOLBAR);

        Button icon = es.eucm.cytochallenge.view.widgets.WidgetBuilder.toolbarIcon(SkinConstants.IC_UNDO);
        icon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onBackPressed();
            }
        });


        Button play = es.eucm.cytochallenge.view.widgets.WidgetBuilder.circleButton(SkinConstants.IC_PLAY);
        play.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playCourse();
            }
        });
        float dpSpace = WidgetBuilder.dpToPixels(6f);
        play.setPosition(Gdx.graphics.getWidth() - play.getWidth() - dpSpace, dpSpace);

        courseInfo = es.eucm.cytochallenge.view.widgets.WidgetBuilder.toolbarIcon(SkinConstants.IC_ERROR);
        courseInfo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (currentCourse != null) {
                    showCourseDialog();
                }
            }
        });

        topTable.add(icon);
        topTable.add(title)
                .expandX();
        topTable.add(courseInfo);

        TopToolbarLayout rootLayout = new TopToolbarLayout();
        rootLayout.setTopToolbar(topTable);
        rootLayout.setContainer(scroll);
        rootLayout.setCheckButton(play);

        root.add(rootLayout).expand().fill();
    }

    private void showCourseDialog() {
        courseDialog.show();
        courseDialog.setVisible(true);
        menu.getStage().addActor(courseDialog);
    }

    private void playCourse() {
        if (currentCourse == null || currentCourse.getChallenges() == null) {
            return;
        }
        challenges.setCurrentCourse(currentCourse);
        challenges.setChallengeResourceProvider(challengeResourceProvider);
        game.changeScreen(challenges);
    }

    @Override
    public void show() {
        super.show();
        if (currentCourse != null) {
            title.setText(i18n.get("course") + ": " + currentCourse.getName());
            courseDialog.init(currentCourse);
            courseInfo.setVisible(true);
            if (showCourseInfo) {
                showCourseInfo = false;
                showCourseDialog();
            }
        } else {
            title.setText(i18n.get("challenges"));
            courseInfo.setVisible(false);
        }
        layout.clearChildren();
        loadChallenges(layout);

    }

    private void loadChallenges(final Table layout) {

        layout.top();
        Json json = new Json();
        float pad = WidgetBuilder.dpToPixels(54f);
        layout.pad(pad);
        final float maxWidth = Gdx.graphics.getWidth() - pad;
        String challengesPath = "challenges/";
        String challengeJson = "challenge.json";

        Array<String> challengesPaths;
        if (currentCourse == null) {
            challengesPaths = json.fromJson(Array.class,
                    Gdx.files.internal(challengesPath + "challenges.json"));
        } else {
            challengesPaths = currentCourse.getChallenges();
        }

        if (challengesPaths == null) {
            return;
        }

        for (int i = 0; i < challengesPaths.size; i++) {
            final String challengeId = challengesPaths.get(i);
            final String challengeFolder = challengesPath + challengeId + "/";
            challengeResourceProvider.setResourcePath(challengeFolder);
            challengeResourceProvider.getChallenge(challengeJson, new ChallengeResourceProvider.ResourceProvidedCallback<Challenge>() {
                @Override
                public void loaded(Challenge challenge) {
                    Gdx.app.log("Files", "json challenge: " + challenge.getImagePath());

                    if (challenge instanceof TextChallenge) {
                        challenge.setId(challengeId);
                        if (currentCourse != null) {
                            courseDialog.addChallenge(challenge, i18n);
                        }
                        TextChallenge textChallenge = (TextChallenge) challenge;

                        final Button button = new ChallengeButton(textChallenge,
                                skin, i18n, resolver);
                        button.setUserObject(challengeFolder);
                        Gdx.app.log("Files", "json challenge getTextControl: " + textChallenge.getTextControl());

                        button.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                if (button.isDisabled()) {
                                    playCourseToast();
                                } else {
                                    challenges.setCurrentCourse(null);
                                    challengeResourceProvider.setResourcePath(button.getUserObject().toString());
                                    challengeResourceProvider.setCurrentChallengeId(challengeId);
                                    challenges.setChallengeResourceProvider(challengeResourceProvider);
                                    game.changeScreen(challenges);
                                }
                            }
                        });

                        layout.add(button).left().fillX().width(maxWidth);
                        layout.row();
                    }
                }

                @Override
                public void failed() {

                }
            });

        }
    }

    private void playCourseToast() {
        Toast toast = new Toast(skin);
        toast.setText(i18n.get("playCourseToUnlockChallenge"));
        stage.addActor(toast);
        toast.show();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public void onBackPressed() {
        game.changeScreen(courseList, Fade.init(1f, true));
    }

    public void setShowCourseInfo(boolean showCourseInfo) {
        this.showCourseInfo = showCourseInfo;
    }
}