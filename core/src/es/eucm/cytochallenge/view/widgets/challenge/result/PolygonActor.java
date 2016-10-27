package es.eucm.cytochallenge.view.widgets.challenge.result;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;
import es.eucm.cytochallenge.view.screens.BaseScreen;

public class PolygonActor extends Actor {

    private float[] vertices;
    private float width = 8f;

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();

        BaseScreen.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        BaseScreen.shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        BaseScreen.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        BaseScreen.shapeRenderer.setColor(getColor());
        int count = vertices.length;
        for (int i = 0, n = count - 2; i < n; i += 2) {
            float x1 = vertices[i];
            float y1 = vertices[i + 1];

            float x2;
            float y2;

            x2 = vertices[i + 2];
            y2 = vertices[i + 3];

            BaseScreen.shapeRenderer.rectLine(x1, y1, x2, y2, width);
        }
        if (count > 5) {
            BaseScreen.shapeRenderer.rectLine(vertices[0], vertices[1], vertices[count - 2], vertices[count - 1], width);
        }
        BaseScreen.shapeRenderer.end();

        batch.begin();
    }

    public float[] getPolygon() {
        return vertices;
    }

    public void setPolygon(float[] vertices) {
        this.vertices = vertices;
    }
}
