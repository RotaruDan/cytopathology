package es.eucm.cytochallenge.model.control.filltheblank;

import es.eucm.cytochallenge.model.control.TextControl;

/**
 * Created by dan on 23/02/2016.
 */
public class FillTheBlankControl extends TextControl {

    private FillTheBlankStatement[] statements;

    public FillTheBlankStatement[] getStatements() {
        return statements;
    }

    public void setStatements(FillTheBlankStatement[] statements) {
        this.statements = statements;
    }
}
