/**
 * eAdventure is a research project of the
 *    e-UCM research group.
 *
 *    Copyright 2005-2014 e-UCM research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    e-UCM is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          CL Profesor Jose Garcia Santesmases 9,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.eucm.cytochallenge.view.widgets.slide.inputstatemachine;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public abstract class InputState {

	public void enter() {

	}

	public void exit() {

	}

	public void touchDown1(InputEvent event, float x, float y) {

	}

	public void touchDown2(InputEvent event, float x, float y) {

	}

	public void touchDown(InputEvent event, float x, float y, int pointer) {

	}

	public void touchUp1(InputEvent event, float x, float y) {

	}

	public void touchUp2(InputEvent event, float x, float y) {

	}

	public void touchUp(InputEvent event, float x, float y, int pointer) {

	}

	public void dragStart1(InputEvent event, float x, float y) {

	}

	public void dragStart2(InputEvent event, float x, float y) {

	}

	public void dragStart(InputEvent event, float x, float y, int pointer) {

	}

	public void drag1(InputEvent event, float x, float y) {

	}

	public void drag2(InputEvent event, float x, float y) {

	}

	public void drag(InputEvent event, float x, float y, int pointer) {

	}

	public void longPress(float x, float y) {

	}

	protected boolean isTouchCancelled(InputEvent event, float x, float y) {
		Actor actor = event.getTarget();
		Actor hit = actor.hit(x, y, true);
		return !(hit == actor || (hit != null && hit.isDescendantOf(actor)));
	}

	public boolean scrolled(InputEvent event, float x, float y, int amount) {
		return false;
	}
}
