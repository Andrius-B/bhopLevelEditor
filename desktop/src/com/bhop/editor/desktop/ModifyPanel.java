package com.bhop.editor.desktop;

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
}

class InputField extends Panel{
    public InputField(String labelText, String content){
        super();
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        Label labelComponent = new Label(labelText);
        this.add(labelComponent, c);

        TextField inputComponent = new TextField(content);
        inputComponent.setColumns(16);
        c.gridx = 1;
        //c.anchor = GridBagConstraints.LINE_END;
        this.add(inputComponent, c);
    }
}
