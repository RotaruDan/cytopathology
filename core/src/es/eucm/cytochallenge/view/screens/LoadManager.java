package es.eucm.cytochallenge.view.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.*;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import es.eucm.cytochallenge.CytoChallenge;
import es.eucm.cytochallenge.lib.skim.Skim;
import es.eucm.cytochallenge.lib.skim.SkimLoader;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.transitions.TransitionManager;

public class LoadManager extends BaseScreen {
    public static final String[] SCALES = new String[]{"1.0", "2.0"};
    private String SKIN_JSON_SRC = "skin/scale1.0/skin.json";
    private String I18N_SRC = "i18n/app";

    private CytoChallenge myGame;
    private TransitionManager transitionManager;

    public LoadManager(CytoChallenge myGame, TransitionManager transitionManager) {
        this.myGame = myGame;
        this.transitionManager = transitionManager;
    }

    @Override
    public void create() {

        // 48px are 0.76cm in scale 1.0
        float scale = (Gdx.graphics.getPpcX() * 0.76f) / SkinConstants.UNIT_SIZE;
        String scaleString;
        if (scale < Float.parseFloat(SCALES[0])) {
            scaleString = SCALES[0];
        } else {
            scaleString = SCALES[SCALES.length - 1];
            for (int i = 0; i < SCALES.length - 1; i++) {
                float lowerScale = Float.parseFloat(SCALES[i]);
                float greaterScale = Float.parseFloat(SCALES[i + 1]);
                if (scale >= lowerScale && scale <= greaterScale) {
                    scaleString = scale - lowerScale < greaterScale - scale ? SCALES[i]
                            : SCALES[i + 1];
                    break;
                }
            }
        }

        SKIN_JSON_SRC = "skin/scale" + scaleString + "/skin.json";
        Gdx.app.log(getClassTag(), "Skin path: " + SKIN_JSON_SRC + ", scale: " + scale);

        stage = new Stage(new ScreenViewport());
        if (Gdx.app.getLogLevel() != Application.LOG_NONE) {
            //stage.setDebugUnderMouse(true);
            stage.setDebugAll(true);
        }

        InternalFileHandleResolver resolver = new InternalFileHandleResolver();
        am = new AssetManager(resolver, false);
        am.setLoader(BitmapFont.class, new BitmapFontLoader(resolver));
        am.setLoader(Music.class, new MusicLoader(resolver));
        am.setLoader(Sound.class, new SoundLoader(resolver));
        am.setLoader(TextureAtlas.class, new TextureAtlasLoader(resolver));
        am.setLoader(Texture.class, new TextureLoader(resolver));
        am.setLoader(Skim.class, new SkimLoader(resolver));
        am.setLoader(I18NBundle.class, new I18NBundleLoader(resolver));

        /*-QUEUE here loading assets-*/
        am.load(SKIN_JSON_SRC, Skim.class);

        am.load(I18N_SRC, I18NBundle.class);
        am.setErrorListener(new AssetErrorListener() {
            @Override
            public void error(AssetDescriptor asset, Throwable throwable) {
                Gdx.app.log("LoadManager", "LoadManager.error: " + asset, throwable);
            }
        });
    }

    public void update() {
        if (am.update()) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    game = myGame;

                    // This allows us to catch events related with
                    // the back key in Android.
                    Gdx.input.setCatchBackKey(true);
                    stage.getRoot().addCaptureListener(new InputListener() {
                        @Override
                        public boolean keyUp(InputEvent event, int keycode) {
                            if (keycode == Input.Keys.BACK
                                    || (Gdx.app.getType() == Application.ApplicationType.Desktop &&
                                    keycode == Input.Keys.ESCAPE)) {
                                game.showingScreen.onBackPressed();
                                return true;
                            } else if (keycode == Input.Keys.ENTER
                                    && !(event.getTarget() instanceof TextArea)) {
                                Gdx.input.setOnscreenKeyboardVisible(false);
                                stage.setKeyboardFocus(null);
                                stage.unfocusAll();
                                return true;
                            } else if (Gdx.app.getType() == Application.ApplicationType.Desktop &&
                                    keycode == Input.Keys.NUM_2) {
                                stage.touchUp(
                                        Gdx.graphics.getWidth() / 4,
                                        Gdx.graphics.getHeight() / 2, 1, Input.Buttons.LEFT);
                            }
                            return false;
                        }

                        @Override
                        public boolean keyDown(InputEvent event, int keycode) {
                            if (Gdx.app.getType() == Application.ApplicationType.Desktop &&
                                    keycode == Input.Keys.NUM_2) {
                                stage.touchDown(
                                        Gdx.graphics.getWidth() / 4,
                                        Gdx.graphics.getHeight() / 2, 1, Input.Buttons.LEFT);
                            }
                            return super.keyDown(event, keycode);
                        }
                    });

                    Gdx.input.setInputProcessor(stage);

                    skin = am.get(SKIN_JSON_SRC, Skim.class);

                    i18n = am.get(I18N_SRC, I18NBundle.class);

                    menu = new Menu();
                    menu.create();

                    lab = new Lab();
                    lab.create();

                    challengeList = new ChallengeList();
                    challengeList.create();

                    challenges = new Challenges();
                    challenges.create();

                    transitionManager.initialize();

                    game.changeScreen(menu, null);
                }
            });
        }
    }

    public void draw() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        menu.dispose();
        am.dispose();
        am = null;
        stage = null;
        game = null;
    }
}
