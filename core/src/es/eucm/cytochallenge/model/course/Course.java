package es.eucm.cytochallenge.model.course;

import com.badlogic.gdx.utils.Array;
import es.eucm.cytochallenge.model.Challenge;
import es.eucm.cytochallenge.model.Difficulty;
import es.eucm.cytochallenge.view.screens.Challenges;

public class Course {
    private String courseId, name;
    private Array<String> challenges;
    private Difficulty difficulty;
    private int timePerChallenge;

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

    public Difficulty getEstimatedDifficulty(Array<Difficulty> courseDiffs) {
        if(difficulty == null) {
            difficulty = Difficulty.EASY;
        }
        if(courseDiffs != null) {
            for(int i = 0; i < courseDiffs.size; ++i) {
                Difficulty challengeDifficulty = courseDiffs.get(i);
                if(challengeDifficulty.ordinal() > difficulty.ordinal()) {
                    difficulty = challengeDifficulty;
                }
            }
        }



        return difficulty;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getCourseCompletionTime() {
        if(timePerChallenge == 0) {
            return -1;
        }

        return timePerChallenge * challenges.size;
    }

    public void setTimePerChallenge(int timePerChallenge) {
        this.timePerChallenge = timePerChallenge;
    }
}
