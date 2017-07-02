package com.bhop.editor.desktop;

import com.bunny.jump.Game.Objects.Object;

import org.lwjgl.Sys;

import java.awt.Choice;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.swing.BoxLayout;

/**
 * Created by Andrius on 6/30/2017.
 */

public class ModifyPanel extends Panel {
    Panel objectModifyPanel;
    public ModifyPanel(){
        super();
        //this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        //gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridy = 0;
        Label panelLabel = new Label("Modify");
        //panelLabel.setAlignment(Label.LEFT);
        this.add(panelLabel,gbc);

        addNewInputField("hey", "love", gbc);
        addNewInputField("?", "up or perhaps some long content?", gbc);


    }

    /**
     *  Adds the input label and textfield pair to this(ModifyPanel parent Panel class)
     * @param labelText - Label text describing the input field
     * @param content - the starting content of the input field
     * @param c - GridBagConstraints for object insertion
     * @return Array list of components that contain both the label and the input field
     */
    public ArrayList<Component> addNewInputField(String labelText, String content, GridBagConstraints c){
        ArrayList<Component> inputRow = new ArrayList<Component>(0);
        c.gridy+=1;
        c.gridx = 0;
        c.anchor = GridBagConstraints.LINE_START;
        Label labelComponent = new Label(labelText);
        this.add(labelComponent, c);

        TextField inputComponent = new TextField(content);
        inputComponent.setColumns(16);
        c.gridx = 1;
        c.anchor = GridBagConstraints.LINE_END;
        this.add(inputComponent, c);

        inputRow.add(labelComponent);
        inputRow.add(inputComponent);
        return inputRow;
    }

    /**
     *  A function that places the input to a given Panel container
     *  all inputs same as above
     * @param labelText
     * @param content
     * @param c
     * @param container
     * @return ArrayList of components that contain the label and the input field
     */
    public ArrayList<Component> addNewInputField(String labelText, String content, GridBagConstraints c, Panel container){

        ArrayList<Component> inputRow = new ArrayList<Component>(0);
        c.gridy+=1;
        c.gridx = 0;
        c.anchor = GridBagConstraints.LINE_START;
        Label labelComponent = new Label(labelText);
        container.add(labelComponent, c);

        TextField inputComponent = new TextField(content);
        inputComponent.setColumns(16);
        c.gridx = 1;
        c.anchor = GridBagConstraints.LINE_END;
        container.add(inputComponent, c);

        inputRow.add(labelComponent);
        inputRow.add(inputComponent);
        return inputRow;
    }
    public Panel setObject(Object o){
        System.out.print("ModifyPanel.setObject called on:"+o.getType()+"!\n");
        if(objectModifyPanel!=null) {
            this.remove(objectModifyPanel);
        }
        Panel objPanel = new Panel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        Label objectTypeLabel = new Label(o.getType());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        objPanel.add(objectTypeLabel, gbc);
        objectModifyPanel = objPanel;
        this.add(objectModifyPanel);
        return objPanel;
    }


}
