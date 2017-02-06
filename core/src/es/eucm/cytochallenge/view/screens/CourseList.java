package es.eucm.cytochallenge.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Scaling;
import es.eucm.cytochallenge.model.Challenge;
import es.eucm.cytochallenge.model.Difficulty;
import es.eucm.cytochallenge.model.TextChallenge;
import es.eucm.cytochallenge.model.course.Course;
import es.eucm.cytochallenge.utils.ChallengeResourceProvider;
import es.eucm.cytochallenge.utils.InternalFilesChallengeResourceProvider;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.transitions.Fade;
import es.eucm.cytochallenge.view.transitions.TransitionManager;
import es.eucm.cytochallenge.view.widgets.*;
import es.eucm.cytochallenge.view.widgets.challenge.CourseTile;

public class CourseList extends BaseScreen {

    private Gallery gallery;

    @Override
    public void create() {
        super.create();

        gallery = new Gallery(2.26f, 3, skin.get("navigation", Gallery.GalleryStyle.class));

        Table topTable = new Table();
        topTable.background(BaseScreen.skin.getDrawable(SkinConstants.DRAWABLE_9P_TOOLBAR));
        topTable.setColor(SkinConstants.COLOR_TOOLBAR_TOP);

        Label label = new Label(i18n.get("courses"), BaseScreen.skin,
                SkinConstants.STYLE_TOOLBAR);

        Button icon = WidgetBuilder.toolbarIcon(SkinConstants.IC_UNDO);
        icon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onBackPressed();
            }
        });

        topTable.add(icon);
        topTable.add(label)
                .expandX();

        TopToolbarLayout rootLayout = new TopToolbarLayout();
        rootLayout.setTopToolbar(topTable);
        rootLayout.setContainer(gallery);

        root.add(rootLayout).expand().fill();
    }

    @Override
    public void show() {
        super.show();
        loadCourses();
    }

    private void loadCourses() {

        gallery.clearChildren();
        Json json = new Json();
        String challengesPath = "challenges/";
        String challengeJson = "courses.json";
        Array<Course> courses = json.fromJson(Array.class,
                Gdx.files.internal(challengesPath + challengeJson));

        for (int i = 0; i < courses.size; i++) {
            final Course course = courses.get(i);

            CourseTile tile = new CourseTile(course, skin, i18n, prefs);

            gallery.add(tile);
            tile.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    challengeList.setCurrentCourse(course);
                    game.changeScreen(challengeList, Fade.init(1f));
                }
            });
        }
    }

    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public void onBackPressed() {
        game.changeScreen(menu, Fade.init(1f));
    }


}