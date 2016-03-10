package es.eucm.cytochallenge.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Scaling;
import es.eucm.cytochallenge.model.Challenge;
import es.eucm.cytochallenge.model.TextChallenge;
import es.eucm.cytochallenge.model.control.MultipleAnswerControl;
import es.eucm.cytochallenge.model.control.MultipleImageAnswerControl;
import es.eucm.cytochallenge.model.control.TextControl;
import es.eucm.cytochallenge.model.control.draganddrop.DragAndDropControl;
import es.eucm.cytochallenge.model.control.filltheblank.FillTheBlankControl;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.transitions.Fade;
import es.eucm.cytochallenge.view.widgets.ChallengeButton;
import es.eucm.cytochallenge.view.widgets.WidgetBuilder;

public class ChallengeList extends BaseScreen {

    @Override
    public void create() {
        super.create();

        Image logo = new Image(skin, SkinConstants.DRAWABLE_LOGO);
        logo.setScaling(Scaling.fit);

        final Button back;
        back = WidgetBuilder.toolbarIcon(SkinConstants.IC_UNDO);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onBackPressed();
            }
        });

        Container<Button> backContainer = new Container<Button>();
        backContainer.setFillParent(true);
        backContainer.setActor(back);
        backContainer.top().left();


        root.defaults().expand().fill();

        Table layout = new Table();

        loadChallenges(layout);

        ScrollPane scroll = new ScrollPane(layout);
        scroll.setScrollingDisabled(true, false);
        root.add(layout).expand().fill().top();

        root.addActor(backContainer);
    }

    private void loadChallenges(Table layout) {

        layout.top();
        float pad = WidgetBuilder.dpToPixels(54f);
        layout.pad(pad);
        float maxWidth = Gdx.graphics.getWidth() - pad;
        String challengesPath = "challenges/";
        String challengeJson = "challenge.json";
        FileHandle internal = Gdx.files.internal(challengesPath);

        FileHandle[] list = internal.list();

        Json json = new Json();

        for (int i = 0; i < list.length; i++) {
            final FileHandle fileHandle = list[i];

            FileHandle child = fileHandle.child(challengeJson);

            Challenge challenge = json.fromJson(Challenge.class, child);

            if (challenge instanceof TextChallenge) {
                TextChallenge textChallenge = (TextChallenge) challenge;

                Button button = new ChallengeButton(textChallenge.getTextControl(),
                        skin);

                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        challenges.setChallengePath(fileHandle.path() + "/");
                        game.changeScreen(challenges);
                    }
                });

                layout.add(button).left().fillX().width(maxWidth);
                layout.row();
            }
        }
    }

    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public void onBackPressed() {
        game.changeScreen(menu, Fade.init(1f, true));
    }

}