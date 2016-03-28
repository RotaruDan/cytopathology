package es.eucm.cytochallenge.view.widgets.challenge.result;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;
import es.eucm.cytochallenge.view.screens.BaseScreen;

public class PolygonActor extends Actor {

    private float[] vertices;

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();

        BaseScreen.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        BaseScreen.shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        BaseScreen.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        BaseScreen.shapeRenderer.setColor(getColor());
        BaseScreen.shapeRenderer.polygon(vertices);
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
