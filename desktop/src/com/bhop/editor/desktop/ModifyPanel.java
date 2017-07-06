package com.bhop.editor.desktop;

import com.badlogic.gdx.math.Vector2;
import com.bhop.editor.Util.NumericTextField;
import com.bhop.editor.Util.ObjectPropertyResolver;
import com.bhop.editor.Util.PropertyList;
import com.bunny.jump.Game.Objects.Object;
import com.bunny.jump.Game.Objects.Platform;
import com.bunny.jump.Game.Objects.ResetBox;
import com.bunny.jump.Game.Objects.SkyBox;
import com.bunny.jump.Game.Objects.Start;
import com.bunny.jump.Game.Objects.VisualBlock;
import com.sun.javaws.Main;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import org.lwjgl.Sys;

import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * Created by Andrius on 6/30/2017.
 * SidePanel containing modify operation input fields
 */

class ModifyPanel extends JPanel {
    private JPanel objectModifyPanel;
    private GridBagConstraints mainGridBagConstraints;
    private MainFrameManager parent;
    private Frame parentFrame;

    /**
     * The GUI elements used in the panel
     */
    Label objectTypeLabel;

    Label posLabel;
    NumericTextField xPosField;
    NumericTextField yPosField;

    Label dimLabel;
    NumericTextField xDimField;
    NumericTextField yDimField;

    Label jumpVelLabel;
    NumericTextField jumpVelField;

    Label texturePathLabel;
    JTextField textureField;
    JCheckBox repeatCheckbox;

    Label resetPointLabel;
    NumericTextField xResetPointField;
    NumericTextField yResetPointField;
    JCheckBox resetSpeedCheckbox;

    PanelListener listener;

    /**
     *
     * @param parent - the main frame manager to get the main frame
     */
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
        listener = new PanelListener();
        arrangeModifyPanel();
        //setObject("Whatever", gbc);
    }

    /**
     * lays out the components, but does not set the data (leaves empty fields and labels)
     */
    private void arrangeModifyPanel(){
        objectModifyPanel = new JPanel(new GridBagLayout());
        objectModifyPanel.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();

        /**
         * Adding fields
         *
         * Yes, the SLOC can be cut down here by using some helper functions, but
         * in my humble opinion that would require some cheeky reference sending
         * and I'm not up for it in the GUI sector
         */

        objectTypeLabel = new Label("");
        gbc.gridwidth = 3;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        objectModifyPanel.add(objectTypeLabel,gbc);
        gbc.gridwidth = 1;


        /**
         * Position fields
         */
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_START;
        posLabel = new Label("Position");
        gbc.gridx = 0;
        objectModifyPanel.add(posLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        xPosField = new NumericTextField(6);
        xPosField.getDocument().addDocumentListener(listener);
        objectModifyPanel.add(xPosField, gbc);
        gbc.gridx = 2;
        yPosField = new NumericTextField(6);
        yPosField.getDocument().addDocumentListener(listener);
        objectModifyPanel.add(yPosField,gbc);

        /**
         * Dimension fields
         */
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_START;
        dimLabel = new Label("Size");
        gbc.gridx = 0;
        objectModifyPanel.add(dimLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        xDimField = new NumericTextField(6);
        xDimField.getDocument().addDocumentListener(listener);
        objectModifyPanel.add(xDimField, gbc);
        gbc.gridx = 2;
        yDimField = new NumericTextField(6);
        yDimField.getDocument().addDocumentListener(listener);
        objectModifyPanel.add(yDimField,gbc);

        /**
         * jumpVel field
         */
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_START;
        jumpVelLabel = new Label("Jump vel");
        gbc.gridx = 0;
        objectModifyPanel.add(jumpVelLabel, gbc);
        jumpVelField = new NumericTextField(6);
        jumpVelField.getDocument().addDocumentListener(listener);
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        objectModifyPanel.add(jumpVelField, gbc);

        gbc.gridy++;
        objectModifyPanel.add(new JSeparator(SwingConstants.HORIZONTAL));//doesnt do jack swift

        /**
         * Texture fields
         */
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_START;
        texturePathLabel = new Label("Texture");
        gbc.gridx = 0;
        objectModifyPanel.add(texturePathLabel, gbc);
        textureField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_END;
        objectModifyPanel.add(textureField, gbc);
        gbc.gridwidth = 1;

        gbc.gridy++;
        repeatCheckbox = new JCheckBox("Repeat texture", false);
        repeatCheckbox.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        objectModifyPanel.add(repeatCheckbox, gbc);
        gbc.gridwidth = 1;

        /**
         * Reset box fields
         */
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_START;
        resetPointLabel = new Label("ResetPoint");
        gbc.gridx = 0;
        objectModifyPanel.add(resetPointLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        xResetPointField = new NumericTextField(6);
        objectModifyPanel.add(xResetPointField, gbc);
        gbc.gridx = 2;
        yResetPointField = new NumericTextField(6);
        objectModifyPanel.add(yResetPointField,gbc);

        gbc.gridy++;
        resetSpeedCheckbox = new JCheckBox("Reset Speed", false);
        resetSpeedCheckbox.setBackground(Color.LIGHT_GRAY);
        gbc.gridwidth = 3;
        gbc.gridx = 0;
        objectModifyPanel.add(resetSpeedCheckbox, gbc);
        gbc.gridwidth = 1;


        mainGridBagConstraints.gridy++;
        this.add(objectModifyPanel, mainGridBagConstraints);
    }


    public JPanel setObject(Object o){
        System.out.print("ModifyPanel.setObject called on:"+o.getType().name()+"!\n");

        PropertyList p = ObjectPropertyResolver.resolveForObject(o);

        /**
         * Fetching information from generic types
         */
        Vector2 pos = o.getPosition();
        Vector2 dim = o.getDimensions();
        String texturePath = o.filepath;
        boolean repeatTexture = o.repeat;

        /**
         * Fetching information from specific types
         * For this object's type is checked and then safely cast to
         * less abstract type
         */
        Vector2 resetPoint = new Vector2();
        boolean resetSpeed = false;
        if(o.getType() == Object.TYPE.RESETBOX){
            resetPoint = ((ResetBox)o).getResetPoint();
            resetSpeed = ((ResetBox)o).getRemoveSpeed();
        }
        float jumpVel = 0f;
        if(o.getType() == Object.TYPE.PLATFORM){
            jumpVel = ((Platform) o).jumpVel;
        }

        /**
         * The type enum is all caps, doesn't look nice
         */
        String lower = o.getType().name().substring(1).toLowerCase();
        objectTypeLabel.setText(o.getType().name().substring(0,1)+lower);

        if(p.positionFields){
            posLabel.setEnabled(true);
            xPosField.setEnabled(true);
            xPosField.setText(Float.toString(pos.x));
            yPosField.setEnabled(true);
            yPosField.setText(Float.toString(pos.y));

            dimLabel.setEnabled(true);
            xDimField.setEnabled(true);
            xDimField.setText(Float.toString(dim.x));
            yDimField.setEnabled(true);
            yDimField.setText(Float.toString(dim.y));
        }else{
            posLabel.setEnabled(false);
            xPosField.setEnabled(false);
            xPosField.setText(Float.toString(pos.x));
            yPosField.setEnabled(false);
            yPosField.setText(Float.toString(pos.y));

            dimLabel.setEnabled(false);
            xDimField.setEnabled(false);
            xDimField.setText(Float.toString(dim.x));
            yDimField.setEnabled(false);
            yDimField.setText(Float.toString(dim.y));
        }

        if(p.textureFields){
            texturePathLabel.setEnabled(true);
            textureField.setEnabled(true);
            textureField.setText(texturePath);

            repeatCheckbox.setEnabled(true);
            repeatCheckbox.setSelected(repeatTexture);
        }else{
            texturePathLabel.setEnabled(false);
            textureField.setEnabled(false);
            textureField.setText(texturePath);

            repeatCheckbox.setEnabled(false);
            repeatCheckbox.setSelected(repeatTexture);
        }

        if(p.jumpField){
            jumpVelLabel.setEnabled(true);
            jumpVelField.setEnabled(true);
            jumpVelField.setText(Float.toString(jumpVel));
        }else{
            jumpVelLabel.setEnabled(false);
            jumpVelField.setEnabled(false);
        }

        if(p.resetFields){
            resetPointLabel.setEnabled(true);
            xResetPointField.setEnabled(true);
            xResetPointField.setText(Float.toString(resetPoint.x));
            yResetPointField.setEnabled(true);
            yResetPointField.setText(Float.toString(resetPoint.y));

            resetSpeedCheckbox.setEnabled(true);
            resetSpeedCheckbox.setSelected(resetSpeed);
        }else{
            resetPointLabel.setEnabled(false);
            xResetPointField.setEnabled(false);
            xResetPointField.setText(Float.toString(resetPoint.x));
            yResetPointField.setEnabled(false);
            yResetPointField.setText(Float.toString(resetPoint.y));

            resetSpeedCheckbox.setEnabled(false);
            resetSpeedCheckbox.setSelected(resetSpeed);
        }

        objectModifyPanel.doLayout();
        return objectModifyPanel;
    }

}

class PanelListener implements DocumentListener{
    Object o;
    public void setObject(Object o){
        this.o = o;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        common(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        common(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        common(e);
    }

    private void common(DocumentEvent e){
        Document source = e.getDocument();
        String contents = "";
        try{
            contents = source.getText(0, source.getLength());
        }catch (BadLocationException e1){
            System.out.print("Bad location Exception thrown in the DocumentListener handling methods of ModifyPanel\n");
        }
        System.out.print(contents+" | "+e.getType()+"\n");
    }
}
