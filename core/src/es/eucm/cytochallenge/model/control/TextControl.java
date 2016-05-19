package es.eucm.cytochallenge.model.control;

/**
 * Created by dan on 23/02/2016.
 */
public class TextControl {

    private String text;
    private float canvasWidth, canvasHeight;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getCanvasWidth() {
        return canvasWidth;
    }

    public void setCanvasWidth(float canvasWidth) {
        this.canvasWidth = canvasWidth;
    }

    public float getCanvasHeight() {
        return canvasHeight;
    }

    public void setCanvasHeight(float canvasHeight) {
        this.canvasHeight = canvasHeight;
    }
}
