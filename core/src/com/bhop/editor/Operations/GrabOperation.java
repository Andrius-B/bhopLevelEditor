package com.bhop.editor.Operations;

import com.badlogic.gdx.math.Vector2;
import com.bunny.jump.Game.Objects.Object;

import java.util.ArrayList;

/**
 * Grab operation for moving groups of objects by set relative amount
 * also contains mounting with mountOperation and unmountOperation
 */
public class GrabOperation extends Operation {

    private Vector2 move = null;
    private boolean mounted = false;

    public void setMove(Vector2 relMove){
        boolean wasMounted = mounted;
        unmountOperation();
        this.move.set(relMove);
        if(wasMounted){
            mountOperation();
        }
    }

    @Override
    public void setOperation(java.lang.Object original, java.lang.Object changed){
        boolean wasMounted = mounted;
        unmountOperation();
        this.stored = original;
        this.original = original;
        this.changed = changed;
        if(wasMounted){
            mountOperation();
        }
    }


    public void mountOperation(){
        System.out.print("Grab operation mounted");
        mounted = true;
        applyOperation();
    }
    public void unmountOperation(){
        if(mounted) {
            System.out.print("Grab operation un-mounted");
            mounted = false;
            undoOperation();
        }
    }

    @Override
    public void applyOperation() {
        unmountOperation();
        System.out.print("Grab operation applied");
        for(Object o: ((ArrayList<Object>) original)){
            Vector2 oPos = o.getPosition();
            oPos = oPos.add(move);
            o.setPosition(oPos);
        }
    }

    @Override
    public void undoOperation() {
        for(Object o: ((ArrayList<Object>) original)){
            Vector2 oPos = o.getPosition();
            oPos = oPos.sub(move);
            o.setPosition(oPos);
        }
    }
}
