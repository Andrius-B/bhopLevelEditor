package com.bhop.editor.Util;

import com.badlogic.gdx.graphics.Color;
import com.bunny.jump.Game.Objects.Finish;
import com.bunny.jump.Game.Objects.Object;
import com.bunny.jump.Game.Objects.Platform;
import com.bunny.jump.Game.Objects.ResetBox;
import com.bunny.jump.Game.Objects.SkyBox;
import com.bunny.jump.Game.Objects.Start;
import com.bunny.jump.Game.Objects.VisualBlock;
import com.bunny.jump.States.PlayState;

/**
 * Created by Andrius on 6/24/2017.
 * This class is pure utility - for containing the whole palette of the editor in one spot
 */

public class ColorMap {
    static public Color getBGColor(){
        return Color.valueOf("39333cff");
    }
    static public Color getColor(Object o){
        /**
         * Color-coding of objects
         * For now the colors are pretty much random
         *
         * Interesting note:
         * the order here matters, because say Start inherits Platform, so
         * Start is an instance of Platform
         * For this I have the getType() strings, though this seems to be working as well
         * and I suspect that those Strings are not the most efficient way of accomplishing this task
         */
        if(o.getType() == Object.TYPE.SKYBOX)return Color.valueOf("5458ffff");
        else if(o.getType() == Object.TYPE.FINISH)return Color.valueOf("ff0d62ff");
        else if(o.getType() == Object.TYPE.START)return Color.valueOf("2aff00ff");
        else if(o.getType() == Object.TYPE.RESETBOX)return Color.valueOf("ac3397ff");
        else if(o.getType() == Object.TYPE.VISUALBLOCK)return Color.valueOf("ac7220ff");
        else if(o.getType() == Object.TYPE.PLATFORM)return Color.valueOf("2dac6cff");
        return Color.BLACK;
    }
    static public Color getLoadingColor(){
        return Color.valueOf("d3d3d3ff");
    }
    static public Color getSelectColor(){
        return Color.valueOf("d1d1d10e");
    }
}
