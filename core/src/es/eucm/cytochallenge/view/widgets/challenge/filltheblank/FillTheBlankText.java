package es.eucm.cytochallenge.view.widgets.challenge.filltheblank;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.SnapshotArray;
import es.eucm.cytochallenge.model.control.filltheblank.FillTheBlankStatement;
import es.eucm.cytochallenge.view.widgets.AbstractWidget;
import es.eucm.cytochallenge.view.widgets.WidgetBuilder;


public class FillTheBlankText extends TextButton {

    private final SelectBox.SelectBoxStyle selectBoxStyle;
    private final FillTheBlankStyle fillTheBlankStyle;
    private Skin skin;
    private int totalAnswers, correctAnswers;

    public FillTheBlankText(Skin skin) {
        this(skin, skin.get("default", FillTheBlankStyle.class));
    }

    public FillTheBlankText(Skin skin, FillTheBlankStyle fillTheBlankStyle) {
        super("", fillTheBlankStyle);
        this.fillTheBlankStyle = fillTheBlankStyle;
        selectBoxStyle = skin.get(fillTheBlankStyle.selectBoxStyle,
                SelectBox.SelectBoxStyle.class);
        setSkin(skin);
    }

    public void init(FillTheBlankStatement statement) {
        String text = statement.getText();

        String[][] options = statement.getOptions();

        float leftPad = WidgetBuilder.dpToPixels(8f);
        int tokeenOffset = 0;
        for (int i = 0; i < options.length; i++) {

            String[] option = options[i];

            String token = "[" + i + "]";
            int tokenIndex = text.indexOf(token);
            String currentText = text.substring(tokeenOffset, tokenIndex);

            Label label;
            if(i == 0) {
                label = getLabel();
                label.setText(currentText);
                getLabelCell().padLeft(leftPad);
            } else {
                label = new Label(currentText, getLabel().getStyle());
                add(label);
            }
            label.setAlignment(Align.left);

            tokeenOffset = tokenIndex + token.length();

            SelectBox<String> optionsBox = new SelectBox<String>(selectBoxStyle);
            optionsBox.setItems(option);
            optionsBox.setUserObject(statement.getCorrectAnswers()[i]);

            add(optionsBox);
        }
        totalAnswers = options.length;

        if(tokeenOffset < text.length()) {
            String currentText = text.substring(tokeenOffset, text.length());
            Label label = new Label(currentText, getLabel().getStyle());
            label.setAlignment(Align.left);
            add(label);
        }
    }

    public int getTotalAnswers() {
        return totalAnswers;
    }

    public int getCorrectAnswers() {
        correctAnswers = 0;
        SnapshotArray<Actor> children = getChildren();
        for (int i = 0; i < children.size; i++) {
            Actor child = children.get(i);
            if(child instanceof SelectBox) {
                SelectBox childBox = (SelectBox) child;
                if(childBox.getSelectedIndex() == (Integer)childBox.getUserObject()) {
                    correctAnswers++;
                }
            }
        }
        return correctAnswers;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public Skin getSkin() {
        return skin;
    }

    public static class FillTheBlankStyle extends TextButton.TextButtonStyle {

        String selectBoxStyle;

    }
}
