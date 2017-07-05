package com.bhop.editor.Util;

import com.bunny.jump.Game.Objects.Object;

/**
 * Created by Andrius on 7/5/2017.
 * This class resovles what properties can be modified
 * on an Object or an Object array
 */

public class ObjectPropertyResolver {
    public PropertyList resolveForObject(Object o){
        PropertyList p = new PropertyList();
        return p;
    }
}

/**
 * By default this object assumes that every possible field is on the object
 */
class PropertyList{
    /**
     * Object has position and dimension fields
     */
    public boolean positionFields = true;
    /**
     * Object has texture and repeat texture fields
     */
    public boolean textureFields = true;
    /**
     * Platforms have jumpSpeed field, which can be edited
     */
    public boolean jumpField = true;
    /**
     * Reset Fields for resetBoxes
     */
    public boolean resetFields = true;

}
