package es.eucm.cytochallenge.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;
import es.eucm.cytochallenge.CytoChallenge;
import es.eucm.cytochallenge.utils.Prefs;
import es.eucm.cytochallenge.utils.Resolver;
import es.eucm.cytochallenge.view.transitions.Fade;

public class BaseScreen {

    protected static String TAG = "BaseScreen";
    public static Skin skin;
    protected static Stage stage;
    protected static CytoChallenge game;
    protected static AssetManager am;
    protected static I18NBundle i18n;
    public static ShapeRenderer shapeRenderer;
    public static Prefs prefs;
    protected static Resolver resolver;
    
    protected static Menu menu;
    protected static Lab lab;
    protected static ChallengeList challengeList;
    protected static CourseList courseList;
    protected static Challenges challenges;
    protected BaseScreen previousScreen;
    protected Table root;

    protected void setUpRoot() {
        Gdx.app.log(getClassTag(), "setUpRoot");
        root = new Table(skin);
        root.setFillParent(true);
    }

    public void update() {
        // Gdx.app.log(getClassTag(), "update");
        stage.act();
    }

    public void draw() {
        // Gdx.app.log(getClassTag(), "draw");
        stage.draw();
    }

    public void create() {
        Gdx.app.log(getClassTag(), "create");
        setUpRoot();
    }

    public void show() {
        Gdx.app.log(getClassTag(), "show");
        stage.addActor(root);
    }

    public void hide() {
        Gdx.app.log(getClassTag(), "hide");
        stage.getRoot().removeActor(root);
    }

    public InputProcessor getInputProcessor() {
        return stage;
    }

    public void pause() {
        Gdx.app.log(getClassTag(), "pause");
    }

    public void resume() {
        Gdx.app.log(getClassTag(), "resume");
    }

    public void resize(int width, int height) {
        Gdx.app.log(getClassTag(), "resize: " + width + ", " + height);
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        Gdx.app.log(getClassTag(), "dispose");
    }

    public void onBackPressed() {
        Gdx.app.log(getClassTag(), "onBackPressed");
        game.changeScreen(previousScreen, Fade.init(1f));
    }

    public static Stage getStage() {
        return stage;
    }

    public String getClassTag() {
        return TAG + " " + getClass().getSimpleName();
    }
}
