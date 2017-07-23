package com.bhop.editor.Operations;

import com.badlogic.gdx.math.Vector2;
import com.bhop.editor.Util.ClipBoard;
import com.bunny.jump.Game.Objects.Object;

import java.util.ArrayList;

/**
 * Created by Andrius on 6/26/2017.
 */

public class ModifyOperation extends Operation {
    private Vector2 move;
    private Vector2 relSize;
    private float jumpVel;
    private Vector2 resetPoint;

    public ModifyOperation(ArrayList<Object> objects){
        for(Object o: objects){
            System.out.print("Testing object content in the modifier constructor:"+o.toString()+"\t");
            System.out.print("pos:"+o.getPosition().toString()+"\n");
        }
        this.stored = objects;
        this.original = objects;
    }

    public void setMove(Vector2 rel){
        this.move = rel;
    }

    @Override
    public void applyOperation(){
        //System.out.print("Object stack on modOp:"+original.toString());
        /*for(Object o: ((ArrayList<Object>) this.original)){
            System.out.print("Testing object content in the modifier apply func original:"+o.toString()+"\t");
            System.out.print("pos:"+o.getPosition().toString()+"\n");
        }
        for(Object o: ((ArrayList<Object>) this.stored)){
            System.out.print("Testing object content in the modifier apply func stored:"+o.getPosition().toString()+"\n");
        }*/
        for(Object o: ((ArrayList<Object>) this.original)){
            System.out.print("Applying modify operation\n");
            Vector2 pos = o.getPosition();
            System.out.print("Before modify pos:"+pos.toString()+"\n");
            pos.add(move);
            System.out.print("After modify pos:"+pos.toString()+"\n");
            o.setPosition(pos);
        }
    }

    @Override
    public void undoOperation(){
        for(Object o: ((ArrayList<Object>) original)){
            System.out.print("Un-doing modify operation\n");
            Vector2 pos = o.getPosition();
            pos.sub(move);
            o.setPosition(pos);
        }
    }
}
