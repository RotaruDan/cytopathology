package es.eucm.cytochallenge.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import es.eucm.cytochallenge.model.Challenge;

public class InternalFilesChallengeResourceProvider implements ChallengeResourceProvider {

    private String currentChallengeId;
    private String resourcePath;
    private Json json;

    public InternalFilesChallengeResourceProvider() {
        this.json = new Json();
    }

    @Override
    public void getTexture(String imagePath, ResourceProvidedCallback<Texture> callback) {
        if(imagePath == null || imagePath.isEmpty()) {
            callback.failed();
            return;
        }
        callback.loaded(new Texture(Gdx.files.internal(resourcePath + imagePath)));
    }

    @Override
    public void getChallenge(String jsonPath, ResourceProvidedCallback<Challenge> callback) {
        try {
            callback.loaded(json.fromJson(Challenge.class, Gdx.files.internal(resourcePath + jsonPath)));
        } catch (Exception exception) {
            exception.printStackTrace();
            callback.failed();
        }
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getCurrentChallengeId() {
        return currentChallengeId;
    }

    public void setCurrentChallengeId(String currentChallengeId) {
        this.currentChallengeId = currentChallengeId;
    }
}
