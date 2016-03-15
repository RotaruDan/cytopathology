package es.eucm.cytochallenge.model.control;


public class InteractiveZoneControl extends TextControl {

    private float[][] answers;
    private int[] correctAnswers;

    public int[] getCorrectAnswer() {
        return correctAnswers;
    }

    public void setCorrectAnswer(int[] correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public float[][] getAnswers() {
        return answers;
    }

    public void setAnswers(float[][] answers) {
        this.answers = answers;
    }
}
