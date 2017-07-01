package com.bhop.editor.Util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bhop.editor.LevelEditor;
import com.bhop.editor.Operations.ModifyOperation;
import com.bhop.editor.Operations.Operation;
import com.bhop.editor.Operations.SelectOperation;
import com.bhop.editor.Renderer;
import com.bunny.jump.Game.Objects.InputDisplays.InputDisplayContainer;
import com.bunny.jump.Game.Objects.Object;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Andrius on 6/25/2017.
 */

public class InputEventProcessor {
    private Renderer parent;
    public ClipBoard clip;
    /**
     * Variables for containing information about user input
     */
    private float scroll;
    private Rectangle selectBox;
    private int selectPointer;
    private Vector2 selectTouch;
    private Operation currentOperation;

    /**
     * Operation stack
     */
    Stack<Operation> operations;


    public InputEventProcessor(Renderer l){
        clip = new ClipBoard();
        operations = new Stack<Operation>();

        parent = l;
        scroll = 10; //scroll*scroll/100 = % of screen seen, so the default is 10

        selectTouch = new Vector2();
        selectBox = new Rectangle();
        selectPointer = -1;
    }
    public void processKeyDownEvent(int keycode){
        InputEvent e = new InputEvent(keycode);
        if(e.keycode == Input.Keys.SPACE && !e.shiftModifier && !e.ctrlModifier){
            parent.resetView();
            return;
        }else if(e.keycode == Input.Keys.A && !e.shiftModifier && !e.ctrlModifier){
            SelectOperation o = clip.selectAll(parent);
            addOperation(o);
            return;
        }else if(e.keycode == Input.Keys.G && !e.shiftModifier && !e.ctrlModifier){
            System.out.print("Move the selected Objects");
            Object o = clip.getSelection().get(0);
            ModifyOperation move = new ModifyOperation(o);

            //addOperation();
            return;
        }
        parent.addInputEvent(e);
    }
    public void processKeyUpEvent(int keycode){

    }
    public void processKeyTypedEvent(char key){

    }
    public void processTouchDownEvent(int x, int y, int pointer, int button){
        if(button == Input.Buttons.LEFT && selectPointer<=0){
            System.out.print("Pre new selection Selection contains:"+clip.getSelection().size()+"\n");
            currentOperation = new SelectOperation(clip);
            currentOperation.setOperation(clip.getSelection(), clip.getTempSelection());
            selectPointer = pointer;
            selectTouch = parent.getWorldCoordinates(x,y);
            selectBox = new Rectangle(selectTouch.x-1, selectTouch.y-1, 2,2);
            clip.setTempSelection(parent.findSelected(selectBox, false));
        }else{
            selectPointer = -1;
        }

    }

    public void processTouchDraggedEvent(int x, int y, int pointer){
        if(pointer == selectPointer){
            Vector2 newPos = parent.getWorldCoordinates(x,y);
            float minx = Math.min(selectTouch.x, newPos.x);
            float miny = Math.min(selectTouch.y, newPos.y);
            float dx = Math.abs(newPos.x - selectTouch.x);
            float dy = Math.abs(newPos.y - selectTouch.y);
            selectBox = new Rectangle(minx, miny, dx, dy);
            clip.setTempSelection(parent.findSelected(selectBox, false));
        }

    }
    public void processTouchUpEvent(int x, int y, int pointer, int button){
        if(pointer == selectPointer){
            Vector2 newPos = parent.getWorldCoordinates(x,y);
            float minx = Math.min(selectTouch.x, newPos.x);
            float miny = Math.min(selectTouch.y, newPos.y);
            float dx = Math.abs(newPos.x - selectTouch.x);
            float dy = Math.abs(newPos.y - selectTouch.y);
            selectBox = new Rectangle(minx, miny, dx, dy);
            clip.setTempSelection(parent.findSelected(selectBox, false));
            selectBox = null;
            selectPointer = -1;
            selectTouch = new Vector2();
            /**
             * This is actually the place where a new Select operation should be pushed to the
             * operation stack, because while mid selection there is no point in creating a transformation
             * Either way this is only temporary selection for the moment
             */
            currentOperation.setOperation(currentOperation.getOriginal(), clip.getTempSelection());
            addOperation(currentOperation);
            clip.setTempSelection(new ArrayList<Object>(0));
        }
    }

    public Rectangle getSelectBox(){
        if(selectPointer==-1)return null;
        return selectBox;
    }

    public void processMouseMovedEvent(int x, int y){

    }
    public float processScrolledEvent(int amount){
        scroll+=scroll*amount/10;
        if(scroll<=1){
            scroll = 1; //to stop from ovezooming
        }
        return scroll;
    }
    public void resetScroll(){
        scroll = 10;
    }

    public void addOperation(Operation o){
        o.applyOperation();
        operations.add(o);
        System.out.print("Operation stack length:"+operations.size()+"\n");
    }
    public void removeOperation(){
        if(operations.size()==0){
            System.out.print("The operation stack is empty, nothing to un-do\n");
            return;
        }
        Operation o = operations.pop();
        o.undoOperation();
    }
}