package es.eucm.cytochallenge.model.course;

import com.badlogic.gdx.utils.Array;

/**
 * Created by dan on 04/08/2016.
 */
public class Course {
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
}
