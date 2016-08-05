package es.eucm.cytochallenge.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.transitions.Fade;
import es.eucm.cytochallenge.view.widgets.ChatDialog;
import es.eucm.cytochallenge.view.widgets.IconButton;
import es.eucm.cytochallenge.view.widgets.LabBackground;
import es.eucm.cytochallenge.view.widgets.WidgetBuilder;

public class Lab extends BaseScreen {

    private ChatDialog dialog;

    @Override
    public void create() {
        super.create();

        LabBackground labBackground = new LabBackground();

        IconButton tablet = WidgetBuilder.toolbarIcon(SkinConstants.IC_TABLET);
        tablet.addAction(Actions.forever(
                Actions.sequence(
                        Actions.fadeOut(.5f),
                        Actions.fadeIn(.5f))));
        tablet.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.changeScreen(courseList);
            }
        });

        final Button back;
        back = WidgetBuilder.toolbarIcon(SkinConstants.IC_UNDO);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onBackPressed();
            }
        });

        Table buttonsContainer = new Table();
        buttonsContainer.setTouchable(Touchable.childrenOnly);
        buttonsContainer.setFillParent(true);
        buttonsContainer.add(back).expand().top().left();
        buttonsContainer.add(tablet).expand().top().right();

        root.add(labBackground).expand().fill();
        root.addActor(buttonsContainer);


        dialog = new ChatDialog(skin);
        dialog.setText(i18n.get("introDialog"));
        dialog.show();
        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });

        buttonsContainer.row();
        buttonsContainer.add(dialog.bottom()).colspan(2).expandX().fillX().expandY();
        root.addActor(dialog);
    }

    @Override
    public void show() {
        super.show();
        dialog.show();
        dialog.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() * .25f);

    }


    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public void onBackPressed() {
        game.changeScreen(menu);
    }

}