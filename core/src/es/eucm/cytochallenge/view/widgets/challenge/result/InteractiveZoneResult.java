package es.eucm.cytochallenge.view.widgets.challenge.result;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Scaling;
import es.eucm.cytochallenge.model.control.InteractiveZoneControl;
import es.eucm.cytochallenge.model.control.MultipleAnswerControl;
import es.eucm.cytochallenge.utils.Grades;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.widgets.IconButton;
import es.eucm.cytochallenge.view.widgets.TopToolbarLayout;
import es.eucm.cytochallenge.view.widgets.slide.SlideEditor;

public class InteractiveZoneResult extends ResultLayout<InteractiveZoneControl> {


    public InteractiveZoneResult(Skin skin,
                                 InteractiveZoneControl control,
                                 I18NBundle i18n,
                                 Array<Button> markers, Actor right) {
        super(skin, control, i18n, markers, right);
    }

    // InteractiveZone
    private int checkInteractiveZoneResult(InteractiveZoneControl zoneControl,
                                           Array<Button> markers) {
        int correctAnswers = 0;

        float[][] answers = zoneControl.getAnswers();

        for (int i = 0; i < markers.size; i++) {
            Button button = markers.get(i);

            for (int j = 0; j < answers.length; j++) {
                float[] answer = answers[j];

                Polygon polygon = new Polygon(answer);

                if (polygon.contains(button.getX() + button.getWidth() * .5f,
                        button.getY() + button.getHeight() * .5f)) {
                    correctAnswers++;
                    break;
                }
            }
        }


        return correctAnswers;
    }


    @Override
    protected Label buildLabel(InteractiveZoneControl control, Object... args) {
        Array markers = (Array) args[0];
        int results = checkInteractiveZoneResult(control, markers);
        int total = markers.size;
        if(total == 0) {
            total = 1;
        }

        score = results / (float) total * 100;

        Label resultsLabel = new Label(Grades.getGrade(score) +
                "    " + results + "/" + total, getSkin(), SkinConstants.STYLE_TOAST);
        resultsLabel.setAlignment(Align.center);
        return resultsLabel;
    }

    @Override
    protected Actor[] buildTabs(InteractiveZoneControl control, Object... args) {
        Array<Button> markers = (Array) args[0];
        Actor right = (Actor) args[1];

        SlideEditor editor = (SlideEditor) right;
        Group rootRightImage = (Group) editor.getRootActor();

        final Image imageActor = new Image(((Image)rootRightImage.getChildren().first())
                .getDrawable());
        imageActor.setScaling(Scaling.fit);

        final Group imageGroup = new Group();
        imageGroup.setTouchable(Touchable.disabled);
        imageGroup.addActor(imageActor);
        imageGroup.setBounds(0, 0, imageActor.getWidth(), imageActor.getHeight());

        SlideEditor slideEditor = new SlideEditor(getSkin());
        slideEditor.setRootActor(imageGroup);


        float[][] answers = control.getAnswers();
        int[] correctAnswers = control.getCorrectAnswers();

        for (int i = 0; i < answers.length; i++) {
            PolygonActor polygonActor = new PolygonActor();
            polygonActor.setPolygon(answers[i]);
            imageGroup.addActor(polygonActor);
            polygonActor.setColor(Color.RED);
            for (int j = 0; j < correctAnswers.length; j++) {
                int correctAnswer = correctAnswers[j];
                if(i == correctAnswer) {
                    polygonActor.setColor(Color.GREEN);
                    break;
                }
            }
        }

        for (int i = 0; i < markers.size; i++) {
            Button button = markers.get(i);

            boolean correctAnswer = false;
            for (int j = 0; j < correctAnswers.length; j++) {
                float[] answer = answers[correctAnswers[j]];

                Polygon polygon = new Polygon(answer);

                if (polygon.contains(button.getX() + button.getWidth() * .5f,
                        button.getY() + button.getHeight() * .5f)) {
                    correctAnswer = true;
                    break;
                }
            }
            final IconButton mapMarker = es.eucm.cytochallenge.view.widgets.WidgetBuilder.toolbarIcon(SkinConstants.IC_MAPMARKER);

            if(correctAnswer) {
                mapMarker.getIcon().setColor(Color.GREEN);
            } else {
                mapMarker.getIcon().setColor(Color.RED);
            }

            imageGroup.addActor(mapMarker);
            mapMarker.pack();
            mapMarker.setOrigin(Align.center);
            mapMarker.setPosition(button.getX(), button.getY());
        }

        Label label = new Label(control.getText(), getSkin(),
                SkinConstants.STYLE_TOOLBAR);
        label.setAlignment(Align.center);

        ScrollPane horizontalScroll = new ScrollPane(label);
        horizontalScroll.setScrollingDisabled(false, true);

        addDescription(horizontalScroll);

        return new Actor[]{
                slideEditor
        };
    }
    protected String[] buildTabNames(I18NBundle i18n) {
        return new String[]{
                i18n.get("result")
        };
    }
}
