package es.eucm.cytochallenge.utils;

import com.badlogic.gdx.graphics.Texture;
import es.eucm.cytochallenge.model.Challenge;

public interface ChallengeResourceProvider {

    void getTexture(String imagePath, ResourceProvidedCallback<Texture> callback);

    void getChallenge(String jsonPath, ResourceProvidedCallback<Challenge> callback);

    interface ResourceProvidedCallback<T> {

        void loaded(T resource);

        void failed();
    }
}
