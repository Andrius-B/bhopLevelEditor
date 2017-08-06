package com.bhop.editor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Queue;
import com.bhop.editor.Operations.Operation;
import com.bhop.editor.Util.AssetFileHandleResolver;
import com.bhop.editor.Util.ClipBoard;
import com.bhop.editor.Util.InputEvent;
import com.bunny.jump.Game.Objects.Object;
import com.bunny.jump.LevelManager;

import java.io.File;
import java.util.ArrayList;

public class LevelEditor extends ApplicationAdapter {
    private AssetFileHandleResolver resolver = new AssetFileHandleResolver();
    public Renderer r;
    private AssetManager manager;
    private LevelManager lvlmngr;
    /**
     * A flag to 'sync' event handling thread and the render thread
     * basically to load assets from the asset manager to the texture containers
     */
    private boolean levelChanged = false;
    private boolean doneLoading = false; // what manager.update() returns in the render loop
    private Queue<InputEvent> commands;
    private boolean created = false; //variable for keeping track of the create function

    /**
     * The operation stack defines each operation(Select, move modify)
     * in a stack and so it can trace back past operations for un-do
     */

    public LevelEditor(){
        commands = new Queue<InputEvent>();
    }

	@Override
	public void create () {
        manager = new AssetManager();
        lvlmngr = new LevelManager();
        /**
         * Most of the input is handled by the renderer, because here it is only supposed to affect
         * how the scene is rendered
         */
        r = new Renderer();//pass a reference of the clipboard to most of the objects, to it becomes sort of omni-present
        //even though this might not perfectly adhere to OOP methodology
        Gdx.input.setInputProcessor(r);
        created = true;
	}

    /**
     * Fetch the single object if a single object is selected
     * @return Object or null if not one selected
     */
    public Object getSingleSelection(){
        /**
         * The null checks are here, because this function is called from the EventPoller thread
         * and the renderer might not be initialized yet
         */
        if(r!=null && r.getSelection()!=null && r.getSelection().size()==1){
            ArrayList<Object> selection = r.getSelection();
            return selection.get(0);
        }else{
            return null;
        }
    }

	@Override
	public void render () {
        doneLoading = manager.update();
        r.setLoading(!doneLoading);
        if(doneLoading && levelChanged){

            System.out.print("Trying to set objects\n");
            r.setObjects(lvlmngr.fromJson(manager));
            levelChanged = false;
        }
        r.render();
	}

    public boolean isDoneLoading(){
        return doneLoading;
    }
	
	@Override
	public void dispose () {
		r.dispose();
	}

    public void changeRenderMethod(){
        r.changeRenderMethod();
    }

    public void setFile(File file){
        System.out.print("Disposing of AssetManager\n");
        manager = new AssetManager(resolver);

        lvlmngr.setFile(new FileHandle(file));

        System.out.print("New level file set\n");
        lvlmngr.queTextures(manager);
        System.out.print("Textures queued in Asset manager\n");
        levelChanged = true;
    }

    public InputEvent getCommand(){
        if(!this.created)return new InputEvent(-1);

        /**
         * Get events from the renderer and add them to the event queue
         */
        Queue<InputEvent> c = r.pollEvents();
        for(InputEvent s: c){
            commands.addLast(s);
        }
        /**
         * Return the first event to the application - handle it
         */
        //commands = new Queue<String>(1);
        if(commands.size>0){
            InputEvent currentCommand = commands.first();
            commands.removeFirst();
            return currentCommand;
        }
        return new InputEvent(-1);
    }

    public void setWorkingDir(String dir){
        resolver.setAssetDir(dir);
    }

    public void newFile(){
        System.out.print("New file handler\n");
    }
    public void saveFile(){
        System.out.print("Save to file handler\n");
        String json = lvlmngr.toJson(r.getAllObjects());
        //System.out.print("Here is some json:\n");
        //System.out.print(json);
        FileHandle level = lvlmngr.getFile();
        System.out.print("Saving json to file:"+level.path()+"\n");
        level.writeString(json, false);
    }
    public void exit(){
        System.out.print("Exit handler\n");
        System.exit(0);
    }
    public void cut(){
        System.out.print("Cut handler\n");
    }
    public void copy(){
        System.out.print("Copy handler\n");
    }
    public void paste(){
        System.out.print("Paste handler\n");
    }

    public void addOperation(Operation o){
        r.addOperation(o);
    }
    public void removeOperation(){
        r.removeOperation();
    }

    public void undo(){
        System.out.print("Undo handler\n");
        removeOperation();
    }
    public void duplicate(){}
    public void createNew(String type){

    }

}
