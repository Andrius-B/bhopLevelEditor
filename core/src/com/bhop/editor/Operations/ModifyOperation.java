package com.bhop.editor.Operations;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;
import com.bhop.editor.Util.ClipBoard;
import com.bunny.jump.Game.Objects.Object;
import com.bunny.jump.Game.Objects.Platform;
import com.bunny.jump.Game.Objects.ResetBox;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Andrius on 6/26/2017.
 */

public class ModifyOperation extends Operation {
    private Vector2 move;

    private Vector2 absSize;
    private Vector2 oldAbsSize;

    private Float jumpVel;
    private Float oldJumpVel;

    private Vector2 resetPoint;
    private Vector2 oldResetPoint;

    private Boolean resetSpeed;
    private Boolean oldResetSpeed;

    private String texturePath;
    private String oldTexturePath;

    private Boolean repeatTexture;
    private Boolean oldRepeatTexture;


    public ModifyOperation(ArrayList<Object> objects){
        for(Object o: objects){
            System.out.print("Testing object content in the modifier constructor:"+o.toString()+"\t");
            System.out.print("dim:"+o.getDimension().toString()+"\n");
        }
        this.stored = objects;
        this.original = objects;
    }

    public void setMove(Vector2 rel){
        this.move = rel;
    }
    public void setAbsoluteSize(Vector2 dim){
        for(Object o: ((ArrayList<Object>)original)){
            oldAbsSize = o.getDimension();
        }
        this.absSize = dim;
    }

    public void setJump(float jmp){
        for(Object o: ((ArrayList<Object>)original)){
            oldJumpVel = ((Platform)o).getJumpVel();
        }
        this.jumpVel = jmp;
    }
    public void setResetPoint(Vector2 resetPoint){
        for(Object o: ((ArrayList<Object>)original)){
            /*
                This function is unavailable for objects, that do not have a resetPoint
                ObjectPropertyResolver is supposed to take care of this.
             */
            oldResetPoint = ((ResetBox)o).getResetPoint();
        }
        this.resetPoint = resetPoint;
    }

    public void setResetSpeed(boolean resetSpeed){
        for(Object o: ((ArrayList<Object>)original)){
            this.oldResetSpeed = ((ResetBox)o).getRemoveSpeed();
        }
        this.resetSpeed = new Boolean(resetSpeed);
    }

    public void setTexture(String path){
        for(Object o: ((ArrayList<Object>)original)){
            oldTexturePath = o.filepath;
        }
        this.texturePath = path;
    }
    public void setRepeatTexture(boolean repeat){
        for(Object o: ((ArrayList<Object>)original)){
            this.oldRepeatTexture = o.repeat;
        }
        this.repeatTexture = repeat;
    }


    /**
     * This operation only handles single object modifications - for group modifications
     * the GroupModifyOperation is used ( on second thought entering relative number
     * doesn't really make sense, so for rel operations some new type of input should be used)
     * Though the this.original and changed are ArrayLists just for consistency.
     * I don't think the overhead is something to be scared of... :^)
     */
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
            if(move!=null) {
                Vector2 pos = o.getPosition();
                pos.add(move);
                o.setPosition(pos);
                System.out.print("Moving:"+move.toString()+"\n");
            }
            if(absSize!=null) {
                o.setDimension(absSize);
                System.out.print("Resizing:"+absSize.toString()+"\n");
                System.out.print("Sprite size:"+o.getDimension()+"\n");
            }
            if(texturePath!=null) {
                o.filepath = texturePath;
                System.out.print("New texturePath:"+texturePath+"\n");
            }
            if(repeatTexture!=null){
                o.repeat = repeatTexture;
                System.out.print("Changed repeat:"+repeatTexture+"\n");
            }
            if(resetPoint!=null){
                ((ResetBox)o).setResetPoint(resetPoint);
                System.out.print("New ResetPoint:"+resetPoint.toString()+"\n");
            }
            if(jumpVel!=null) {
                o.jumpVel = jumpVel;
                System.out.print("Changed jumpVel:"+jumpVel+"\n");
            }
            if(resetSpeed!=null){
                ((ResetBox)o).setRemoveSpeed(resetSpeed);
                System.out.print("Changed resetSpeed:"+resetSpeed.toString()+"\n");
            }
        }
    }

    @Override
    public void undoOperation(){
        for(Object o: ((ArrayList<Object>) original)){
            System.out.print("Un-doing modify operation\n");
            if(move!=null) {
                Vector2 pos = o.getPosition();
                pos = pos.sub(move);
                o.setPosition(pos);
            }
            if(oldAbsSize!=null) {
                o.setDimension(oldAbsSize);
            }
            if(oldTexturePath!=null) {
                o.filepath = oldTexturePath;
            }
            if(resetPoint!=null){
                ((ResetBox)o).setResetPoint(oldResetPoint);
            }
            if(oldJumpVel!=null) {
                o.jumpVel = oldJumpVel;
            }
            if(oldResetSpeed!=null){
                ((ResetBox)o).setRemoveSpeed(oldResetSpeed);
            }
            if(oldRepeatTexture!=null){
                o.repeat = oldRepeatTexture;
            }
        }
    }

    /**
     * This function is used for previewing changes real-time without adding the operation to the stack
     */
    public void mount(){
        System.out.print("Mounting the operation\n");
        //// TODO: 7/24/2017  
    }

    /**
     * Internal function for unmounting the preview.
     * Should be called:
     *      -Before applying
     *   or
     *      -Before discarding the operation
     */
    private void unmount(){
        System.out.print("Unmounting the modify operation\n");
        //// TODO: 7/24/2017
    }
}
