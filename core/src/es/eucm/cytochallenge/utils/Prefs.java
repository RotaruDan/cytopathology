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

    public void saveCourseChallengeScore(String challengeId, float score) {
        saveChallengeScore("course_" + challengeId, score);
    }

    public float getCourseChallengeScore(String challengeId) {
        return getChallengeScore("course_" + challengeId);
    }

    public void saveCourseScore(String courseId, float score) {
        if (courseId != null) {
            String key = "course_" + courseId;
            float currentScore = prefs.getFloat(key, 0);
            if (currentScore < score) {
                prefs.putFloat(key, score);
                prefs.flush();
            }
        }
    }

    public float getCourseScore(String courseId) {
        return prefs.getFloat("course_" + courseId, 0f);
    }
}
