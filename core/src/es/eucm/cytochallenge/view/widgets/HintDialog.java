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
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Scaling;
import es.eucm.cytochallenge.model.hint.Hint;
import es.eucm.cytochallenge.model.hint.ImageInfo;
import es.eucm.cytochallenge.model.hint.Info;
import es.eucm.cytochallenge.model.hint.TextInfo;
import es.eucm.cytochallenge.utils.Actions2;
import es.eucm.cytochallenge.utils.ChallengeResourceProvider;

public class HintDialog extends Table {

    public HintDialog(Skin skin, Hint hint, I18NBundle i18n, ChallengeResourceProvider challengeResourceProvider) {
        HintDialogStyle style = skin.get(HintDialogStyle.class);
        background(style.background);
        float pad24dp = WidgetBuilder.dpToPixels(24);
        float pad16dp = WidgetBuilder.dpToPixels(16);
        pad(pad24dp, pad24dp, pad16dp, pad24dp);

        Table container = new Table();
        container.defaults().space(pad24dp);
        container.pad(pad16dp);
        ScrollPane scroll = new ScrollPane(container);
        scroll.setScrollingDisabled(true, false);

        Info[] infos = hint.getInfos();
        for (int i = 0; i < infos.length; i++) {
            Info info = infos[i];
            if (info instanceof TextInfo) {
                TextInfo textInfo = (TextInfo) info;

                Label textLabel = new Label(textInfo.getText(), style.textStyle);
                textLabel.setWrap(true);
                container.add(textLabel).expandX().fillX();
                container.row();

            } else if (info instanceof ImageInfo) {
                ImageInfo imageInfo = (ImageInfo) info;
                final Image image = new Image();
                challengeResourceProvider.getTexture(imageInfo.getImagePath(), new ChallengeResourceProvider.ResourceProvidedCallback<Texture>() {
                    @Override
                    public void loaded(Texture resource) {
                        resource.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                        image.setDrawable(new TextureRegionDrawable(new TextureRegion(resource)));
                    }

                    @Override
                    public void failed() {

                    }
                });
                image.setScaling(Scaling.fit);
                container.add(image).expand().fill();
                container.row();
            }
        }

        TextButton ok = WidgetBuilder.dialogButton(i18n.get("ok"),
                style.buttonStyle);
        ok.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });
        add(scroll).expand().fill();
        row();
        add(ok).expandX().right().padTop(pad16dp);
        setFillParent(true);
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
        float y = getY();
        setY(Gdx.graphics.getHeight());
        clearActions();
        addAction(Actions.moveTo(0, y, 0.33f, Interpolation.exp5Out));
    }

    public void hide() {
        addAction(Actions.sequence(Actions.moveTo(0,Gdx.graphics.getHeight(),
                0.33f, Interpolation.exp5Out), Actions.run(new Runnable() {
            @Override
            public void run() {
                remove();
            }
        })));
    }


    public static class HintDialogStyle {

        public Drawable background;

        public Label.LabelStyle textStyle;

        public TextButton.TextButtonStyle buttonStyle;

    }
}