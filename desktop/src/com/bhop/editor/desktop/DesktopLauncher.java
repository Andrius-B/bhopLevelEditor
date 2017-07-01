package com.bhop.editor.desktop;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.bhop.editor.LevelEditor;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
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
import javax.swing.JSeparator;
import javax.swing.SwingConstants;


public class DesktopLauncher {

	public DesktopLauncher(){
	}

	public static void main (String[] arg) {
		final LevelEditor lvlEditor = new LevelEditor();

		/**
		 * Preparing the frame
		 */
		final Frame frame = new Frame("bhop level editor");
		frame.setSize(1024, 720);
		//window close handling:
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				//System.out.print("WindowEvent: "+e+"\n");
				/*try{
					//dispose();
				}catch (java.lang.Exception ex){
					ex.printStackTrace();
				}*/
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
		final Canvas canvas = new Canvas();
		Dimension d = new Dimension((int)frame.getSize().getWidth()*5/7, frame.getHeight()*6/7);
		canvas.setPreferredSize(d);
		LwjglApplication app = new LwjglApplication(lvlEditor, config, canvas);


		/**
		 * Window resize event catching
		 */
		frame.addComponentListener(new ComponentListener() {
			@Override
			public void componentResized(ComponentEvent e) {
				Dimension d = new Dimension((int)frame.getSize().getWidth()*80/100, frame.getHeight()*95/100);
				canvas.setSize(d);
				System.out.print("Resize operation\n");
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
		});
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

		ModifyPanel modifyPanel = new ModifyPanel();
		Box modifyPanelBox = new Box(BoxLayout.Y_AXIS);
		modifyPanelBox.add(modifyPanel);

		frame.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 0.3; //the modify panel has to be much smaller than the canvas
		gbc.weighty = 0.5;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.insets = new Insets(8,8,8,8); //some padding
		frame.add(modifyPanelBox, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.insets = new Insets(0,0,0,0);//remove the padding
		frame.add(canvas, gbc);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		toggleViewMenuItem.dispatchEvent(new ActionEvent(toggleViewMenuItem, 500, "Wireframe"));

		EventPoller task = new EventPoller(lvlEditor);
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


}
/**
 * Handler for the shortcuts and other input events
 * That originate from the LWJGL backend
 *
 * Reminder: All keyboard input is handled in the LWJGL backend
 * because, I think, there is no other choices in the frame for keyboard focus
 */
class EventPoller extends TimerTask{
	LevelEditor lvlEditor;
	public EventPoller(LevelEditor lvlEditor){
		this.lvlEditor = lvlEditor;
	}

	public void run(){
		com.bhop.editor.Util.InputEvent e = lvlEditor.getCommand();
		if(e.keycode !=-1){
			if(e.keycode == Input.Keys.N && e.ctrlModifier && !e.shiftModifier){
				/**
				 * NEW FILE
				 */
				System.out.print("New Item shortcut used\n");
				lvlEditor.newFile();
			}else if(e.keycode == Input.Keys.S && e.ctrlModifier && !e.shiftModifier){
				/**
				 * SAVE FILE
				 */
				System.out.print("Save Level shortcut used\n");
				lvlEditor.saveFile();
			}else if(e.keycode == Input.Keys.X && e.ctrlModifier && !e.shiftModifier){
				/**
				 * EDIT -> CUT
				 */
				System.out.print("Cut shortcut used\n");
				lvlEditor.cut();
			}else if(e.keycode == Input.Keys.C && e.ctrlModifier && !e.shiftModifier){
				/**
				 * EDIT -> COPY
				 */
				System.out.print("Copy shortcut used\n");
				lvlEditor.copy();
			}else if(e.keycode == Input.Keys.V && e.ctrlModifier && !e.shiftModifier){
				/**
				 * EDIT -> PASTE
				 */
				System.out.print("Paste shortcut used\n");
				lvlEditor.paste();
			}else if(e.keycode == Input.Keys.Z && e.ctrlModifier && !e.shiftModifier){
				/**
				 * EDIT -> UNDO
				 */
				System.out.print("Undo shortcut used\n");
				lvlEditor.undo();
			}else if(e.keycode == Input.Keys.Z && !e.ctrlModifier && !e.shiftModifier){
				/**
				 * TOGGLE WIREFRAME
				 */
				System.out.print("Wireframe shortcut used\n");
				lvlEditor.changeRenderMethod();
			}
		}
	}
}

/**
 * Handling of things avaiable through the menuBar:
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
