package es.eucm.cytochallenge.view.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.Scaling;
import es.eucm.cytochallenge.model.hint.Hint;
import es.eucm.cytochallenge.model.hint.ImageInfo;
import es.eucm.cytochallenge.model.hint.Info;
import es.eucm.cytochallenge.model.hint.TextInfo;
import es.eucm.cytochallenge.utils.ChallengeResourceProvider;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.widgets.challenge.TextChallengeWidget;

public class ResultDialog extends Container<Table> {

    private Image star1, star2, star3;
    private float score;

    public ResultDialog(boolean nextCourse, final Hint hint, int score, final ChallengeResourceProvider resourceProvider, final Skin skin, final I18NBundle i18n) {
        super(new Table());
        ResultDialogStyle style = skin.get(ResultDialogStyle.class);
        background(style.background);
        this.score = score;
        center();
        Table root = getActor();
        root.setBackground(style.panelBackground);

        float pad24dp = WidgetBuilder.dpToPixels(24);
        float pad16dp = WidgetBuilder.dpToPixels(16);
        float pad48dp = WidgetBuilder.dpToPixels(48);
        root.pad(pad48dp, pad48dp, pad48dp, pad48dp);

        Table container = new Table();
        container.defaults().space(pad24dp);
        container.pad(pad16dp);
        TextButton explanation = null;
        if (hint != null) {
            explanation = WidgetBuilder.dialogButton(i18n.get("explanation"),
                    style.buttonStyle);
            explanation.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    HintDialog dialog = new HintDialog(skin, hint, i18n, resourceProvider);
                    getStage().addActor(dialog);
                    dialog.show();
                }
            });
        }
        TextButton moreDetails = WidgetBuilder.dialogButton(i18n.get("moreDetails"),
                style.buttonStyle);
        moreDetails.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ResultEvent groupEvent = Pools.obtain(ResultEvent.class);
                groupEvent.setType(ResultEvent.Type.moreInfo);
                fire(groupEvent);
                Pools.free(groupEvent);
                hide();
            }
        });

        Label scoreLabel = new Label(score + "", style.textStyle);
        container.add(scoreLabel);
        container.row();
        container.add(explanation).expandX().right();
        container.row();

        container.add(moreDetails);

        TextButton exit = WidgetBuilder.dialogButton(i18n.get("exit").toUpperCase(),
                style.buttonStyle);
        exit.getLabel().setColor(Color.LIGHT_GRAY);
        TextButton next = null;
        if(nextCourse) {
            next = WidgetBuilder.dialogButton(i18n.get("next").toUpperCase(),
                    style.buttonStyle);

            next.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    ResultEvent groupEvent = Pools.obtain(ResultEvent.class);
                    groupEvent.setType(ResultEvent.Type.next);
                    fire(groupEvent);
                    Pools.free(groupEvent);
                    hide();
                }
            });
        }
        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ResultEvent groupEvent = Pools.obtain(ResultEvent.class);
                groupEvent.setType(ResultEvent.Type.back);
                fire(groupEvent);
                Pools.free(groupEvent);
                hide();
            }
        });

        Drawable starDrawable = skin.getDrawable(SkinConstants.DRAWABLE_STAR);
        star1 = new Image(starDrawable);
        star1.setColor(Color.YELLOW);
        root.add(container).padTop(star1.getHeight() + pad16dp).expand().fill().colspan(2);
        root.row();
        root.add(exit).expandX().right().padTop(pad16dp);
        root.add(next).right().padTop(pad16dp);

        star2 = new Image(starDrawable);
        star2.setColor(Color.YELLOW);
        star3 = new Image(starDrawable);
        star3.setColor(Color.YELLOW);
        star1.setOrigin(Align.center);
        star2.setOrigin(Align.center);
        star3.setOrigin(Align.center);
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
        star1.setScale(0f);
        star2.setScale(0f);
        star3.setScale(0f);

        pack();

        final Table root = getActor();

        clearActions();
        getColor().a = 0f;
        addAction(Actions.alpha(1f, .33f));

        float y = root.getY();
        root.setY(Gdx.graphics.getHeight());
        root.addAction(Actions.sequence(Actions.moveTo(root.getX(), y, 0.33f, Interpolation.exp5Out), Actions.run(new Runnable() {
            @Override
            public void run() {
                if (score > 40) {
                    star1.addAction(Actions.parallel(Actions.scaleTo(1f, 1f, .5f, Interpolation.bounceOut), Actions.alpha(1f, .4f)));
                }
                if (score > 65) {
                    star2.addAction(Actions.delay(.6f, Actions.parallel(Actions.scaleTo(1f, 1f, .5f, Interpolation.bounceOut), Actions.alpha(1f, .4f))));
                }
                if (score > 90) {
                    star3.addAction(Actions.delay(1.2f, Actions.parallel(Actions.scaleTo(1f, 1f, .5f, Interpolation.bounceOut), Actions.alpha(1f, .4f))));
                }
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

    public static class ResultListener implements EventListener {

        @Override
        public boolean handle(Event event) {
            if (event instanceof ResultEvent) {
                ResultEvent groupEvent = (ResultEvent) event;
                switch (groupEvent.getType()) {
                    case next:
                        nextChallenge();
                        break;
                    case back:
                        backPressed();
                        break;
                    case moreInfo:
                        moreInfo();
                        break;
                }
                return true;
            }
            return false;
        }

        public void nextChallenge() {

        }

        public void backPressed() {

        }

        public void moreInfo() {

        }
    }

    public static class ResultEvent extends Event {

        private Type type;

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public enum Type {
            next, back, moreInfo
        }
    }

}