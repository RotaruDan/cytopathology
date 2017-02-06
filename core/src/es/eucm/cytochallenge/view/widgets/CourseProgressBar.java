package es.eucm.cytochallenge.view.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import es.eucm.cytochallenge.view.SkinConstants;

public class CourseProgressBar extends IconButton {

    private Table progressBarPanel;
    private Cell<ProgressBar> progressBarCell;
    private Label progressBarLabel, remainingTime;
    private float time = -1;
    private boolean stopped;
    private Runnable onTimeoutListener;

    public CourseProgressBar(String icon, Skin skin) {
        this(icon, skin, "default");
    }

    public CourseProgressBar(String icon, Skin skin, String styleName) {
        super(icon, skin, styleName);

        CourseProgressBarStyle style = skin.get(styleName, CourseProgressBarStyle.class);

        float space = WidgetBuilder.dp16ToPixels();

        progressBarPanel = new Table();
        progressBarPanel.setBackground(style.panelBackground);

        IconButton closeProgressBar = es.eucm.cytochallenge.view.widgets.WidgetBuilder.toolbarIcon(SkinConstants.IC_CLOSE);
        closeProgressBar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hideProgress();
            }
        });
        closeProgressBar.getIcon().setColor(Color.BLACK);

        progressBarPanel.defaults().space(space);
        progressBarPanel.pad(space);
        progressBarPanel.add(closeProgressBar);
        progressBarCell = progressBarPanel.add().expand().fill();
        progressBarLabel = new Label("", skin);
        progressBarPanel.add(progressBarLabel);
        remainingTime = new Label("", skin);
        progressBarPanel.add(remainingTime);

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showProgress();
            }
        });
    }

    private void updateProgressPercentage() {
        ProgressBar progressBar = progressBarCell.getActor();
        String percentage = MathUtils.round((progressBar.getValue() / (progressBar.getMaxValue() - progressBar.getMinValue())) * 100) + "%";
        progressBarLabel.setText(percentage);
    }

    public void showProgress() {
        Stage stage = getStage();
        if (stage != null) {
            updateProgressPercentage();
            progressBarPanel.pack();
            stage.addActor(progressBarPanel);
            progressBarPanel.setPosition(0, -progressBarPanel.getHeight());
            progressBarPanel.clearActions();
            progressBarPanel.addAction(Actions.moveTo(0, 0, .35f, Interpolation.circleOut));
        }
    }

    public void hideProgress() {
        progressBarPanel.clearActions();
        progressBarPanel.addAction(Actions.sequence(Actions.moveTo(0, -progressBarPanel.getHeight(), .35f, Interpolation.circleIn), Actions.removeActor()));
    }

    public void setProgressBar(ProgressBar progressBar) {
        progressBarCell.setActor(progressBar);
    }


    static public class CourseProgressBarStyle extends IconButtonStyle {
        public Drawable panelBackground;

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (time <= 0 || stopped) {
            return;
        }
        time -= delta;
        remainingTime.setText(String.valueOf((int) time));
        if(onTimeoutListener != null && time <= 0) {
            onTimeoutListener.run();
        }
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
        if(time > 0) {
            remainingTime.setVisible(true);
            remainingTime.setText(String.valueOf((int) time));
        } else {
            remainingTime.setVisible(false);
        }
    }

    public void stop() {
        stopped = true;
    }

    public void start() {
        stopped = false;
    }

    public void setOnTimeoutListener(Runnable onTimeoutListener) {
        this.onTimeoutListener = onTimeoutListener;
    }
}
