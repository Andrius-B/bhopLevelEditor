package com.bhop.editor.Operations;

import com.bhop.editor.Util.ClipBoard;
import com.bunny.jump.Game.Objects.Object;

import java.util.ArrayList;


/**
 * Created by Andrius on 6/26/2017.
 */

public class SelectOperation extends Operation{
    ClipBoard clip;
    boolean click;

    /**
     * Constructor assumes that this operation is created upon click
     * and updated upon drag
     * @param clip - this operation only manipulates the selection, that is contained in the ClipBoard
     */
    public SelectOperation(ClipBoard clip){
        this.click = true;
        this.clip = clip;
    }

    @Override
    public void applyOperation() {
        if(this.stored instanceof ArrayList){
            if(click){
                System.out.print("Click select requested!\n");
                clip.setSelection((ArrayList<Object>) this.changed);
            }else {
                clip.setSelection((ArrayList<Object>) this.changed);
            }
        }
    }

    @Override
    public void undoOperation(){
        clip.setSelection((ArrayList<Object>) this.stored);
    }

    public boolean isClick(){
        return click;
    }
    public void setClick(boolean click){
        this.click = click;
    }
}
