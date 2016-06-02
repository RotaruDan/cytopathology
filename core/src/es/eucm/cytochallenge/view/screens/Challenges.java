package es.eucm.cytochallenge.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.*;
import es.eucm.cytochallenge.model.*;
import es.eucm.cytochallenge.model.control.filltheblank.FillTheBlankControl;
import es.eucm.cytochallenge.model.control.filltheblank.FillTheBlankStatement;
import es.eucm.cytochallenge.model.hint.Hint;
import es.eucm.cytochallenge.model.hint.ImageInfo;
import es.eucm.cytochallenge.model.hint.Info;
import es.eucm.cytochallenge.model.hint.TextInfo;
import es.eucm.cytochallenge.utils.ChallengeResourceProvider;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.transitions.Fade;
import es.eucm.cytochallenge.view.widgets.CirclesMenu;
import es.eucm.cytochallenge.view.widgets.HintDialog;
import es.eucm.cytochallenge.view.widgets.WidgetBuilder;
import es.eucm.cytochallenge.view.widgets.challenge.ChallengeLayout;
import es.eucm.cytochallenge.view.widgets.challenge.TextChallengeWidget;

public class Challenges extends BaseScreen {

    private ChallengeLayout challengeLayout = new ChallengeLayout();
    private TextChallengeWidget currentChallenge;
    private Button check, hint;
    private Button addButton;
    private ChallengeResourceProvider challengeResourceProvider;
    ClickListener hideListener;

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

        /*
        final CirclesMenu circlesMenu = buildAddButtons();
        addButton = es.eucm.cytochallenge.view.widgets.WidgetBuilder.circleButton(SkinConstants.IC_ADD);
        addButton.pack();
        circlesMenu.pack();
        hideListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                circlesMenu.hide(new Runnable() {
                    @Override
                    public void run() {
                        circlesMenu.remove();
                        challengeLayout.removeListener(hideListener);
                    }
                });
            }
        };
        addButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                challengeLayout.addActor(circlesMenu);
                circlesMenu.show();
                circlesMenu.setPosition(addButton.getX()
                                - circlesMenu.getWidth()
                                + addButton.getWidth(),
                        addButton.getY());
                challengeLayout.addListener(hideListener);
            }
        });
        */

        hint = WidgetBuilder.circleButton(SkinConstants.IC_ERROR);
        hint.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Hint hint = currentChallenge.getChallenge().getHint();
                if (hint != null) {
                    HintDialog dialog = new HintDialog(skin, hint, i18n, challengeResourceProvider);
                    menu.getStage().addActor(dialog);
                    dialog.show();
                }
            }
        });

        check = WidgetBuilder.circleButton(SkinConstants.IC_CHECK);
        addButton = check;
        check.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentChallenge.setUpScore();
                addButton = hint;
                check.remove();
                Hint hintInfo = currentChallenge.getChallenge().getHint();
                if (hintInfo != null) {
                    challengeLayout.setCheckButton(hint);
                }

            }
        });

        challengeLayout.setCheckButton(addButton);
        challengeLayout.setBackButton(back);

        root.add(challengeLayout).expand().fill();
    }

    private CirclesMenu buildAddButtons() {


        final CirclesMenu menu = new CirclesMenu(Align.right);

        menu.defaults().space(WidgetBuilder.dpToPixels(16));
        ImageButton check = WidgetBuilder.circleButton(SkinConstants.IC_CHECK);
        check.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentChallenge.setUpScore();
                addButton.setVisible(false);

            }
        });
        menu.add(check);
        ImageButton hint = WidgetBuilder.circleButton(SkinConstants.IC_ERROR);
        hint.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Hint hint = currentChallenge.getChallenge().getHint();
                if (hint != null) {
                    HintDialog dialog = new HintDialog(skin, hint, i18n, challengeResourceProvider);
                    menu.getStage().addActor(dialog);
                    dialog.show();
                }
            }
        });
        menu.add(hint);
        ImageButton close = WidgetBuilder.circleButton(SkinConstants.IC_CLOSE);
        menu.add(close);
        return menu;
    }

    @Override
    public void show() {
        super.show();

        challengeResourceProvider.getChallenge("challenge.json", new ChallengeResourceProvider.ResourceProvidedCallback<Challenge>() {
            @Override
            public void loaded(Challenge resource) {

                currentChallenge = new TextChallengeWidget(skin, i18n);
                currentChallenge.setChallengeResourceProvider(challengeResourceProvider);
                currentChallenge.init((TextChallenge) resource);

                challengeLayout.setContent(currentChallenge.getWidget());

                addButton = check;
                challengeLayout.setCheckButton(check);
            }

            @Override
            public void failed() {

            }
        });

        // print();
    }

    private void print() {
        TextChallenge ftb = new TextChallenge();
        ftb.setDifficulty(Difficulty.MEDIUM);

        FillTheBlankControl tc = new FillTheBlankControl();

        tc.setText("Endocervical columnar cells  cytologic criteria");

        FillTheBlankStatement[] sts = new FillTheBlankStatement[12];

        FillTheBlankStatement sts0 = new FillTheBlankStatement();
        sts0.setText("Cell shape  [0]");
        sts0.setOptions(new String[][]{new String[]{"squamous", "columnar", "cuboidal"}});
        sts0.setCorrectAnswers(new int[]{1});

        sts[0] = sts0;

        tc.setStatements(sts);

        ftb.setTextControl(tc);

        Hint hint = new Hint();

        TextInfo t1 = new TextInfo();
        t1.setText("They comprise the top layers of stratified (many layers) non-keratinized squamous epithelium");

        ImageInfo i1 = new ImageInfo();
        i1.setImagePath("superficialSquamousCellsInfo1.jpg");

        ImageInfo i2 = new ImageInfo();
        i2.setImagePath("superficialSquamousCellsInfo2.jpg");

        TextInfo t2 = new TextInfo();
        t2.setText("Non-keratinizing squamous epithelium lines labia minora, vagina and ectocervix");


        TextInfo t3 = new TextInfo();
        t3.setText("These cells are in layers to provide a protective function");


        TextInfo t4 = new TextInfo();
        t4.setText("These cells are mature squamous cells that are not capable of any metabolic activity and are prone to exfoliation");


        TextInfo t5 = new TextInfo();
        t5.setText("The squamous epithelium will mature all the way upto superficial cell layers under the hormonal influence of female sex hormone, estrogen, secreted in the first half of the menstrual cycle by the ovary in cycling women");

        Info[] infos = new Info[]{
                t1,
                i1,
                t2,
                t3,
                i2,
                t4,
                t5
        };

        hint.setInfos(infos);

        ftb.setHint(hint);

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
        if(challengeList != null) {
            game.changeScreen(challengeList, Fade.init(1f, true));
        } else {
            game.changeScreen(challenges, Fade.init(1f, true));
        }
    }

    public void setChallengeResourceProvider(ChallengeResourceProvider challengeResourceProvider) {
        this.challengeResourceProvider = challengeResourceProvider;
    }
}