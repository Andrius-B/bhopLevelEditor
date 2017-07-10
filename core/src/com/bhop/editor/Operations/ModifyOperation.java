package com.bhop.editor.Operations;

import com.badlogic.gdx.math.Vector2;
import com.bhop.editor.Util.ClipBoard;
import com.bunny.jump.Game.Objects.Object;

/**
 * Created by Andrius on 6/26/2017.
 */

public class ModifyOperation extends Operation {
    private ClipBoard clip;
    private Vector2 move;
    private Vector2 relSize;
    private float jumpVel;
    private Vector2 resetPoint;
    public ModifyOperation(ClipBoard clip){
        this.clip = clip;
    }

    public void setMove(Vector2 rel){

    }

    @Override
    public void applyOperation(){
        //object = (Object) changed;
    }

    @Override
    public void undoOperation(){
        //object = (Object) original;
    }
}
