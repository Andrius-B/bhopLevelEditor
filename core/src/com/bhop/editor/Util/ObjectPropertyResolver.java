package com.bhop.editor.Util;

import com.bunny.jump.Game.Objects.Object;

import java.util.ArrayList;

/**
 * Created by Andrius on 7/5/2017.
 * This class resovles what properties can be modified
 * on an Object or an Object array
 */

public class ObjectPropertyResolver {
    static public PropertyList resolveForObject(Object o){
        PropertyList p = new PropertyList();
        /**
         * Resolved by this table:
         * Object       |pos;dim    |texture    | specific
         * ----------------------------------------------
         * Platform     |true       |true       |jumpField
         * SkyBox       |true       |optional   |
         * Start/Finish |true       |true       |
         * ResetBox     |true       |false      |resetFields
         * VisualBlock  |true       |true       |
         * ----------------------------------------------
         */
        if(o.getType() == Object.TYPE.PLATFORM){
            p.positionFields = true;
            p.textureFields = true;
            p.jumpField = true;
        }else if(o.getType() == Object.TYPE.SKYBOX){
            p.positionFields = true;
            p.textureFields = true;//may be not set, but should be enabled in the ui
        }else if(o.getType() == Object.TYPE.START || o.getType() == Object.TYPE.FINISH){
            p.positionFields = true;
            p.textureFields = true;
        }else if(o.getType() == Object.TYPE.RESETBOX){
            p.positionFields = true;
            p.textureFields = false;
            p.jumpField = false;
            p.resetFields = true;
        }else if(o.getType() == Object.TYPE.VISUALBLOCK){
            p.positionFields = true;
            p.textureFields = true;
            p.jumpField = false;
            p.resetFields = false;
        }
        return p;
    }
    static public PropertyList resolveForObjectArray(ArrayList<Object> objects){
        PropertyList p = new PropertyList();
        p.setAllTrue();
        /**
         * Resolved by the same table with a different approach
         */
        for(Object o: objects){
            if (o.getType() == Object.TYPE.PLATFORM) {
                //p.positionFields = true;
                //p.textureFields = true;
                //p.jumpField = true;
                p.resetFields = false;
            } else if (o.getType() == Object.TYPE.SKYBOX) {
                //p.positionFields = true;
                //p.textureFields = true;
                p.jumpField = false;
                p.resetFields = false;
            } else if (o.getType() == Object.TYPE.START || o.getType() == Object.TYPE.FINISH) {
                //p.positionFields = true;
                //p.textureFields = true;
                p.jumpField = false;
                p.resetFields = false;
            } else if (o.getType() == Object.TYPE.RESETBOX) {
                //p.positionFields = true;
                p.textureFields = false;
                p.jumpField = false;
                //p.resetFields = false;
            } else if (o.getType() == Object.TYPE.VISUALBLOCK) {
                //p.positionFields = true;
                //p.textureFields = true;
                p.jumpField = false;
                p.resetFields = false;
            }
        }
        return p;
    }
}
