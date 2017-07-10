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
 * Event processor of the libgdx app
 * Also as noted before the canvas receives all the kbd input
 * so it is channeled up the chain back to the handler in LevelEditor class via parent reference
 * and the parent addInputEvent function
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
    private int clickSelectionCounter = 0;

    /**
     * Operation stack
     */
    private Stack<Operation> operations;


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
            ModifyOperation move = new ModifyOperation(clip);

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
            /**
             * a simple left click performs single object selection:
             * cycles through all the objects that are underneath the pointer
             */
            System.out.print("Pre new selection Selection contains:"+clip.getSelection().size()+"\n");
            currentOperation = new SelectOperation(clip);
            currentOperation.setOperation(clip.getSelection(), clip.getTempSelection());
            selectPointer = pointer;
            selectTouch = parent.getWorldCoordinates(x,y);
            selectBox = new Rectangle(selectTouch.x-1, selectTouch.y-1, 2,2);
            ArrayList<Object> selection = parent.findSelected(selectBox, false);
            clip.setTempSelection(selection);
            clip.setClickObjectSelection(selection);
        }else{
            selectPointer = -1;
        }

    }

    public void processTouchDraggedEvent(int x, int y, int pointer){
        if(pointer == selectPointer){
            /**
             * Selection pointer moved -> area measurement for box selection
             */
            Vector2 newPos = parent.getWorldCoordinates(x,y);
            float minx = Math.min(selectTouch.x, newPos.x);
            float miny = Math.min(selectTouch.y, newPos.y);
            float dx = Math.abs(newPos.x - selectTouch.x);
            float dy = Math.abs(newPos.y - selectTouch.y);
            selectBox = new Rectangle(minx, miny, dx, dy);
            clip.setTempSelection(parent.findSelected(selectBox, false));
            if(currentOperation instanceof SelectOperation && (dx*dx+dy*dy>=5*5)){ // if the dist is more than 5px then its a drag and not a click
                ((SelectOperation) currentOperation).setClick(false);
            }
        }

    }
    public void processTouchUpEvent(int x, int y, int pointer, int button){
        if(pointer == selectPointer && currentOperation instanceof SelectOperation){
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
             * This is the place where a new Select operation should be pushed to the
             * operation stack, because while mid selection there is no point in creating a transformation
             */
            if(!((SelectOperation) currentOperation).isClick()) {
                /**
                 * Box select
                 */
                currentOperation.setOperation(currentOperation.getOriginal(), clip.getTempSelection());
                addOperation(currentOperation);
                clip.setTempSelection(new ArrayList<Object>(0));
            }else{
                /**
                 * Click select
                 */
                if(clip.sameClickSelection(clip.getTempSelection())){ //I am not exactly sure why I wrote this check or what it does
                    ArrayList<Object> clickObject = new ArrayList<Object>(0);
                    if(clickSelectionCounter>=clip.getTempSelection().size()){

                        clickSelectionCounter = 0;
                        if(clip.getTempSelection().size()==0){
                            currentOperation.setOperation(currentOperation.getOriginal(), clickObject);
                            addOperation(currentOperation);
                            return;
                        }
                    }
                    clickObject.add(clip.getTempSelection().get(clickSelectionCounter));
                    clickSelectionCounter++;
                    currentOperation.setOperation(currentOperation.getOriginal(), clickObject);
                    addOperation(currentOperation);
                    clip.setTempSelection(new ArrayList<Object>(0));
                    System.out.print("Click select operation added:"+clickObject.get(0).getType().name()+" selected\n");
                }
            }
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
