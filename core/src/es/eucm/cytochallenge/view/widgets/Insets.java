package es.eucm.cytochallenge.view.widgets;

public class Insets {

    public float left;

    public float right;

    public float top;

    public float bottom;

    public void set(float value) {
        left = right = top = bottom = value;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public void set(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public float getWidth() {
        return left + right;
    }

    public float getHeight() {
        return top + bottom;
    }

    public void set(Insets insets) {
        this.left = insets.left;
        this.right = insets.right;
        this.top = insets.top;
        this.bottom = insets.bottom;
    }
}
