package com.bhop.editor.Util;

import com.bhop.editor.Operations.Operation;
import com.bhop.editor.Operations.SelectOperation;
import com.bhop.editor.Renderer;
import com.bunny.jump.Game.Objects.Object;

import java.util.ArrayList;

/**
 * Created by Andrius on 6/25/2017.
 */

public class ClipBoard {
    private ArrayList<Object> tempSelection;
    private ArrayList<Object> selection;
    private ArrayList<Object> clickObjectSelection;


    public ClipBoard(){
        tempSelection = new ArrayList<Object>(0);
        selection = new ArrayList<Object>(0);
        clickObjectSelection = new ArrayList<Object>(0);
    }



    public void setTempSelection(ArrayList<Object> selection){
        this.tempSelection = selection;
        /*System.out.print("Selection: ");
        for(Object o: selection){
            System.out.print(o.getType()+";");
        }
        System.out.print("\n");*/
    }

    public boolean isSelected(Object o){
        if(tempSelection.size()>0){
            return tempSelection.contains(o);
        }else{
            return selection.contains(o);
        }
    }

    /**
     * This is a blender-esque select all:
     * if something is already selected, deselect everything
     * otherwise select all
     */
    public SelectOperation selectAll(Renderer r){
        SelectOperation selectAll = new SelectOperation(this);
        ArrayList<Object> newSelection = new ArrayList<Object>();
        if(selection.size()>0){
            selectAll.setOperation(selection, newSelection);
        }else{
            for(Object o: r.getAllObjects()){
                newSelection.add(o);
            }
            selectAll.setOperation(selection, newSelection);
        }
        return selectAll;
    }

    public ArrayList<Object> getSelection(){
        return selection;
    }

    public void setSelection(ArrayList<Object> selection) {
        this.selection = selection;
    }

    public ArrayList<Object> getTempSelection(){
        return tempSelection;
    }

    public boolean sameClickSelection(ArrayList<Object> newSelection){
        /**
         * this function has some shortcircuits
         */
        if(newSelection.size() != clickObjectSelection.size())return false;
        for(int i = 0; i < newSelection.size(); i++){
            if(newSelection.get(i)!=clickObjectSelection.get(i))return false;
        }
        return true;
    }
    public void setClickObjectSelection(ArrayList<Object> select){
        this.clickObjectSelection = select;
    }
    public void clearClickObjectSelection(){
        this.clickObjectSelection.clear();
    }
}
