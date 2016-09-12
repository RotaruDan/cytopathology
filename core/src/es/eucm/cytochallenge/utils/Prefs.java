package es.eucm.cytochallenge.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Prefs {

    private Preferences prefs;

    public Prefs() {
        prefs = Gdx.app.getPreferences("cytopathologychallenge");

    }

    public void saveChallengeScore(String challengeId, float score) {
        if (challengeId != null) {
            String key = "challenge_" + challengeId;
            float currentScore = prefs.getFloat(key, 0);
            if (currentScore < score) {
                prefs.putFloat(key, score);
                prefs.flush();
            }
        }
    }

    public float getChallengeScore(String challengeId) {
        return prefs.getFloat("challenge_" + challengeId, 0f);
    }
}
