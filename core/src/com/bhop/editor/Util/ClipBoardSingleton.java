package com.bhop.editor.Util;

/**
 * Created by Andrius on 7/20/2017.
 * Singleton wrapper for the ClipBoard,
 * Because it has to be global and available to all
 * Also thread safety ir necessary because ActionEventHandlers execute in a separate thread.
 */

 public class ClipBoardSingleton {
    private static ClipBoard instance;
    private ClipBoardSingleton(){}
    public static synchronized ClipBoard getInstance(){
        if(instance==null){
           instance = new ClipBoard();
        }
        return instance;
    }
}
