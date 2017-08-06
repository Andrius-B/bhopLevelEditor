package com.bhop.editor.Util;

/**
 * By default this object assumes that every field is on the object except the specific
 */
public class PropertyList{
    /**
     * Object has position and dimension fields
     */
    public boolean positionFields = true;
    /**
     * Object has texture and repeat texture fields
     */
    public boolean textureFields = true;

    /**
     * Platform specific
     * Platforms have jumpSpeed field, which can be edited
     */
    public boolean jumpField = false;
    /**
     * ResetBox specific
     * Reset Fields for resetBoxes
     */
    public boolean resetFields = false;

    /**
     * Sets all the fields to true
     */
    public void setAllTrue(){
        this.jumpField = true;
        this.positionFields = true;
        this.resetFields = true;
        this.textureFields = true;
    }
}