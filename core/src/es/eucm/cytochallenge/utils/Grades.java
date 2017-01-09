package es.eucm.cytochallenge.utils;

public class Grades {

    public static String getGrade(float percentage) {
        String grade;
        if (percentage < 60) {
            grade = "F";
        } else if (percentage < 63) {
            grade = "D-";
        } else if (percentage < 67) {
            grade = "D";
        } else if (percentage < 70) {
            grade = "D+";
        } else if (percentage < 73) {
            grade = "C-";
        } else if (percentage < 77) {
            grade = "C";
        } else if (percentage < 80) {
            grade = "C+";
        } else if (percentage < 83) {
            grade = "B-";
        } else if (percentage < 87) {
            grade = "B";
        } else if (percentage < 90) {
            grade = "B+";
        } else if (percentage < 93) {
            grade = "A-";
        } else if (percentage < 95) {
            grade = "A";
        } else {
            grade = "A+";
        }

        return grade;
    }

    public static int getStars(float score) {
        int stars = 0;
        if (score > 90) {
            stars = 3;
        } else if (score > 60) {
            stars = 2;
        } else if (score > 20) {
            stars = 1;
        }
        return stars;
    }
}
