/**
 * eAdventure is a research project of the
 * e-UCM research group.
 * <p/>
 * Copyright 2005-2014 e-UCM research group.
 * <p/>
 * You can access a list of all the contributors to eAdventure at:
 * http://e-adventure.e-ucm.es/contributors
 * <p/>
 * e-UCM is a research group of the Department of Software Engineering
 * and Artificial Intelligence at the Complutense University of Madrid
 * (School of Computer Science).
 * <p/>
 * CL Profesor Jose Garcia Santesmases 9,
 * 28040 Madrid (Madrid), Spain.
 * <p/>
 * For more info please visit:  <http://e-adventure.e-ucm.es> or
 * <http://www.e-ucm.es>
 * <p/>
 * ****************************************************************************
 * <p/>
 * This file is part of eAdventure
 * <p/>
 * eAdventure is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * eAdventure is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.eucm.cytochallenge.view.widgets.slide.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import es.eucm.cytochallenge.view.widgets.slide.SlideEditor;
import es.eucm.cytochallenge.view.widgets.slide.inputstatemachine.InputStateMachine;

public class EditStateMachine extends InputStateMachine {

    private SlideEditor slideEditor;

    public EditStateMachine(SlideEditor slideEditor) {
        this.slideEditor = slideEditor;
        addState(new NoPointersState(this));
        addState(new NothingPressedState(this));
        addState(new CameraState(this));
        setState(NoPointersState.class);
    }

    @Override
    public void drag(InputEvent event, float x, float y, int pointer) {
        super.drag(event, x, y, pointer);
    }

    void enterFullScreen() {
        slideEditor.enterFullScreen();
    }

    void exitFullScreen() {
        slideEditor.exitFullScreen();
    }

    void exitPan() {
        exitFullScreen();
        slideEditor.fireContainerUpdated();
    }

    public void pan1() {
        slideEditor.pan(-getDeltaX1(), -getDeltaY1(), false);
    }

    public void pan2() {
        slideEditor.pan(-getDeltaX2(), -getDeltaY2(), false);
    }

    public void cancelTouchFocus() {
        slideEditor.getStage().cancelTouchFocus(slideEditor);
    }

    public float getZoom() {
        return slideEditor.getZoom();
    }

    public void zoom(float centerX, float centerY, float zoom) {
        slideEditor.zoom(centerX, centerY, zoom, false);
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {

        Gdx.app.log(getClass().getSimpleName(),
                " mouse enter");

        Stage stage = slideEditor.getStage();
        if (stage != null) {
            stage.setScrollFocus(slideEditor);
        }
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {

        Gdx.app.log(getClass().getSimpleName(),
                " mouse exit " + pointer);

        if (pointer != -1) {
            return;
        }
        Stage stage = slideEditor.getStage();
        if (stage != null) {
            stage.setScrollFocus(null);
        }
    }

    @Override
    public void setState(Class nextStateClass) {
        Gdx.app.log(getClass().getSimpleName(),
                "set state " + nextStateClass.getSimpleName());
        super.setState(nextStateClass);
    }
}
