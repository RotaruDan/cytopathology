package es.eucm.cytochallenge.view.widgets;


import com.badlogic.gdx.scenes.scene2d.ui.Image;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.screens.BaseScreen;

public class LabBackground extends AbstractWidget {

    private Image clock;
    private Image booksLeft, booksRight, board, doctorBoard, desk;

    public LabBackground() {
        clock = new Clock(BaseScreen.skin);

        booksLeft = new Image(BaseScreen.skin.getDrawable(SkinConstants.DRAWABLE_BOOKS_LEFT));
        booksRight = new Image(BaseScreen.skin.getDrawable(SkinConstants.DRAWABLE_BOOKS_RIGHT));
        desk = new Image(BaseScreen.skin.getDrawable(SkinConstants.DRAWABLE_DESK));
        board = new Image(BaseScreen.skin.getDrawable(SkinConstants.DRAWABLE_BOARD));
        doctorBoard = new Image(BaseScreen.skin.getDrawable(SkinConstants.DRAWABLE_DOCTOR_BOARD));

        addActor(clock);
        addActor(booksLeft);
        addActor(booksRight);
        addActor(board);
        addActor(doctorBoard);
        addActor(desk);
    }

    @Override
    public void layout() {
        float width = getWidth();
        float height = getHeight();

        float clockPrefW = getPrefWidth(clock);
        float clockPrefH = getPrefHeight(clock);
        setBounds(clock, (width - clockPrefW) * .5f, (height - clockPrefH),
                clockPrefW, clockPrefH);


        float booksLPrefW = getPrefWidth(booksLeft);
        float booksLPrefH = getPrefHeight(booksLeft);
        setBounds(booksLeft, 0, (height - booksLPrefH) * .85f,
                booksLPrefW, booksLPrefH);


        float booksRPrefW = getPrefWidth(booksRight);
        float booksRPrefH = getPrefHeight(booksRight);
        setBounds(booksRight, (width - booksRPrefW) * .95f, (height - booksRPrefH) * .85f,
                booksRPrefW, booksRPrefH);


        float deskPrefW = getPrefWidth(desk);
        float deskPrefH = getPrefHeight(desk);
        setBounds(desk, 0, (height - deskPrefH) * .0f,
                deskPrefW, deskPrefH);


        float boardPrefW = getPrefWidth(board);
        float boardPrefH = getPrefHeight(board);
        setBounds(board, desk.getX() + deskPrefW * .85f, desk.getY() + deskPrefH * .3f,
                boardPrefW, boardPrefH);


        float doctorBoardPrefW = getPrefWidth(doctorBoard);
        float doctorBoardPrefH = getPrefHeight(doctorBoard);
        setBounds(doctorBoard, board.getX() + boardPrefW * .4f, desk.getY(),
                doctorBoardPrefW, doctorBoardPrefH);
    }
}
