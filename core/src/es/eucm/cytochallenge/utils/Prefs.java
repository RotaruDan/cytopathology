package es.eucm.cytochallenge.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.TimeUtils;

public class Prefs {

    private Preferences prefs;

    public Prefs() {
        prefs = Gdx.app.getPreferences("cytopathologychallenge");

    }

    public void saveChallengeScore(String challengeId, float score, long startTime) {
        if (challengeId != null) {
            String key = "challenge_" + challengeId;
            float currentScore = prefs.getFloat(key, 0);
            long time = getChallengeTime(challengeId);
            if(time == 0l){
                prefs.putLong("challengeTime_" + challengeId, TimeUtils.millis() - startTime);
            }
            if (currentScore < score) {
                prefs.putFloat(key, score);
                prefs.putLong("challengeTime_" + challengeId, TimeUtils.millis() - startTime);
                prefs.flush();
            }
        }
    }

    public float getChallengeScore(String challengeId) {
        return prefs.getFloat("challenge_" + challengeId, 0f);
    }

    public long getChallengeTime(String challengeId) {
        return prefs.getLong("challengeTime_" + challengeId, 0l);
    }
}
