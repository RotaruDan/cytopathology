package es.eucm.cytochallenge.view;


import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class TimerWidget extends TextButton {

    private boolean stopped;
    private float time;
    private Runnable onTimeoutListener;

    public TimerWidget(String text, Skin skin) {
        super(text, skin);
        setTouchable(Touchable.disabled);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (time <= 0 || stopped) {
            return;
        }
        time -= delta;
        setText(String.valueOf((int) time));
        if(onTimeoutListener != null && time <= 0) {
            onTimeoutListener.run();
        }
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
        setText(String.valueOf((int) time));
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
