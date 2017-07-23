package com.bhop.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Queue;
import com.bhop.editor.Operations.Operation;
import com.bhop.editor.Util.ClipBoard;
import com.bhop.editor.Util.ClipBoardSingleton;
import com.bhop.editor.Util.ColorMap;
import com.bhop.editor.Util.InputEvent;
import com.bhop.editor.Util.InputEventProcessor;
import com.bunny.jump.Game.Objects.Object;
import com.bunny.jump.Game.Objects.ResetBox;
import com.bunny.jump.Game.Objects.SkyBox;

import java.util.ArrayList;

/**
 * Created by Andrius on 6/23/2017.
 * Main renderer of libgdx editing app
 */

public class Renderer implements InputProcessor {
    /**
     * The level - a list of all objects in it
     */
    private ArrayList<Object> obj;

    /**
     * Rendering objects
     */
    private OrthographicCamera cam;
    private SpriteBatch batch;
    private ShapeRenderer sr;
    public boolean textured = false;
    /**
     * Local rendering variables
     */
    private float scroll;
    private Vector2 offset;
    private ColorMap clrs;

    /**
     * If the asset manager is still loading information, we should render some loading information thingy
     */
    private boolean loading;
    private float rotation = 0;

    /**
     * Variables for containing information about user input
     * All input except for viewport navigation is handled in the InputEventProcessor class
     *
     * This splitting is because I have already implemented nav here
     */
    private Vector3 lastMMPos; // last noted midle mouse position * the third var used for pointer

    /**
     * This string queue is used for event handling - specifically passing them to the parent object
     * The Canvas for this LWJGL app seems to be hoarding all of the input events and since I cant figure out why
     * that is, this is a workaround
     *
     * This queue is filled upon input events and emptied in the pollEvents function
     */
    private Queue<InputEvent> commands;

    private ClipBoard clip;

    public InputEventProcessor inputEventProcessor;

    public Renderer(){
        obj = new ArrayList<Object>();
        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();
        sr = new ShapeRenderer();

        resetView();
        clrs = new ColorMap();

        lastMMPos = new Vector3();
        commands = new Queue<InputEvent>();
        this.clip = ClipBoardSingleton.getInstance();
        inputEventProcessor = new InputEventProcessor(this);
    }

    public void setObjects(ArrayList<Object> o){
        this.obj = o;
        System.out.print("/////////////////////////////\nLEVEL LOADED\n/////////////////////////////////////\n");
        resetView();
    }

    public void setLoading(boolean l){
        loading = l;
    }

    public void addOperation(Operation o){
        inputEventProcessor.addOperation(o);
    }
    public void removeOperation(){
        inputEventProcessor.removeOperation();
    }

    public void render(){
        Gdx.gl.glClearColor(0.13725490196078431372549019607843f,
                0.25490196078431372549019607843137f,
                0.42745098039215686274509803921569f,
                0.f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        /**
         * The scroll transforms the window like so:
         * scroll^2/100 of original screen size
         */
        cam = new OrthographicCamera(w*scroll*scroll/100, h*scroll*scroll/100);
        cam.position.set(offset.x, offset.y, 0);
        cam.update();

        //System.out.print("Render loop\n");
        if(textured) {
            /**
             * renders everything just like in the game - textures and all
             */
            batch.setProjectionMatrix(cam.combined);
            batch.begin();
            for (Object o : obj) {
                o.render(batch);
                if (o instanceof SkyBox) {
                    //System.out.print("Rendering: "+o.getType()+"\n");
                    /**
                     * for paralax, skyboxes must be rendered with a different method
                     */
                    ((SkyBox) o).render(batch, offset.x / 10, offset.y / 10);
                }
            }
            batch.end();
        }else{
            /**
             * shape renderer, some times it's nice to have non textured view
             */

            Gdx.gl.glClearColor(ColorMap.getBGColor().r, ColorMap.getBGColor().g, ColorMap.getBGColor().b, ColorMap.getBGColor().a);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            sr.setProjectionMatrix(cam.combined);
            sr.begin(ShapeRenderer.ShapeType.Line);
            for (Object o : obj) {
                sr.setColor(ColorMap.getColor(o));
                Rectangle r = o.getBoundBox();
                if(inputEventProcessor.clip.isSelected(o)){
                    sr.end();
                    /**
                     * All this work just to highlight what is selected
                     */
                    sr.begin(ShapeRenderer.ShapeType.Filled);
                    Color selected = ColorMap.getColor(o);
                    selected.a = 0.7f;
                    sr.setColor(selected);
                    drawThickRect(r.x, r.y, r.getWidth(), r.getHeight(), sr, scroll*scroll/25f);
                    sr.end();
                    sr.begin(ShapeRenderer.ShapeType.Line);
                }
                sr.rect(r.x, r.y, r.getWidth(), r.getHeight());
                if(o instanceof ResetBox){
                    Vector2 p = ((ResetBox) o).getResetPoint();
                    sr.x(p.x, p.y, 10);
                }
            }
            sr.end();

            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        sr.setProjectionMatrix(cam.combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(ColorMap.getSelectColor());
        Rectangle r = inputEventProcessor.getSelectBox();
        if(r!=null){
            sr.rect(r.x, r.y, r.width, r.height);
        }
        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        if(loading){
            cam = new OrthographicCamera(w, h);
            cam.update();
            sr.setProjectionMatrix(cam.combined);
            sr.begin(ShapeRenderer.ShapeType.Line);
                sr.setColor(ColorMap.getLoadingColor());
                sr.ellipse(-w/2+w*1/100, h/2-w*2/100, w*1/100, w*1.5f/100, rotation, 64);
                rotation-=5;
            sr.end();
        }
    }

    public void dispose(){
        batch.dispose();
        sr.dispose();
    }

    public ArrayList<Object> getSelection(){
        return clip.getSelection();
    }

    public void changeRenderMethod(){
        textured = !textured;
    }

    public void resetView(){
        if(inputEventProcessor!=null){
            inputEventProcessor.resetScroll();
        }
        scroll = 10;
        offset = new Vector2(0, 0);
    }

    public void addInputEvent(InputEvent event){
        commands.addLast(event);
    }

    public Queue<InputEvent> pollEvents(){
        //System.out.print("Renderer polled for events\n");
        Queue<InputEvent> co = new Queue<InputEvent>();
        for(InputEvent s: commands){
            co.addLast(s);
        }
        commands.clear();
        return co;
    }

    private void drawThickRect(float x, float y, float dx, float dy, ShapeRenderer sr, float THICC){

        /**
         * Draws a thick lined rect using ShapeRenderers rectLine method, thus requires
         *       ___(2)____
         *      |..........|
         *   (1)|..........|(3)
         *      |..........|
         *      |__________|
         *          (4)
         */
        sr.rectLine(x, y, x, y+dy, THICC);//(1)
        sr.rectLine(x, y+dy, x+dx, y+dy, THICC);//(2)
        sr.rectLine(x+dx, y+dy, x+dx, y, THICC);//(3)
        sr.rectLine(x+dx, y, x, y, THICC);//(4)
    }

    public Vector2 getViewport(){
        cam.update();
        return new Vector2(cam.viewportWidth, cam.viewportHeight);
    }

    public Vector2 getWorldCoordinates(float x, float y){
        /**
         * The parameters of this function are in screen coordinates and this
         * function transforms them into world coordinates
         */
        //System.out.print("Converting screen coordinates to world:("+x+";"+y+")\n");
        /**
         * Conversion to noramlized camera space
         */
        float centeredx = (x - Gdx.graphics.getWidth()/2)/Gdx.graphics.getWidth();
        float centeredy = -(y - Gdx.graphics.getHeight()/2)/Gdx.graphics.getHeight();
        //System.out.print("Centered coordinates:("+centeredx+";"+centeredy+")\n");

        /**
         * Convert camera space to world space
         */
        float worldx = cam.position.x + centeredx*getViewport().x;
        float worldy = cam.position.y + centeredy*getViewport().y;
        //System.out.print("World coordinates:("+worldx+";"+worldy+")\n");
        return new Vector2(worldx, worldy);
    }

    @Override
    public boolean keyDown (int keycode) {
        inputEventProcessor.processKeyDownEvent(keycode);
        return false;
    }

    @Override
    public boolean keyUp (int keycode) {
        inputEventProcessor.processKeyUpEvent(keycode);
        return true;
    }

    @Override
    public boolean keyTyped (char character) {
        inputEventProcessor.processKeyTypedEvent(character);
        return false;
    }

    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {
        inputEventProcessor.processTouchDownEvent(x, y, pointer, button);
        if(button == Input.Buttons.MIDDLE){
            lastMMPos = new Vector3(x,y,pointer);
        }else{
            lastMMPos = new Vector3(lastMMPos.x ,lastMMPos.y, -1);
        }
        return true;
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
        inputEventProcessor.processTouchUpEvent(x, y, pointer, button);
        if(pointer == lastMMPos.z){
            lastMMPos = new Vector3(x,y,pointer);
        }
        return true;
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer) {
        inputEventProcessor.processTouchDraggedEvent(x, y, pointer);
        if(pointer == lastMMPos.z) {
            /**
             * First step to continiuos dragging is to get the delta position of the mouse over the last tick
             * for that a variable(lastMMPos) is used, also it knows which pointer was used for middle click
             *
             * secondly we normalize the amount dragged - make it a fraction of screen size
             * and multiply by our transformed screen size i.e. unproject the drag
             */
            float normX = (x - lastMMPos.x) / Gdx.graphics.getWidth();
            float normY = (y - lastMMPos.y) / Gdx.graphics.getHeight();
            lastMMPos = new Vector3(x,y,pointer);
            offset.add(-normX*cam.viewportWidth, normY*cam.viewportHeight);
        }
        return true;
    }

    public ArrayList<Object> findSelected(Rectangle selectBox, boolean selectSkyboxes){
        ArrayList<Object> selected = new ArrayList<Object>();
        for(Object o: obj){
            if(o.getBoundBox().overlaps(selectBox)){
                if(o.getType() == Object.TYPE.SKYBOX && selectSkyboxes){
                    selected.add(o);
                }
                if(!(o.getType() == Object.TYPE.SKYBOX)) {
                    selected.add(o);
                }
            }
        }
        return selected;
    }

    @Override
    public boolean mouseMoved (int x, int y) {
        inputEventProcessor.processMouseMovedEvent(x, y);
        return false;
    }

    @Override
    public boolean scrolled (int amount) {
        scroll = inputEventProcessor.processScrolledEvent(amount);

        return true;
    }

    public ArrayList<Object> getAllObjects(){
        return obj;
    }
}
