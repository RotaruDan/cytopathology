package es.eucm.cytochallenge.model.control.filltheblank;

public class FillTheBlankStatement {

    private String text;
    private String[][] options;
    private int[] correctAnswers;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[][] getOptions() {
        return options;
    }

    public void setOptions(String[][] options) {
        this.options = options;
    }

    public int[] getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int[] correctAnswers) {
        this.correctAnswers = correctAnswers;
    }
}
