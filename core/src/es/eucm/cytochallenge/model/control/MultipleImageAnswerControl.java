package es.eucm.cytochallenge.model.control;

/**
 * Created by dan on 23/02/2016.
 */
public class MultipleImageAnswerControl extends TextControl {

    private String[] answers;
    private int[] correctAnswers;

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public int[] getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int[] correctAnswers) {
        this.correctAnswers = correctAnswers;
    }
}
