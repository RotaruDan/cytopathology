package es.eucm.cytochallenge.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
import es.eucm.cytochallenge.view.widgets.challenge.ChallengeTile;

public class ChallengeList extends BaseScreen {

    private InternalFilesChallengeResourceProvider challengeResourceProvider = new InternalFilesChallengeResourceProvider();

    private boolean showCourseInfo = false;
    private Course currentCourse;
    private Gallery gallery;
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

        gallery = new Gallery(2.26f, 3, skin.get("navigation", Gallery.GalleryStyle.class));
        courseDialog = new CourseInfoDialog(skin, i18n, resolver);

        Table topTable = new Table();
        topTable.background(BaseScreen.skin.getDrawable(SkinConstants.DRAWABLE_9P_TOOLBAR));
        topTable.setColor(SkinConstants.COLOR_TOOLBAR_TOP_DARK);

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
        float dpSpace = WidgetBuilder.dp8ToPixels();
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
        rootLayout.setContainer(gallery);
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

        StartCourseDialog startDialog = new StartCourseDialog(currentCourse, skin, i18n, resolver);
        startDialog.setOnFinishedCallback(new Runnable() {
            @Override
            public void run() {
                challenges.setCurrentCourse(currentCourse);
                challenges.setChallengeResourceProvider(challengeResourceProvider);
                game.changeScreen(challenges);
            }
        });
        menu.getStage().addActor(startDialog);
        startDialog.show();
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
        gallery.clearChildren();
        loadChallenges();

    }

    private void loadChallenges() {

        Json json = new Json();
        float pad = WidgetBuilder.dp48ToPixels();
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

                        final ChallengeTile tile = new ChallengeTile(challenge, skin, i18n, prefs);

                        tile.setUserObject(challengeFolder);
                        tile.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                if (tile.isDisabled()) {
                                    playCourseToast();
                                } else {
                                    challenges.setCurrentCourse(null);
                                    challengeResourceProvider.setResourcePath(tile.getUserObject().toString());
                                    challengeResourceProvider.setCurrentChallengeId(challengeId);
                                    challenges.setChallengeResourceProvider(challengeResourceProvider);
                                    game.changeScreen(challenges);
                                }
                            }
                        });

                        gallery.add(tile);
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


        Gdx.gl.glClearColor(SkinConstants.COLOR_TOOLBAR_TOP_DARK.r, SkinConstants.COLOR_TOOLBAR_TOP_DARK.g,
                SkinConstants.COLOR_TOOLBAR_TOP_DARK.b, SkinConstants.COLOR_TOOLBAR_TOP_DARK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.draw();
    }

    @Override
    public void onBackPressed() {
        game.changeScreen(courseList, Fade.init(1f));
    }

    public void setShowCourseInfo(boolean showCourseInfo) {
        this.showCourseInfo = showCourseInfo;
    }
}