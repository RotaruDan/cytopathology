package es.eucm.cytochallenge.view.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Scaling;
import es.eucm.cytochallenge.model.hint.Hint;
import es.eucm.cytochallenge.model.hint.ImageInfo;
import es.eucm.cytochallenge.model.hint.Info;
import es.eucm.cytochallenge.model.hint.TextInfo;
import es.eucm.cytochallenge.utils.ChallengeResourceProvider;
import es.eucm.cytochallenge.view.SkinConstants;

public class ResultDialog extends Container<Table> {

    private Image star1, star2, star3;

    public ResultDialog(Hint hint, int score, ChallengeResourceProvider resourceProvider, Skin skin, I18NBundle i18n) {
        super(new Table());
        ResultDialogStyle style = skin.get(ResultDialogStyle.class);
        background(style.background);

        center();
        Table root = getActor();
        root.setBackground(style.panelBackground);

        float pad24dp = WidgetBuilder.dpToPixels(24);
        float pad16dp = WidgetBuilder.dpToPixels(16);
        pad(pad24dp, pad24dp, pad16dp, pad24dp);

        Table container = new Table();
        container.defaults().space(pad24dp);
        container.pad(pad16dp);

        TextButton explanation = WidgetBuilder.dialogButton(i18n.get("explanation"),
                style.buttonStyle);
        TextButton moreDetails = WidgetBuilder.dialogButton(i18n.get("moreDetails"),
                style.buttonStyle);

        Label scoreLabel = new Label(score + "", style.textStyle);
        container.add(scoreLabel).colspan(2);
        container.row();
        container.add(explanation);
        container.add(moreDetails);

        TextButton next = WidgetBuilder.dialogButton(i18n.get("next").toUpperCase(),
                style.buttonStyle);
        TextButton exit = WidgetBuilder.dialogButton(i18n.get("exit").toUpperCase(),
                style.buttonStyle);
        exit.getLabel().setColor(Color.LIGHT_GRAY);

        next.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO GO Next Challenge or Challenges
                hide();
            }
        });
        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO go challenges
                hide();
            }
        });

        Drawable starDrawable = skin.getDrawable(SkinConstants.DRAWABLE_STAR);
        star1 = new Image(starDrawable);
        root.add(container).padTop(star1.getHeight() + pad16dp).expand().fill();
        root.row();
        root.add(exit).expandX().right().padTop(pad16dp);
        root.add(next).right().padTop(pad16dp);

        star2 = new Image(starDrawable);
        star3 = new Image(starDrawable);
        root.addActor(star1);
        root.addActor(star2);
        root.addActor(star3);

        setFillParent(true);
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        Actor hit = super.hit(x, y, touchable);
        return hit == null ? this : hit;
    }

    public void show() {
        star1.getColor().a = 0f;
        star2.getColor().a = 0f;
        star3.getColor().a = 0f;

        pack();

        final Table root = getActor();

        clearActions();
        getColor().a = 0f;
        addAction(Actions.alpha(1f, .5f));

        float y = root.getY();
        root.setY(Gdx.graphics.getHeight());
        root.addAction(Actions.sequence(Actions.moveTo(root.getX(), y, 0.33f, Interpolation.exp5Out), Actions.run(new Runnable() {
            @Override
            public void run() {
                star1.addAction(Actions.alpha(1f, 1f));
                star2.addAction(Actions.alpha(1f, 2f));
                star3.addAction(Actions.alpha(1f, 3f));
                star1.setPosition(MathUtils.round(-star1.getWidth() * .5f), MathUtils.round(root.getHeight() - star1.getWidth() * .75f));
                star2.setPosition(MathUtils.round((root.getWidth() - star2.getWidth()) * .5f), MathUtils.round(root.getHeight() - star2.getWidth() * .5f));
                star3.setPosition(MathUtils.round(root.getWidth() - star1.getWidth() * .5f), MathUtils.round(root.getHeight() - star3.getWidth() * .75f));
            }
        })));
    }

    public void hide() {
        addAction(Actions.sequence(Actions.alpha(0, .33f, Interpolation.exp5Out), Actions.run(new Runnable() {
            @Override
            public void run() {
                remove();
            }
        })));
    }


    public static class ResultDialogStyle {

        public Drawable background, panelBackground;

        public Label.LabelStyle textStyle;

        public TextButton.TextButtonStyle buttonStyle;

    }
}