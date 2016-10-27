package es.eucm.cytochallenge.model.control;

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
