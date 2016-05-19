package es.eucm.cytochallenge.model.control.draganddrop;

import es.eucm.cytochallenge.model.control.TextControl;


public class DragAndDropControl extends TextControl {

    private DragAndDropAnswer[] answers;

    public DragAndDropAnswer[] getAnswers() {
        return answers;
    }

    public void setAnswers(DragAndDropAnswer[] answers) {
        this.answers = answers;
    }
}
