package es.eucm.cytochallenge.view.widgets.challenge;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import es.eucm.cytochallenge.model.Challenge;
import es.eucm.cytochallenge.model.course.Course;
import es.eucm.cytochallenge.utils.Grades;
import es.eucm.cytochallenge.utils.Prefs;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.screens.BaseScreen;
import es.eucm.cytochallenge.view.widgets.Tile;

import java.util.Date;

public class ChallengeTile extends Tile {

    private float score;
    private final Label grade;
    private Image star1, star2, star3;
    private boolean isDisabled;

    public ChallengeTile(Challenge challenge, Skin skin, I18NBundle i18n, Prefs prefs) {
        super(skin);

        setText(challenge.getName());
        setMarker(es.eucm.cytochallenge.view.widgets.WidgetBuilder
                .difficulty(challenge.getDifficulty(), i18n));

        grade = new Label(Grades.getGrade(score = prefs.getChallengeScore(challenge.getId())), skin);
        labelContainer.add(grade);
        invalidateHierarchy();
        setUpStars(skin);


        boolean disabled = true;
        long timeValue = BaseScreen.prefs.getChallengeTime(challenge.getId());
        if (timeValue > 0l) {
            disabled = false;
        }
        setDisabled(disabled);
        if(disabled) {
            label.setColor(Color.LIGHT_GRAY);
            grade.setColor(Color.LIGHT_GRAY);
            setColor(Color.LIGHT_GRAY);
        }
    }


    public boolean isDisabled () {
        return isDisabled;
    }

    public void setDisabled (boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    @Override
    public void layout() {
        super.layout();
        labelContainer.getCell(label).width((labelContainer.getWidth() - grade.getWidth()) * .92f);

        float starY = getHeight() - marker.getHeight();
        if (star3 != null) {
            star3.setPosition(getWidth() - star3.getWidth(), starY - star3.getHeight());
        }
        if (star2 != null) {
            star2.setPosition(getWidth() - star2.getWidth() * 2f, starY - star2.getHeight());
        }
        if (star1 != null) {
            star1.setPosition(getWidth() - star1.getWidth() * 3f, starY - star1.getHeight());
        }
    }

    private void setUpStars(Skin skin) {

        Drawable starDrawable = skin.getDrawable(SkinConstants.DRAWABLE_SMALL_STAR);
        int stars = Grades.getStars(score);

        if (stars > 2) {
            star3 = new Image(starDrawable);
            star3.setColor(Color.YELLOW);
            star3.setOrigin(Align.center);
            star3.setScale(0f);
            star3.addAction(Actions.sequence(Actions.delay(1.2f), Actions.scaleTo(1f, 1f, .75f, Interpolation.swingOut)));
            addActor(star3);

        }
        if (stars > 1) {
            star2 = new Image(starDrawable);
            star2.setColor(Color.YELLOW);
            star2.setOrigin(Align.center);
            star2.setScale(0f);
            star2.addAction(Actions.sequence(Actions.delay(.6f), Actions.scaleTo(1f, 1f, .75f, Interpolation.swingOut)));
            addActor(star2);

        }
        if (stars > 0) {
            star1 = new Image(starDrawable);
            star1.setColor(Color.YELLOW);
            star1.setOrigin(Align.center);
            star1.setScale(0f);
            star1.addAction(Actions.scaleTo(1f, 1f, .75f, Interpolation.swingOut));
            addActor(star1);

        }
    }
}
