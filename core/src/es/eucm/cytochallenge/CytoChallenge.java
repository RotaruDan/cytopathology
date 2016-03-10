package es.eucm.cytochallenge;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import es.eucm.cytochallenge.view.screens.BaseScreen;
import es.eucm.cytochallenge.view.screens.LoadManager;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.transitions.TransitionManager;

public class CytoChallenge extends ApplicationAdapter {

    public BaseScreen showingScreen;
    private LoadManager loadManager;
    private TransitionManager transitionManager;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        this.transitionManager = new TransitionManager();
        this.loadManager = new LoadManager(this, this.transitionManager);
        this.showingScreen = this.loadManager;
        this.showingScreen.create();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(SkinConstants.COLOR_BACKGROUND.r, SkinConstants.COLOR_BACKGROUND.g,
                SkinConstants.COLOR_BACKGROUND.b, SkinConstants.COLOR_BACKGROUND.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.showingScreen.update();
        this.showingScreen.draw();
    }

    @Override
    public void resize(int width, int height) {
        this.showingScreen.resize(width, height);
    }

    @Override
    public void pause() {
        this.showingScreen.pause();
    }

    @Override
    public void resume() {
        this.showingScreen.resume();
    }

    /**
     * Changes the Screen without any animation.
     *
     * @param next
     */
    public void changeScreen(BaseScreen next) {
        changeScreen(next, null);
    }

    /**
     * Changes the Screen with an animation.
     *
     * @param next
     */
    public void changeScreen(BaseScreen next, TransitionManager.Transition screenTransition) {
        Gdx.app.log(this.showingScreen.getClassTag(), "Changing screen to " + next.getClass().getSimpleName() +
                " with transition: " + screenTransition);
        this.showingScreen = this.transitionManager.prepateTransition(next, null);
    }
}
