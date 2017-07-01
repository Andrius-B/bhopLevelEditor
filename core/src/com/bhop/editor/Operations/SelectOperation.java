package com.bhop.editor.Operations;

import com.bhop.editor.Util.ClipBoard;
import com.bunny.jump.Game.Objects.Object;

import java.util.ArrayList;


/**
 * Created by Andrius on 6/26/2017.
 */

public class SelectOperation extends Operation{
    ClipBoard clip;
    public SelectOperation(ClipBoard clip){
        this.clip = clip;
    }

    @Override
    public void applyOperation() {
        if(this.stored instanceof ArrayList){
            clip.setSelection((ArrayList<Object>) this.changed);
        }
    }

    @Override
    public void undoOperation(){
        clip.setSelection((ArrayList<Object>) this.stored);
    }
}
