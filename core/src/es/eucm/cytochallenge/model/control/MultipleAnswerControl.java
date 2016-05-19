package es.eucm.cytochallenge.model.control;

/**
 * Created by dan on 23/02/2016.
 */
public class MultipleAnswerControl extends TextControl {

    private String[] answers;
    private int correctAnswer;

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String... answers) {
        this.answers = answers;
    }
}
