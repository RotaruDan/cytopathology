package es.eucm.cytochallenge.view.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Scaling;
import es.eucm.cytochallenge.model.Challenge;
import es.eucm.cytochallenge.model.TextChallenge;
import es.eucm.cytochallenge.model.course.Course;
import es.eucm.cytochallenge.model.hint.Hint;
import es.eucm.cytochallenge.model.hint.ImageInfo;
import es.eucm.cytochallenge.model.hint.Info;
import es.eucm.cytochallenge.model.hint.TextInfo;
import es.eucm.cytochallenge.utils.ChallengeResourceProvider;
import es.eucm.cytochallenge.utils.Grades;
import es.eucm.cytochallenge.utils.Resolver;
import es.eucm.cytochallenge.view.screens.BaseScreen;

import java.util.Date;

public class CourseInfoDialog extends Table {

    private final I18NBundle i18n;
    private Resolver resolver;
    private Label.LabelStyle textStyle;
    private Table container;
    private Label title, totalScore;
    private int challenges;
    private float totalChallengeScore;
    private long totalCourseTime;

    public CourseInfoDialog(Skin skin, I18NBundle i18n, Resolver resolver) {
        setSkin(skin);
        this.resolver = resolver;
        this.i18n = i18n;
        CourseInfoDialogStyle style = skin.get(CourseInfoDialogStyle.class);
        background(style.background);
        float pad16dp = WidgetBuilder.dp16ToPixels();
        float pad8dp = WidgetBuilder.dp8ToPixels();
        pad(pad16dp, pad16dp, pad8dp, pad16dp);

        textStyle = style.textStyle;
        title = new Label("", textStyle);
        container = new Table();
        container.defaults().space(pad16dp);
        container.pad(pad8dp);
        ScrollPane scroll = new ScrollPane(container);
        scroll.setScrollingDisabled(true, false);

        TextButton ok = WidgetBuilder.dialogButton(i18n.get("ok"),
                style.buttonStyle);
        ok.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });

        add(title);
        row();
        add(scroll).expand().fill();
        row();
        add(ok).expandX().right().padTop(pad8dp);
        setFillParent(true);

        totalScore = new Label("", skin);
        totalScore.setAlignment(Align.right);
        totalCourseTime = 0l;
    }

    public void clearChallenges() {
        totalChallengeScore = 0f;
        challenges = 0;
        container.clearChildren();
        container.add(totalScore).expandX().fillX().width(Gdx.graphics.getWidth() * .95f).center();
    }

    public void init(Course course) {
        clearChallenges();
        totalCourseTime = 0l;
        title.setText(i18n.get("exam") + ": " + course.getName());
    }

    public void addChallenge(Challenge challenge, I18NBundle i18n) {
        ChallengeButton challengeButton = new ChallengeButton(challenge, getSkin(), true, "dialog", i18n, resolver);
        challengeButton.setTouchable(Touchable.disabled);
        container.getCell(totalScore).setActor(challengeButton).expandX().fillX().width(Gdx.graphics.getWidth() * .95f).center();
        container.row();
        container.add(totalScore).expandX().width(Gdx.graphics.getWidth() * .95f).right();

        totalChallengeScore += BaseScreen.prefs.getChallengeScore(challenge.getId());
        ++challenges;

        totalCourseTime += BaseScreen.prefs.getChallengeTime(challenge.getId());

        int finalScore = MathUtils.round(totalChallengeScore / challenges);
        totalScore.setText(i18n.get("total") + ":    "
                + finalScore + "    " +
                Grades.getGrade(finalScore) + "    " + resolver.format("mm:ss", new Date(totalCourseTime)));
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        Actor hit = super.hit(x, y, touchable);
        return hit == null ? this : hit;
    }

    public void show() {
        float y = 0;
        setY(Gdx.graphics.getHeight());
        clearActions();
        addAction(Actions.moveTo(0, y, 0.33f, Interpolation.exp5Out));
    }

    public void hide() {
        addAction(Actions.sequence(Actions.moveTo(0, Gdx.graphics.getHeight(),
                0.33f, Interpolation.exp5Out), Actions.run(new Runnable() {
            @Override
            public void run() {
                remove();
            }
        })));
    }


    public static class CourseInfoDialogStyle {

        public Drawable background;

        public Label.LabelStyle textStyle;

        public TextButton.TextButtonStyle buttonStyle;

    }
}