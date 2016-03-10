package es.eucm.cytochallenge.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.*;
import es.eucm.cytochallenge.model.*;
import es.eucm.cytochallenge.model.control.filltheblank.FillTheBlankControl;
import es.eucm.cytochallenge.model.control.filltheblank.FillTheBlankStatement;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.transitions.Fade;
import es.eucm.cytochallenge.view.widgets.challenge.ChallengeLayout;
import es.eucm.cytochallenge.view.widgets.challenge.TextChallengeWidget;

public class Challenges extends BaseScreen {

    private ChallengeLayout challengeLayout = new ChallengeLayout();
    private TextChallengeWidget currentChallenge;
    private Button check;
    private String challengePath;

    @Override
    public void create() {
        super.create();

        final Button back;

        back = es.eucm.cytochallenge.view.widgets.WidgetBuilder.toolbarIcon(SkinConstants.IC_UNDO);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onBackPressed();
            }
        });

        check = es.eucm.cytochallenge.view.widgets.WidgetBuilder.toolbarIcon(SkinConstants.IC_CHECK);
        check.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentChallenge.setUpScore();
                check.setVisible(false);
            }
        });

        challengeLayout.setBackButton(back);
        challengeLayout.setCheckButton(check);

        root.add(challengeLayout).expand().fill();
    }

    @Override
    public void show() {
        super.show();

        Json json = new Json();
        Challenge challenge = json.fromJson(Challenge.class,
                Gdx.files.internal(challengePath + "challenge.json"));

        currentChallenge = new TextChallengeWidget();
        currentChallenge.setChallengePath(challengePath);
        currentChallenge.init((TextChallenge) challenge);

        challengeLayout.setContent(currentChallenge.getWidget());

        check.setVisible(true);
        // print();
    }

    private void print() {
        TextChallenge ftb = new TextChallenge();

        FillTheBlankControl tc = new FillTheBlankControl();

        tc.setText("Endocervical columnar cells – cytologic criteria");

        FillTheBlankStatement[] sts = new FillTheBlankStatement[12];

        FillTheBlankStatement sts0 = new FillTheBlankStatement();
        sts0.setText("Cell shape – [0]");
        sts0.setOptions(new String[][]{new String[]{"squamous", "columnar", "cuboidal"}});
        sts0.setCorrectAnswers(new int[]{1});

        sts[0] = sts0;

        tc.setStatements(sts);

        ftb.setTextControl(tc);
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        System.out.println(json.prettyPrint(ftb));
    }

    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public void onBackPressed() {
        game.changeScreen(challengeList, Fade.init(1f, true));
    }

    public void setChallengePath(String challengePath) {
        this.challengePath = challengePath;
    }
}