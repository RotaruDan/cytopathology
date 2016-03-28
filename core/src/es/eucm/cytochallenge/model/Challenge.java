package es.eucm.cytochallenge.model;

import es.eucm.cytochallenge.model.hint.Hint;

/**
 * Created by dan on 23/02/2016.
 */
public class Challenge {

    private String imagePath;

    private Hint hint;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Hint getHint() {
        return hint;
    }

    public void setHint(Hint hint) {
        this.hint = hint;
    }
}
