package com.bhop.editor.desktop;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.bhop.editor.LevelEditor;
import com.sun.java.swing.plaf.motif.MotifLookAndFeel;

import org.lwjgl.Sys;
import org.lwjgl.opengl.GREMEDYFrameTerminator;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.UIManager;

/**
 * I think it's better to keep the static main function isolated
 * This class is supposed to be the main 'window' instance this includes controlling
 * data transfer between the GUI and libgdx app
 */

public class MainFrameManager {
    public LevelEditor lvlEditor;
    public JFrame frame;
    private Canvas canvas;
    private ModifyPanel modifyPanel;
    private JPanel contentPanel;

    private Dimension modifyPanelDimension;
    public MainFrameManager(){
        lvlEditor = new LevelEditor();

        /**
         * SWING is pretty ugly tbqh
         */
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            //yeah no
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }


        /**
         * Preparing the frame
         */
        frame = new JFrame("bhop level editor");
        Font font = new Font("Verdana", Font.PLAIN, 12);
        frame.setFont(font);
        frame.setSize(1024, 720);
        modifyPanelDimension = new Dimension(310,250);
        //window close handling:
        frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                lvlEditor.exit();
            }
        });

        /**
         * Set up the LWJGL backend for the game rendering window
         */
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 720;
        config.width = 1024;
        config.samples = 4;//antialiasing
        canvas = new Canvas();
        validateResize();
        LwjglApplication app = new LwjglApplication(lvlEditor, config, canvas);


        /**
         * Window resize event catching
         * This section is no longer required, because of the change to the main frame layout
         * Now with layout manager set to null, the window is less scalable, though
         */
        /*frame.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                //validateResize();
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });*/
        /**
         * Window resize catching end
         */

        /**
         * MENU SETUP:
         */
        final MenuBar menuBar = new MenuBar();

        /**
         * File menu items
         */

        Menu fileMenu = new Menu("File");

        FileItemListener fileItemListener = new FileItemListener(frame, lvlEditor);

        MenuItem newMenuItem = new MenuItem("New",new MenuShortcut(KeyEvent.VK_N));
        newMenuItem.setActionCommand("New");
        newMenuItem.addActionListener(fileItemListener);

        MenuItem openMenuItem = new MenuItem("Open");
        openMenuItem.setActionCommand("Open");
        openMenuItem.addActionListener(fileItemListener);

        MenuItem saveMenuItem = new MenuItem("Save", new MenuShortcut(KeyEvent.VK_S));
        saveMenuItem.setActionCommand("Save");
        saveMenuItem.addActionListener(fileItemListener);

        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setActionCommand("Exit");
        exitMenuItem.addActionListener(fileItemListener);

        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        /**
         * File menu end
         */

        /**
         * Edit menu items
         */
        Menu editMenu = new Menu("Edit");

        EditItemListener editMenuListener = new EditItemListener(lvlEditor);

        MenuItem cutMenuItem = new MenuItem("Cut", new MenuShortcut(KeyEvent.VK_X));
        cutMenuItem.setActionCommand("Cut");
        cutMenuItem.addActionListener(editMenuListener);
        editMenu.add(cutMenuItem);

        MenuItem copyMenuItem = new MenuItem("Copy", new MenuShortcut(KeyEvent.VK_C));
        copyMenuItem.setActionCommand("Copy");
        copyMenuItem.addActionListener(editMenuListener);
        editMenu.add(copyMenuItem);

        MenuItem pasteMenuItem = new MenuItem("Paste", new MenuShortcut(KeyEvent.VK_V));
        pasteMenuItem.setActionCommand("Paste");
        pasteMenuItem.addActionListener(editMenuListener);
        editMenu.add(pasteMenuItem);

        MenuItem duplicateMenuItem = new MenuItem("Duplicate", new MenuShortcut(KeyEvent.VK_D));
        duplicateMenuItem.setActionCommand("Duplicate");
        duplicateMenuItem.addActionListener(editMenuListener);
        editMenu.add(duplicateMenuItem);

        MenuItem undoMenuItem = new MenuItem("Undo", new MenuShortcut(KeyEvent.VK_Z));
        undoMenuItem.setActionCommand("Undo");
        undoMenuItem.addActionListener(editMenuListener);
        editMenu.add(undoMenuItem);

        /**
         * Edit menu end
         */


        /**
         * Insert menu items
         */

        Menu insertMenu = new Menu("Insert");

        InsertItemListener insertItemListener = new InsertItemListener(lvlEditor);

        MenuItem platformMenuItem = new MenuItem("Platform");
        platformMenuItem.setActionCommand("Platfrom");
        platformMenuItem.addActionListener(insertItemListener);
        insertMenu.add(platformMenuItem);

        MenuItem skyboxMenuItem = new MenuItem("SkyBox");
        skyboxMenuItem.setActionCommand("SkyBox");
        skyboxMenuItem.addActionListener(insertItemListener);
        insertMenu.add(skyboxMenuItem);

        MenuItem startMenuItem = new MenuItem("Start");
        startMenuItem.setActionCommand("Start");
        startMenuItem.addActionListener(insertItemListener);
        insertMenu.add(startMenuItem);

        /**
         * Insert menu end
         */

        /**
         * View menu items
         */
        Menu viewMenu = new Menu("View");

        ViewItemListener viewItemListener = new ViewItemListener(frame, lvlEditor);

        MenuItem toggleViewMenuItem = new MenuItem("Toggle Wireftame   \t Z");
        toggleViewMenuItem.setActionCommand("Wireframe");
        toggleViewMenuItem.addActionListener(viewItemListener);
        viewMenu.add(toggleViewMenuItem);


        //add menu to menubar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(insertMenu);
        menuBar.add(viewMenu);

        //add menubar to the frame
        frame.setMenuBar(menuBar);

        contentPanel = new JPanel();


        contentPanel.setLayout(new BorderLayout());
        frame.setLayout(new BorderLayout());

        modifyPanel = new ModifyPanel(this, font);
        Dimension modifyPanelSize = modifyPanelDimension;

        modifyPanel.setBackground(Color.LIGHT_GRAY);

        modifyPanel.setBounds(8,8, (int)modifyPanelSize.getWidth(), (int)modifyPanelSize.getHeight());
        modifyPanel.setLocation(8,8);

        frame.add(contentPanel, BorderLayout.CENTER);
        frame.doLayout();

        contentPanel.add(modifyPanel);

        canvas.setBounds(0,0, frame.getWidth(), frame.getHeight());
        contentPanel.add(canvas);


        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        EventPoller task = new EventPoller(lvlEditor, modifyPanel);
        Timer eventTimer = new Timer("eventTimer");
        eventTimer.schedule(task, 10, 5);

        /**
         * TESTING WINDOW:
         */
		/*Frame test = new Frame("Testing some layout options");
		test.setSize(512, 512);

		Panel panel = new ModifyPanel();

		test.add(panel);
		test.setLocationRelativeTo(null);
		test.setVisible(true);*/
        /**
         * TESTING WINDOW END
         */
    }
    public void validateResize(){
        /**
         * The main content panel has no layout manager so we just change the bounds of our canvas on resize
         */
        canvas.setBounds(0,0, frame.getWidth(), frame.getHeight());
    }
}

/**
 * Handler for the shortcuts and other input events
 * That originate from the LWJGL backend
 *
 * Reminder: All keyboard input is handled in the LWJGL backend
 * because, I think, there is no other choices in the frame for keyboard focus
 */
class EventPoller extends TimerTask {
    LevelEditor lvlEditor;
    com.bunny.jump.Game.Objects.Object modObject;
    ModifyPanel modifyPanel;
    public EventPoller(LevelEditor lvlEditor, ModifyPanel modifyPanel){
        this.lvlEditor = lvlEditor;
        this.modifyPanel = modifyPanel;
    }

    public void run(){
        /**
         * Object selection to modify panel transfer
         */
        if(lvlEditor.getSingleSelection()!=null && modObject!=lvlEditor.getSingleSelection()){
            modifyPanel.setObject(lvlEditor.getSingleSelection());
            modObject = lvlEditor.getSingleSelection();
            modifyPanel.setVisible(true);
        }else if(lvlEditor.getSingleSelection()==null){
            modObject = null;
            modifyPanel.setVisible(false);
        }


        /**
         * SHORTCUT HANDLES:
         */
        com.bhop.editor.Util.InputEvent e = lvlEditor.getCommand();
        if(e.keycode !=-1){
            /**
             * Reminder about InputEvent usage
             * InputEvent.keycode directly correlates with libgdx Input.Keys
             * InputEvent.ctrlModifier & InputEvent.shiftModifier are just bools that tell weather
             * the "modifier" keys were pressed at the time of receiving input
             *
             * On a side-note this means that every time the modifier keys are pressed (ctrls/shifts both right and left)
             * an event will end up here.
             */
            if(e.keycode == Input.Keys.N && e.ctrlModifier && !e.shiftModifier){
                /**
                 * NEW FILE
                 * [CTRL+N]
                 */
                System.out.print("New Item shortcut used\n");
                lvlEditor.newFile();
            }else if(e.keycode == Input.Keys.S && e.ctrlModifier && !e.shiftModifier){
                /**
                 * SAVE FILE
                 * [CTRL+S]
                 */
                System.out.print("Save Level shortcut used\n");
                lvlEditor.saveFile();
            }else if(e.keycode == Input.Keys.X && e.ctrlModifier && !e.shiftModifier){
                /**
                 * EDIT -> CUT
                 * [CTRL+X]
                 */
                System.out.print("Cut shortcut used\n");
                lvlEditor.cut();
            }else if(e.keycode == Input.Keys.C && e.ctrlModifier && !e.shiftModifier){
                /**
                 * EDIT -> COPY
                 * [CTRL+C]
                 */
                System.out.print("Copy shortcut used\n");
                lvlEditor.copy();
            }else if(e.keycode == Input.Keys.V && e.ctrlModifier && !e.shiftModifier){
                /**
                 * EDIT -> PASTE
                 * [CTRL+V]
                 */
                System.out.print("Paste shortcut used\n");
                lvlEditor.paste();
            }else if(e.keycode == Input.Keys.Z && e.ctrlModifier && !e.shiftModifier){
                /**
                 * EDIT -> UNDO
                 * [CTRL+Z]
                 */
                System.out.print("Undo shortcut used\n");
                lvlEditor.undo();
            }else if(e.keycode == Input.Keys.Z && !e.ctrlModifier && !e.shiftModifier){
                /**
                 * TOGGLE WIREFRAME
                 * [Z]
                 */
                System.out.print("Wireframe shortcut used\n");
                lvlEditor.changeRenderMethod();
            }else if(e.keycode == Input.Keys.D && e.ctrlModifier && !e.shiftModifier){
                /**
                 * EDIT -> DUPLICATE
                 * [CTRL+D]
                 */
                System.out.print("Duplicate shortcut used\n");
                lvlEditor.duplicate();
            }
        }
    }
}

/**
 * Handling of things available through the menuBar:
 */

class FileItemListener implements ActionListener {
    private Frame frame;
    private LevelEditor lvlEditor;
    public FileItemListener(Frame frame, LevelEditor lvlEditor){
        this.frame = frame;
        this.lvlEditor = lvlEditor;
    }
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("New")){
            /**
             * NEW FILE
             */
            System.out.print("Create new file menu item used\n");
            lvlEditor.newFile();
        }else if(e.getActionCommand().equals("Open")){
            /**
             * OPEN FILE
             * ////////////////////////////////////////////
             * THE ONLY WAY TO DO THIS
             * //////////////////////////////////////
             */
            System.out.print("Open a file:\nFrom:"+e.getSource().toString()+"\n");
            FileDialog fd = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
            //fd.setDirectory("C:\\");
            fd.setFile("*.json");
            fd.setVisible(true);
            String filename = fd.getFile();
            if (filename == null){
                System.out.println("File Dialog closed!\n");

            }else {
                String filepath = fd.getDirectory()+filename;
                System.out.println("You chose " + filepath);

                FileHandle levelFile = new FileHandle(filepath);
                System.out.print("Selected level file found:"+levelFile.exists()+"\n");
                /**
                 * In the game levels are in a folder, which is in the assets folder
                 * Thus getting the parent directory of the folder gets the working(assets) directory
                 */
                FileHandle parentDir = levelFile.parent().parent();
                lvlEditor.setWorkingDir(parentDir.path()+"/");
                lvlEditor.setFile(new File(filepath));

            }
        }else if(e.getActionCommand().equals("Save")){
            /**
             * SAVE FILE
             */
            System.out.print("Save current file menu item\n");
            lvlEditor.saveFile();
        }else if(e.getActionCommand().equals("Exit")){
            /**
             * CLOSE THE PROGRAM
             */
            System.out.print("Close menu item used\n");
            lvlEditor.exit();
        }
    }
}

class EditItemListener implements ActionListener {
    LevelEditor lvlEditor;
    public EditItemListener(LevelEditor lvlEditor){
        this.lvlEditor = lvlEditor;
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Copy")){
            /**
             * EDIT -> COPY
             */
            System.out.print("Copy selection menu item used\n");
            lvlEditor.copy();
        }else if(e.getActionCommand().equals("Cut")){
            /**
             * EDIT -> CUT
             */
            System.out.print("Cut selection menu item used\n");
            lvlEditor.cut();
        }else if(e.getActionCommand().equals("Paste")){
            /**
             * EDIT -> PASTE
             */
            System.out.print("Paste selection menu item used\n");
            lvlEditor.paste();
        }else if(e.getActionCommand().equals("Duplicate")){
            /**
             * EDIT -> DUPLICATE
             */
            System.out.print("Duplicate selection menu item used\n");
            lvlEditor.duplicate();
        }else if(e.getActionCommand().equals("Undo")){
            /**
             * EDIT -> UNDO
             */
            System.out.print("Undo selection menu item used\n");
            lvlEditor.undo();
        }
    }
}

class InsertItemListener implements ActionListener{
    private LevelEditor lvlEditor;
    public InsertItemListener(LevelEditor lvlEditor){
        this.lvlEditor = lvlEditor;
    }
    public void actionPerformed(ActionEvent e){
        System.out.print("Menu path to create: " + e.getActionCommand() + "\n");
        lvlEditor.createNew(e.getActionCommand());
    }
}

class ViewItemListener implements ActionListener{
    private Frame frame;
    private LevelEditor lvlEditor;
    public ViewItemListener(Frame frame, LevelEditor lvlEditor){
        this.frame = frame;
        this.lvlEditor = lvlEditor;
    }
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equals("Wireframe")){
            /**
             * TOGGLE WIREFRAME
             */
            System.out.print("Menu used to toggle the wireframe");
            lvlEditor.changeRenderMethod();
        }
    }
}
