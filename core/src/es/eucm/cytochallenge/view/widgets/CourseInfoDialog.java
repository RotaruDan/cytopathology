package es.eucm.cytochallenge.view.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Scaling;
import es.eucm.cytochallenge.model.Challenge;
import es.eucm.cytochallenge.model.course.Course;
import es.eucm.cytochallenge.model.hint.Hint;
import es.eucm.cytochallenge.model.hint.ImageInfo;
import es.eucm.cytochallenge.model.hint.Info;
import es.eucm.cytochallenge.model.hint.TextInfo;
import es.eucm.cytochallenge.utils.ChallengeResourceProvider;
import es.eucm.cytochallenge.view.screens.BaseScreen;

public class CourseInfoDialog extends Table {

    private Label.LabelStyle textStyle;
    private Table container;
    private Label title;


    public CourseInfoDialog(Skin skin, I18NBundle i18n) {
        CourseInfoDialogStyle style = skin.get(CourseInfoDialogStyle.class);
        background(style.background);
        float pad24dp = WidgetBuilder.dpToPixels(24);
        float pad16dp = WidgetBuilder.dpToPixels(16);
        pad(pad24dp, pad24dp, pad16dp, pad24dp);

        textStyle = style.textStyle;
        title = new Label("", textStyle);
        container = new Table();
        container.defaults().space(pad24dp);
        container.pad(pad16dp);
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

        add(title).expandX().fill();
        row();
        add(scroll).expand().fill();
        row();
        add(ok).expandX().right().padTop(pad16dp);
        setFillParent(true);
    }

    public void clearChallenges() {
        container.clearChildren();
    }

    public void init(Course course) {
        clearChallenges();
        title.setText(course.getName());
    }

    public void addChallenge(Challenge challenge) {

        Label textLabel = new Label(BaseScreen.prefs.getCourseChallengeScore(challenge.getId()) + "", textStyle);
        textLabel.setWrap(true);
        container.add(textLabel).expandX().fillX();
        container.row();
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        Actor hit = super.hit(x, y, touchable);
        return hit == null ? this : hit;
    }

    @Override
    public void layout() {
        super.layout();
    }

    public void show() {
        System.out.println("show");
        float y = getY();
        setY(Gdx.graphics.getHeight());
        clearActions();
        addAction(Actions.moveTo(0, y, 0.33f, Interpolation.exp5Out));
    }

    public void hide() {
        System.out.println("hide");
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