package com.bhop.editor.Util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by Andrius on 6/25/2017.
 */

public class InputEvent {
    public boolean shiftModifier = false;
    public boolean ctrlModifier = false;
    public int keycode = -1;
    public boolean updateRequest = false;

    public InputEvent(int keycode){
        ctrlModifier = Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT);
        shiftModifier = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
        this.keycode = keycode;
    }
}
