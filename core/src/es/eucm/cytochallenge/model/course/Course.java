package es.eucm.cytochallenge.model.course;

import com.badlogic.gdx.utils.Array;
import es.eucm.cytochallenge.model.Difficulty;

public class Course {
    private Difficulty difficulty = Difficulty.EASY;
    private String courseId, name;
    private Array<String> challenges;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Array<String> getChallenges() {
        return challenges;
    }

    public void setChallenges(Array<String> challenges) {
        this.challenges = challenges;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
}
