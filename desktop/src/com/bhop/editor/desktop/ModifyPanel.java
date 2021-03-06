package com.bhop.editor.desktop;

import com.badlogic.gdx.math.Vector2;
import com.bhop.editor.LevelEditor;
import com.bhop.editor.Operations.ModifyOperation;
import com.bhop.editor.Util.ClipBoardSingleton;
import com.bhop.editor.Util.NumericTextField;
import com.bhop.editor.Util.ObjectPropertyResolver;
import com.bhop.editor.Util.PropertyList;
import com.bunny.jump.Game.Objects.Object;
import com.bunny.jump.Game.Objects.Platform;
import com.bunny.jump.Game.Objects.ResetBox;


import org.lwjgl.Sys;

import java.awt.Color;

import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
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

enum TextFields{
    xPosField, yPosField, xDimField, yDimField, jumpVelField,
    texturePathField, xResetPoint, yResetPoint
}

class ModifyPanel extends JPanel {
    private JPanel objectModifyPanel;
    private GridBagConstraints mainGridBagConstraints;
    public MainFrameManager parent;
    private Frame parentFrame;

    /**
     * The GUI elements used in the panel
     */
    public JLabel objectTypeLabel;

    public JLabel posLabel;
    public NumericTextField xPosField;
    public NumericTextField yPosField;

    public JLabel dimLabel;
    public NumericTextField xDimField;
    public NumericTextField yDimField;

    public JLabel jumpVelLabel;
    public NumericTextField jumpVelField;

    public JLabel texturePathLabel;
    public JTextField textureField;
    public JCheckBox repeatCheckbox;

    public JLabel resetPointLabel;
    public NumericTextField xResetPointField;
    public NumericTextField yResetPointField;
    public JCheckBox resetSpeedCheckbox;

    PanelListener listener;
    CheckBoxListener checkBoxListener;

    /**
     *
     * @param parent - the main frame manager to get the main frame
     */
    public ModifyPanel(MainFrameManager parent, Font font){
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
        listener = new PanelListener(this);
        checkBoxListener = new CheckBoxListener(this);
        arrangeModifyPanel(font);
        //setObject("Whatever", gbc);
    }

    /**
     * lays out the components, but does not set the data (leaves empty fields and labels)
     */
    private void arrangeModifyPanel(Font font){
        objectModifyPanel = new JPanel(new GridBagLayout());
        objectModifyPanel.setBackground(Color.LIGHT_GRAY);
        objectModifyPanel.setFont(font);
        GridBagConstraints gbc = new GridBagConstraints();

        /**
         * Adding fields
         *
         * Yes, the SLOC can be cut down here by using some helper functions, but
         * in my humble opinion that would require some cheeky reference sending
         * and I'm not up for it in the GUI sector
         */

        objectTypeLabel = new JLabel("");
        objectTypeLabel.setFont(font);
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
        posLabel = new JLabel("Position");
        posLabel.setFont(font);
        gbc.gridx = 0;
        objectModifyPanel.add(posLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        xPosField = new NumericTextField(6);
        xPosField.setFont(font);
        xPosField.getDocument().addDocumentListener(listener);
        xPosField.getDocument().putProperty("fieldName", TextFields.xPosField);
        objectModifyPanel.add(xPosField, gbc);
        gbc.gridx = 2;
        yPosField = new NumericTextField(6);
        yPosField.setFont(font);
        yPosField.getDocument().addDocumentListener(listener);
        yPosField.getDocument().putProperty("fieldName", TextFields.yPosField);
        objectModifyPanel.add(yPosField,gbc);

        /**
         * Dimension fields
         */
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_START;
        dimLabel = new JLabel("Size");
        dimLabel.setFont(font);
        gbc.gridx = 0;
        objectModifyPanel.add(dimLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        xDimField = new NumericTextField(6);
        xDimField.setFont(font);
        xDimField.getDocument().addDocumentListener(listener);
        xDimField.getDocument().putProperty("fieldName", TextFields.xDimField);
        objectModifyPanel.add(xDimField, gbc);
        gbc.gridx = 2;
        yDimField = new NumericTextField(6);
        yDimField.setFont(font);
        yDimField.getDocument().addDocumentListener(listener);
        yDimField.getDocument().putProperty("fieldName", TextFields.yDimField);
        objectModifyPanel.add(yDimField,gbc);

        /**
         * jumpVel field
         */
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_START;
        jumpVelLabel = new JLabel("Jump vel");
        jumpVelLabel.setFont(font);
        gbc.gridx = 0;
        objectModifyPanel.add(jumpVelLabel, gbc);
        jumpVelField = new NumericTextField(6);
        jumpVelField.setFont(font);
        jumpVelField.getDocument().addDocumentListener(listener);
        jumpVelField.getDocument().putProperty("fieldName", TextFields.jumpVelField);
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        objectModifyPanel.add(jumpVelField, gbc);

        gbc.gridy++;
        objectModifyPanel.add(new JSeparator(SwingConstants.HORIZONTAL));//doesn't do jack swift

        /**
         * Texture fields
         */
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_START;
        texturePathLabel = new JLabel("Texture");
        texturePathLabel.setFont(font);
        gbc.gridx = 0;
        objectModifyPanel.add(texturePathLabel, gbc);
        textureField = new JTextField(20);
        textureField.setFont(font);
        textureField.getDocument().addDocumentListener(listener);
        textureField.getDocument().putProperty("fieldName", TextFields.texturePathField);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_END;
        objectModifyPanel.add(textureField, gbc);
        gbc.gridwidth = 1;

        gbc.gridy++;
        repeatCheckbox = new JCheckBox("Repeat texture", false);
        repeatCheckbox.setActionCommand("repeat");
        repeatCheckbox.addActionListener(checkBoxListener);
        repeatCheckbox.setFont(font);
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
        resetPointLabel = new JLabel("ResetPoint");
        resetPointLabel.setFont(font);
        gbc.gridx = 0;
        objectModifyPanel.add(resetPointLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        xResetPointField = new NumericTextField(6);
        xResetPointField.setFont(font);
        xResetPointField.getDocument().addDocumentListener(listener);
        xResetPointField.getDocument().putProperty("fieldName", TextFields.xResetPoint);
        objectModifyPanel.add(xResetPointField, gbc);
        gbc.gridx = 2;
        yResetPointField = new NumericTextField(6);
        yResetPointField.setFont(font);
        yResetPointField.getDocument().addDocumentListener(listener);
        yResetPointField.getDocument().putProperty("fieldName", TextFields.yResetPoint);
        objectModifyPanel.add(yResetPointField,gbc);

        gbc.gridy++;
        resetSpeedCheckbox = new JCheckBox("Reset Speed", false);
        resetSpeedCheckbox.setActionCommand("resetSpeed");
        resetSpeedCheckbox.addActionListener(checkBoxListener);
        resetSpeedCheckbox.setFont(font);
        resetSpeedCheckbox.setBackground(Color.LIGHT_GRAY);
        gbc.gridwidth = 3;
        gbc.gridx = 0;
        objectModifyPanel.add(resetSpeedCheckbox, gbc);
        gbc.gridwidth = 1;


        mainGridBagConstraints.gridy++;
        this.add(objectModifyPanel, mainGridBagConstraints);
    }

    public void updatePanel(Object o){
        if(o==null)return;
        this.setObject(o);
    }

    public JPanel setObjectArray(ArrayList<Object> o){
        //System.out.print("ModifyPanel.setObject called on:"+o.getType().name()+"!\n");
        /*
         * dereference the object in listener, so the change events can be easily ignored
         */

        listener.setListening(false);
        PropertyList p = ObjectPropertyResolver.resolveForObjectArray(o);

        /*
         * Fetching information from generic types
         */
        Vector2 pos = new Vector2(0,0);
        Vector2 dim = new Vector2(100,100);
        String texturePath = "";
        boolean repeatTexture = false;

        /*
         * Fetching information from specific types
         * For this object's type is checked and then safely cast to
         * less abstract type
         */
        Vector2 resetPoint = new Vector2();
        boolean resetSpeed = false;
        float jumpVel = 0f;


        objectTypeLabel.setText("Multiple Objects selected");

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

        /**
         * pass the listener a reference to the new object for modifications
         */
        listener.setListening(true);
        setVisible(true);
        return objectModifyPanel;
    }

    public JPanel setObject(Object o){
        System.out.print("ModifyPanel.setObject called on:"+o.getType().name()+"!\n");
        /**
         * dereference the object in listener, so the change events can be easily ignored
         */

        listener.setListening(false);
        PropertyList p = ObjectPropertyResolver.resolveForObject(o);

        /**
         * Fetching information from generic types
         */
        Vector2 pos = o.getPosition();
        Vector2 dim = o.getDimension();
        String texturePath = o.filepath;
        boolean repeatTexture = o.repeat;

        /**
         * Fetching information from specific types
         * For this object's type is checked and then safely cast to
         * less abstract type
         */
        Vector2 resetPoint = new Vector2();
        boolean resetSpeed = false;
        if(p.resetFields){
            resetPoint = ((ResetBox)o).getResetPoint();
            resetSpeed = ((ResetBox)o).getRemoveSpeed();
        }
        float jumpVel = 0f;
        if(p.jumpField){
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

        /**
         * pass the listener a reference to the new object for modifications
         */
        listener.setListening(true);
        setVisible(true);
        return objectModifyPanel;
    }

}

class PanelListener implements DocumentListener{
    private Object o;
    private ModifyPanel parent;
    private LevelEditor lvlEditor;
    private boolean listening = false;
    public PanelListener(ModifyPanel parent){
        this.parent = parent;
        this.lvlEditor = parent.parent.lvlEditor;
    }

    public void setListening(boolean listening){
        this.listening = listening;
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

    private void common(DocumentEvent e) {
        Document source = e.getDocument();
        String contents = "";
        try {
            contents = source.getText(0, source.getLength());
        } catch (BadLocationException e1) {
            System.out.print("Bad location Exception thrown in the DocumentListener handling methods of ModifyPanel\n");
        }
        TextFields fieldName = (TextFields) source.getProperty("fieldName");
        ArrayList<Object> selected = ClipBoardSingleton.getInstance().getSelection();
        if (fieldName == null) {
            System.out.print("fieldName not found in the edited textfield!\n");
            return;
        }

        if (listening) {
            if (selected.size() == 1) {
                System.out.print("Single object edit!\n");
                for (Object o : selected) {
                    ModifyOperation op = new ModifyOperation(selected);
                    System.out.print("Textfield changed: " + contents + " | " + fieldName + "\n");


                    /*
                        Each field has its own handler, both for single and multiple modify operations
                     */
                    if (fieldName == TextFields.xPosField) {
                        System.out.print("pos x changed on " + o.getType().name() + "\n");
                        Vector2 pos = o.getPosition();
                        pos.x = parent.xPosField.getContent();
                        op.setMove(pos.sub(o.getPosition()));
                        lvlEditor.addOperation(op);
                    } else if (fieldName == TextFields.yPosField) {
                        System.out.print("pos y changed on " + o.getType().name() + "\n");
                        Vector2 pos = o.getPosition();
                        pos.y = parent.yPosField.getContent();
                        op.setMove(pos.sub(o.getPosition()));
                        lvlEditor.addOperation(op);
                    } else if (fieldName == TextFields.xDimField) {
                        System.out.print("dim x changed on " + o.getType().name() + "\n");
                        Vector2 dim = o.getDimension();
                        dim.x = parent.xDimField.getContent();
                        op.setAbsoluteSize(dim);
                        lvlEditor.addOperation(op);
                    } else if (fieldName == TextFields.yDimField) {
                        System.out.print("dim y changed on " + o.getType().name() + "\n");
                        Vector2 dim = o.getDimension();
                        dim.y = parent.yDimField.getContent();
                        op.setAbsoluteSize(dim);
                        lvlEditor.addOperation(op);
                    } else if (fieldName == TextFields.jumpVelField) {
                        System.out.print("jumpVel changed on " + o.getType().name() + "\n");
                        op.setJump(parent.jumpVelField.getContent());
                        lvlEditor.addOperation(op);
                    } else if (fieldName == TextFields.texturePathField) {
                        System.out.print("textureFiled changed on " + o.getType().name() + "\n");
                        op.setTexture(parent.textureField.getText());
                        lvlEditor.addOperation(op);
                    } else if (fieldName == TextFields.xResetPoint) {
                        System.out.print("resetPoint x changed on " + o.getType().name() + "\n");
                        Vector2 rs = ((ResetBox)o).getResetPoint();
                        rs.x = parent.xResetPointField.getContent();
                        op.setResetPoint(rs);
                        lvlEditor.addOperation(op);
                    } else if (fieldName == TextFields.yResetPoint) {
                        System.out.print("resetPoint y changed on " + o.getType().name() + "\n");
                        Vector2 rs = ((ResetBox)o).getResetPoint();
                        rs.y = parent.yResetPointField.getContent();
                        op.setResetPoint(rs);
                        lvlEditor.addOperation(op);
                    }
                }
            }/* else if (selected.size() > 1) {
                System.out.print("Multiple object edit:\n");
                for(Object o: selected){
                    System.out.print(o.getType().name()+"\n");
                }
            }*/
        }
    }
}

class CheckBoxListener implements ActionListener{
    LevelEditor lvlEditor;
    ModifyPanel parent;
    protected CheckBoxListener(ModifyPanel parent){
        this.parent = parent;
        this.lvlEditor = parent.parent.lvlEditor;
    }
    public void actionPerformed(ActionEvent actionEvent) {
        AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
        boolean selected = abstractButton.getModel().isSelected();
        ModifyOperation op = new ModifyOperation(ClipBoardSingleton.getInstance().getSelection());
        if(actionEvent.getActionCommand().equals("repeat")){
            System.out.print("Repeat checkbox checked: "+Boolean.toString(selected)+"\n");
            op.setRepeatTexture(selected);
            lvlEditor.addOperation(op);
        }else if(actionEvent.getActionCommand().equals("resetSpeed")){
            System.out.print("resetSpeed checkbox checked: "+Boolean.toString(selected)+"\n");
            op.setResetSpeed(selected);
            lvlEditor.addOperation(op);
        }
    }
};
