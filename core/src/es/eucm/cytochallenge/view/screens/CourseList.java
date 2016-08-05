package es.eucm.cytochallenge.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Scaling;
import es.eucm.cytochallenge.model.Challenge;
import es.eucm.cytochallenge.model.TextChallenge;
import es.eucm.cytochallenge.model.course.Course;
import es.eucm.cytochallenge.utils.ChallengeResourceProvider;
import es.eucm.cytochallenge.utils.InternalFilesChallengeResourceProvider;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.transitions.Fade;
import es.eucm.cytochallenge.view.widgets.*;

public class CourseList extends BaseScreen {

    @Override
    public void create() {
        super.create();

        Gallery gallery = new Gallery(3, 3, skin.get("navigation", Gallery.GalleryStyle.class));

        loadCourses(gallery);

        ScrollPane scroll = new ScrollPane(gallery, BaseScreen.skin, "verticalScroll");
        scroll.setScrollingDisabled(true, false);

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
        rootLayout.setContainer(scroll);

        root.add(rootLayout).expand().fill();
    }

    private void loadCourses(final Gallery layout) {

        Json json = new Json();
        float pad = WidgetBuilder.dpToPixels(24f);
        String challengesPath = "challenges/";
        String challengeJson = "courses.json";
        Array<Course> courses = json.fromJson(Array.class,
                Gdx.files.internal(challengesPath + challengeJson));

        for (int i = 0; i < courses.size; i++) {
            final Course course = courses.get(i);

            Tile tile = new Tile(skin);
            tile.setText(course.getName());

            layout.add(tile);
            tile.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    challengeList.setCurrentCourse(course);
                    game.changeScreen(challengeList);
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
        game.changeScreen(menu, Fade.init(1f, true));
    }


}