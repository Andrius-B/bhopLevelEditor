package com.bhop.editor.desktop;

import com.badlogic.gdx.math.Vector2;
import com.bunny.jump.Game.Objects.Object;
import com.bunny.jump.Game.Objects.SkyBox;
import com.bunny.jump.Game.Objects.Start;
import com.sun.javaws.Main;

import org.lwjgl.Sys;

import java.awt.Choice;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
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
 * SidePanel containing modify operation input fields
 */

class ModifyPanel extends Panel {
    private Panel objectModifyPanel;
    private GridBagConstraints mainGridBagConstraints;
    private MainFrameManager parent;
    private Frame parentFrame;
    public ModifyPanel(MainFrameManager parent){
        super();
        this.parent = parent;
        this.parentFrame = parent.frame;
        //this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setLayout(new GridBagLayout());
        mainGridBagConstraints = new GridBagConstraints();
        //gbc.anchor = GridBagConstraints.LINE_START;
        mainGridBagConstraints.gridy = 0;
        Label panelLabel = new Label("Modify");
        //panelLabel.setAlignment(Label.LEFT);
        this.add(panelLabel,mainGridBagConstraints);

        addNewInputField("hey", "love");
        addNewInputField("?", "up or perhaps some long content?");
        //setObject("Whatever", gbc);


    }

    /**
     *  Adds the input label and textfield pair to this(ModifyPanel parent Panel class)
     * @param labelText - Label text describing the input field
     * @param content - the starting content of the input field
     * @return Array list of components that contain both the label and the input field
     */
    public ArrayList<Component> addNewInputField(String labelText, String content){
        ArrayList<Component> inputRow = new ArrayList<Component>(0);
        mainGridBagConstraints.gridy+=1;
        mainGridBagConstraints.gridx = 0;
        mainGridBagConstraints.anchor = GridBagConstraints.LINE_START;
        Label labelComponent = new Label(labelText);
        this.add(labelComponent, mainGridBagConstraints);

        TextField inputComponent = new TextField(content);
        inputComponent.setColumns(16);
        inputComponent.setEnabled(false);
        mainGridBagConstraints.gridx = 1;
        mainGridBagConstraints.anchor = GridBagConstraints.LINE_END;
        this.add(inputComponent, mainGridBagConstraints);

        inputRow.add(labelComponent);
        inputRow.add(inputComponent);
        return inputRow;
    }

    /**
     *  A function that places the input to a given Panel container
     *  all inputs same as above
     * @param labelText - input field description
     * @param content - pre-edit content of the field
     * @param container - container to put these fields in
     * @return ArrayList of components that contain the label and the input field
     */
    public ArrayList<Component> addNewInputField(String labelText, String content, Panel container){

        ArrayList<Component> inputRow = new ArrayList<Component>(0);
        mainGridBagConstraints.gridy+=1;
        mainGridBagConstraints.gridx = 0;
        mainGridBagConstraints.anchor = GridBagConstraints.LINE_START;
        Label labelComponent = new Label(labelText);
        container.add(labelComponent, mainGridBagConstraints);

        TextField inputComponent = new TextField(content);
        inputComponent.setColumns(16);
        mainGridBagConstraints.gridx = 1;
        mainGridBagConstraints.anchor = GridBagConstraints.LINE_END;
        container.add(inputComponent, mainGridBagConstraints);

        inputRow.add(labelComponent);
        inputRow.add(inputComponent);
        return inputRow;
    }
    public Panel setObject(Object o){
        System.out.print("ModifyPanel.setObject called on:"+o.getType().name()+"!\n");
        if(objectModifyPanel!=null) {
            this.remove(objectModifyPanel);
            mainGridBagConstraints.gridy -= 1;
        }
        objectModifyPanel = new Panel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        Label objectTypeLabel = new Label("Editing: "+o.getType().name());
        Label infoLabel = new Label("This is a test label");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        objectModifyPanel.add(objectTypeLabel,gbc);
        gbc.gridy+=1;
        objectModifyPanel.add(infoLabel, gbc);

        mainGridBagConstraints.anchor = GridBagConstraints.LAST_LINE_START;
        mainGridBagConstraints.gridy += 1;
        mainGridBagConstraints.gridx = 0;
        this.add(objectModifyPanel, mainGridBagConstraints);
        parent.adjustCanvasSize();
        System.out.print("Panel added\n");
        return objectModifyPanel;
    }


}
