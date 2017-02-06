package es.eucm.cytochallenge.view.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import es.eucm.cytochallenge.model.course.Course;
import es.eucm.cytochallenge.utils.Resolver;

import java.util.Date;

public class StartCourseDialog extends Container<Table> {

    private Runnable onFinished;

    public StartCourseDialog(Course course, final Skin skin, final I18NBundle i18n, Resolver resolver) {
        super(new Table());
        StartCourseDialogStyle style = skin.get(StartCourseDialogStyle.class);
        background(style.background);
        center();
        Table root = getActor();
        root.setBackground(style.panelBackground);

        float pad24dp = WidgetBuilder.dp24ToPixels();
        float pad48dp = WidgetBuilder.dp48ToPixels();
        root.pad(pad48dp, pad48dp, pad48dp, pad48dp);

        root.defaults().space(pad24dp);

        int courseCompletionTime = course.getCourseCompletionTime();
        int courseSize = course.getChallenges().size;
        String courseSizeMessage = null;
        if (courseSize > 1) {
            courseSizeMessage = i18n.format("examModeStart", courseSize);
        } else if (courseSize == 1) {
            courseSizeMessage = i18n.format("examModeStart1", courseSize);
        }

        Label scoreLabel = new Label(courseSizeMessage, style.textStyle);
        scoreLabel.setWrap(true);
        scoreLabel.setWidth(Gdx.graphics.getWidth() * .5f);
        root.add(scoreLabel).width(scoreLabel.getWidth());
        root.row();

        if (courseCompletionTime > 0) {
            Date date = new Date(courseCompletionTime);
            String availableTimeMessage = i18n.get("availableTime") + " - " + resolver.format("mm:ss", date);
            Label timeLabel = new Label(availableTimeMessage, style.textStyle);
            timeLabel.setWrap(true);
            timeLabel.setWidth(Gdx.graphics.getWidth() * .5f);
            root.add(timeLabel).width(timeLabel.getWidth());
        }

        setFillParent(true);
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        Actor hit = super.hit(x, y, touchable);
        return hit == null ? this : hit;
    }

    public void show() {

        pack();

        final Table root = getActor();
        root.pack();

        clearActions();
        getColor().a = 0f;
        addAction(Actions.alpha(1f, .33f));

        float y = root.getY();
        root.setY(Gdx.graphics.getHeight());
        root.addAction(Actions.sequence(Actions.moveTo(root.getX(), y, .6f, Interpolation.swingOut),
                Actions.delay(1.8f, Actions.sequence(Actions.moveTo(root.getX(), -root.getHeight(), .6f, Interpolation.swingIn), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        remove();
                        if (onFinished != null) {
                            onFinished.run();
                        }
                    }
                })))));
    }

    public void setOnFinishedCallback(Runnable onFinished) {
        this.onFinished = onFinished;
    }


    public static class StartCourseDialogStyle {

        public Drawable background, panelBackground;

        public Label.LabelStyle textStyle;

    }
}