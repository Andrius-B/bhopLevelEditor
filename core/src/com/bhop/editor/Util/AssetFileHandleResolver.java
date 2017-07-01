package com.bhop.editor.Util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;
import java.io.IOException;

/**
 * Created by Andrius on 6/23/2017.
 */

public class AssetFileHandleResolver implements FileHandleResolver{
    String assetDir = "G://Programs/Android/bhop/android/assets/";
    @Override
    public FileHandle resolve(String fileName){
        FileHandle f = Gdx.files.absolute(assetDir+fileName);
        System.out.print("Resolving file to: "+assetDir+fileName+"\n");
        try {
            if (!f.exists()) {
                throw new Exception("File was not found at working dir:" + assetDir + " file name: " + fileName);
            }
        }catch (Exception e){
                e.printStackTrace();
        }
        return f;
    }

    public void setAssetDir(String dir){
        assetDir = dir;
    }
}
