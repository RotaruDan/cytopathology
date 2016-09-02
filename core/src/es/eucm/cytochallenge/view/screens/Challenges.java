package es.eucm.cytochallenge.view.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import es.eucm.cytochallenge.model.*;
import es.eucm.cytochallenge.model.course.Course;
import es.eucm.cytochallenge.model.hint.Hint;
import es.eucm.cytochallenge.utils.ChallengeResourceProvider;
import es.eucm.cytochallenge.utils.InternalFilesChallengeResourceProvider;
import es.eucm.cytochallenge.view.*;
import es.eucm.cytochallenge.view.transitions.Fade;
import es.eucm.cytochallenge.view.widgets.HintDialog;
import es.eucm.cytochallenge.view.widgets.Toast;
import es.eucm.cytochallenge.view.widgets.WidgetBuilder;
import es.eucm.cytochallenge.view.widgets.challenge.ChallengeLayout;
import es.eucm.cytochallenge.view.widgets.challenge.TextChallengeWidget;

public class Challenges extends BaseScreen {

    private ChallengeLayout challengeLayout = new ChallengeLayout();
    private TextChallengeWidget currentChallenge;
    private Button check, hint;
    private Button addButton;
    private ChallengeResourceProvider challengeResourceProvider;
    private Course currentCourse;
    private int challengeCount = 0;
    private ImageButton nextChallenge;

    public void setCurrentCourse(Course currentCourse) {
        this.currentCourse = currentCourse;
        challengeCount = 0;
    }

    @Override
    public void create() {
        super.create();
        final Button back;

        back = es.eucm.cytochallenge.view.widgets.WidgetBuilder.toolbarIcon(SkinConstants.IC_UNDO);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onBackPressed();
            }
        });

        hint = WidgetBuilder.circleButton(SkinConstants.IC_ERROR);
        hint.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Hint hint = currentChallenge.getChallenge().getHint();
                if (hint != null) {
                    HintDialog dialog = new HintDialog(skin, hint, i18n, challengeResourceProvider);
                    menu.getStage().addActor(dialog);
                    dialog.show();
                }
            }
        });

        nextChallenge = WidgetBuilder.circleButton(SkinConstants.IC_ARROW);
        nextChallenge.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (currentCourse.getChallenges().size == challengeCount) {
                    challengeLayout.setNextChallenge(null);
                    challengeList.setShowCourseInfo(true);
                    game.changeScreen(challengeList);
                } else {
                    challengeLayout.setNextChallenge(null);
                    game.changeScreen(challenges);
                }
            }
        });

        check = WidgetBuilder.circleButton(SkinConstants.IC_CHECK);
        addButton = check;
        check.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                checkChallenge();
            }
        });

        challengeLayout.setCheckButton(addButton);
        challengeLayout.setBackButton(back);

        root.add(challengeLayout).expand().fill();
    }

    private void checkChallenge() {
        currentChallenge.setUpScore();
        addButton = hint;
        check.remove();
        Hint hintInfo = currentChallenge.getChallenge().getHint();
        if (hintInfo != null) {
            challengeLayout.setCheckButton(hint);
        }
        if (currentCourse != null) {
            challengeLayout.setNextChallenge(nextChallenge);
        }
        TimerWidget timer = challengeLayout.getTimer();
        if (timer != null) {
            timer.stop();
        }
    }

    @Override
    public void show() {
        super.show();

        if (currentCourse != null &&
                currentCourse.getChallenges().size > 0 &&
                (challengeResourceProvider instanceof InternalFilesChallengeResourceProvider)) {
            if (challengeCount >= currentCourse.getChallenges().size) {
                challengeCount = 0;
            }
            String currentChallengeId = currentCourse.getChallenges().get(challengeCount);
            String currentChallenge = "challenges/" + currentChallengeId + "/";
            challengeCount++;
            InternalFilesChallengeResourceProvider internalChallengeResourceProvider =
                    (InternalFilesChallengeResourceProvider) challengeResourceProvider;
            internalChallengeResourceProvider.setResourcePath(currentChallenge);
            internalChallengeResourceProvider.setCurrentChallengeId(currentChallengeId);
        }

        if (currentCourse == null) {
            challengeLayout.setNextChallenge(null);
        }

        challengeResourceProvider.getChallenge("challenge.json", new ChallengeResourceProvider.ResourceProvidedCallback<Challenge>() {
            @Override
            public void loaded(Challenge resource) {

                resource.setId(((InternalFilesChallengeResourceProvider) challengeResourceProvider).getCurrentChallengeId());

                if (currentChallenge != null) {
                    currentChallenge.getWidget().remove();
                }
                currentChallenge = new TextChallengeWidget(skin, i18n);
                currentChallenge.setCompletedListener(new TextChallengeWidget.CompletedListener() {
                    @Override
                    public void completed(String challengeId, float score) {
                        if (currentCourse != null) {
                            prefs.saveCourseChallengeScore(challengeId, score);
                        } else {
                            prefs.saveChallengeScore(challengeId, score);
                        }
                    }
                });
                currentChallenge.setChallengeResourceProvider(challengeResourceProvider);
                currentChallenge.init((TextChallenge) resource);

                challengeLayout.setContent(currentChallenge.getWidget());

                addButton = check;
                challengeLayout.setCheckButton(check);
            }

            @Override
            public void failed() {

            }
        });

        if (currentChallenge.getChallenge().getDifficulty() == Difficulty.EASY) {

        } else if (currentChallenge.getChallenge().getDifficulty() == Difficulty.MEDIUM) {
            buildTimer(15.5f);
        } else {
            buildTimer(10.5f);
        }
    }

    private void buildTimer(float startTime) {
        TimerWidget timer = new TimerWidget("", skin);
        timer.setTime(startTime);
        challengeLayout.setTimer(timer);
        timer.setOnTimeoutListener(new Runnable() {
            @Override
            public void run() {
                stage.addAction(Actions.delay(.5f, Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = new Toast(skin);
                        toast.setText(i18n.get("timeout"));
                        stage.addActor(toast);
                        toast.show();
                        challengeLayout.setTimer(null);
                    }
                })));
                checkChallenge();
            }
        });
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void hide() {
        super.hide();
        challengeLayout.setNextChallenge(null);
        challengeLayout.setTimer(null);
    }

    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public void onBackPressed() {
        if (challengeList != null) {
            game.changeScreen(challengeList, Fade.init(1f, true));
        } else {
            game.changeScreen(challenges, Fade.init(1f, true));
        }
    }

    public void setChallengeResourceProvider(ChallengeResourceProvider challengeResourceProvider) {
        this.challengeResourceProvider = challengeResourceProvider;
    }
}