package es.eucm.cytochallenge.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Prefs {

    private Preferences prefs;

    public Prefs() {
        prefs = Gdx.app.getPreferences("cytopathologychallenge");

    }

    public void saveChallengeScore(String challengeId, float score) {
        prefs.putFloat("challenge_" + challengeId, score);
    }

    public float getChallengeScore(String challengeId) {
        return prefs.getFloat("challenge_" + challengeId, 0);
    }

    public void saveCourseScore(String courseId, String score) {
        prefs.putString("course_" + courseId, score);
    }

    public String getCourseScore(String courseId) {
        return prefs.getString("course_" + courseId, "");
    }
}
