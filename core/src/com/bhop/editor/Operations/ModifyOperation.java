package com.bhop.editor.Operations;

import com.bunny.jump.Game.Objects.Object;

/**
 * Created by Andrius on 6/26/2017.
 */

public class ModifyOperation extends Operation {
    private Object object;
    public ModifyOperation(Object object){
        this.object = object;
    }

    @Override
    public void applyOperation(){
        object = (Object) changed;
    }

    @Override
    public void undoOperation(){
        object = (Object) original;
    }
}
