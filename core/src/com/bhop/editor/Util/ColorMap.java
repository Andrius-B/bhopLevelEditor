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
 */

public class ColorMap {
    public Color getBGColor(){
        return Color.valueOf("39333cff");
    }
    public Color getColor(Object o){
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
        if(o instanceof SkyBox)return Color.valueOf("5458ffff");
        else if(o instanceof Finish)return Color.valueOf("ff0d62ff");
        else if(o instanceof Start)return Color.valueOf("2aff00ff");
        else if(o instanceof ResetBox)return Color.valueOf("ac3397ff");
        else if(o instanceof VisualBlock)return Color.valueOf("ac7220ff");
        else if(o instanceof Platform)return Color.valueOf("2dac6cff");
        return Color.BLACK;
    }
    public Color getLoadingColor(){
        return Color.valueOf("d3d3d3ff");
    }
    public Color getSelectColor(){
        return Color.valueOf("d1d1d10e");
    }
}
