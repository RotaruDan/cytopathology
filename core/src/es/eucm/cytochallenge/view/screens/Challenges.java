package es.eucm.cytochallenge.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.*;
import es.eucm.cytochallenge.model.*;
import es.eucm.cytochallenge.model.control.filltheblank.FillTheBlankControl;
import es.eucm.cytochallenge.model.control.filltheblank.FillTheBlankStatement;
import es.eucm.cytochallenge.model.course.Course;
import es.eucm.cytochallenge.model.hint.Hint;
import es.eucm.cytochallenge.model.hint.ImageInfo;
import es.eucm.cytochallenge.model.hint.Info;
import es.eucm.cytochallenge.model.hint.TextInfo;
import es.eucm.cytochallenge.utils.ChallengeResourceProvider;
import es.eucm.cytochallenge.utils.InternalFilesChallengeResourceProvider;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.transitions.Fade;
import es.eucm.cytochallenge.view.widgets.CirclesMenu;
import es.eucm.cytochallenge.view.widgets.HintDialog;
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

        final Button nextChallenge = WidgetBuilder.circleButton(SkinConstants.IC_ARROW);
        nextChallenge.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(currentCourse.getChallenges().size == challengeCount) {
                    // TODO show final Course completion result
                    challengeLayout.setNextChallenge(null);
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
                currentChallenge.setUpScore();
                addButton = hint;
                check.remove();
                Hint hintInfo = currentChallenge.getChallenge().getHint();
                if (hintInfo != null) {
                    challengeLayout.setCheckButton(hint);
                }
                if(currentCourse != null) {
                    challengeLayout.setNextChallenge(nextChallenge);
                }
            }
        });

        challengeLayout.setCheckButton(addButton);
        challengeLayout.setBackButton(back);

        root.add(challengeLayout).expand().fill();
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
            String currentChallenge = "challenges/" + currentCourse.getChallenges().get(challengeCount) + "/";
            challengeCount++;
            InternalFilesChallengeResourceProvider internalChallengeResourceProvider =
                    (InternalFilesChallengeResourceProvider) challengeResourceProvider;
            internalChallengeResourceProvider.setResourcePath(currentChallenge);
        }

        challengeResourceProvider.getChallenge("challenge.json", new ChallengeResourceProvider.ResourceProvidedCallback<Challenge>() {
            @Override
            public void loaded(Challenge resource) {

                System.out.println("resource = " + resource);
                if(currentChallenge != null) {
                    currentChallenge.getWidget().remove();
                }
                currentChallenge = new TextChallengeWidget(skin, i18n);
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
    }

    @Override
    public void hide() {
        super.hide();
        challengeLayout.setNextChallenge(null);
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