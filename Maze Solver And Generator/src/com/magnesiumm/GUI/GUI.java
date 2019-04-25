package com.magnesiumm.GUI;

import com.magnesiumm.configurationData.actualData.GenerationAlgorithm;
import com.magnesiumm.configurationData.actualData.Mode;
import com.magnesiumm.configurationData.actualData.SolveAlgorithm;
import com.magnesiumm.logic.LogicThread;
import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;

/**
* The GUI class is responsible for the GUI menus.
*
* @author  AJ Radik and Victoria Vigorito
* @version 6.0 
*/
public class GUI{
	
	/**
	 * value={@value title}; This value represents the default title of the window.
	 */
	public static final String defaultTitle = "The a-MAZE-ing maze solver";
	
	/**
	 * value={@value iconFilePath}; This value represents the file path for the window icon.
	 */
	public static final String iconFilePath = "resources/squares.png";
	
	/**
	 * value={@value generatedMazeFilePath}; This value represents the file path for the generated maze.
	 */
	public static final String generatedMazeFilePath = "generatedMaze.txt";
	
	/**
	 * value={@value musicFilePath}; This value represents the file path for the duel of the fates easter egg music.
	 */
	public static final String musicFilePath = "resources/John Williams Duel of the Fates Star Wars Soundtrack.wav";
	
	/**
	 * The current logical thread that this GUI is representing
	 */
	private Thread currentLogicThread;
	
	/**
	 * This values represents the JFrame which the GUI is 
	 * using to display everything.
	 */
    private JFrame frame;
    
    /**
     * The part of the gui that represents the maze.
     */
    private MazeJPanel mazeJPanel;
    
    /**
     * The MenuActionListener for the active JComponents.
     */
    private MenuActionListener menuActionListener;
    
    /**
     * Used to tell paintComponent that it is reset time.
     */
    private boolean reset = false;
    
    /**
     * Denotes whether realTime is currently enabled or not.
     */
    private boolean realTime = false;
    
    /**
     * Initializes GUI window.
     * @param maze This is the maze which the GUI
     * will be responsible for displaying.
     */   
    public GUI() {
       menuActionListener = new MenuActionListener();   
       currentLogicThread = new LogicThread();
       currentLogicThread.start();
       currentLogicThread.suspend();
       frame = new JFrame();
       frame.setTitle(defaultTitle);
       ImageIcon icon = new ImageIcon(iconFilePath);
       frame.setIconImage(icon.getImage());
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       mazeJPanel = new MazeJPanel();
       frame.add(mazeJPanel);
       Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
       int minimizedWidth = (int) (screenSize.getWidth() * 0.6);
       int minimizedHeight = (int) (screenSize.getHeight() * 0.6);
       int minimizedXLocation = (int) (screenSize.getWidth() * 0.2);
       int minimizedYLocation = (int) (screenSize.getHeight() * 0.2);
       frame.setLocation(minimizedXLocation, minimizedYLocation);
       frame.setSize(minimizedWidth, minimizedHeight);
       frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
       //frame.setUndecorated(true);
       drawMenu();
       frame.setVisible(true);
    }
    
    /**
     * Draws the menu at the top of the window. Should only be called once.
     */
    public void drawMenu() {
    	//Menu Bar
    	JMenuBar menuBar = (JMenuBar) initJComponent(NavElementID.JMenuBar_menuBar);
    	
    	//Start Options Menu
        JMenu options = (JMenu) initJComponent(NavElementID.JMenu_options);
        //Speed Section
        options.add(initJComponent(NavElementID.JLabel_speed));
        options.add(initJComponent(NavElementID.JSlider_speed));
        NavElementID.JSlider_speed.getjComponent().setPreferredSize(new Dimension(460, 16));
        options.add(initJComponent(NavElementID.JMenuItem_realTime));
        options.addSeparator();
        //Generate Solve Delay Section
        options.add(initJComponent(NavElementID.JLabel_generateSolveDelay));
        options.add(initJComponent(NavElementID.JSlider_generateSolveDelay));
        NavElementID.JSlider_generateSolveDelay.getjComponent().setPreferredSize(new Dimension(460, 16));
        options.addSeparator();
        //Save As Menu Item
        options.add(initJComponent(NavElementID.JMenuItem_saveAs));
        //Reset Menu Item
        options.add(initJComponent(NavElementID.JMenuItem_reset));
        //End Options Menu
        
        //Start Mode Menu
        JMenu mode = (JMenu) initJComponent(NavElementID.JMenu_mode);
        ButtonGroup buttonGroupForMode = new ButtonGroup();
        mode.add(initJComponent(NavElementID.JRadioButtonMenuItem_demoMode, buttonGroupForMode));
        mode.add(initJComponent(NavElementID.JRadioButtonMenuItem_customMode, buttonGroupForMode));
        //End Mode Menu
        
        
        //Start Maze Menu
        JMenu maze = (JMenu) initJComponent(NavElementID.JMenu_maze);
        maze.add(initJComponent(NavElementID.JLabel_mazeSize));
        maze.add(initJComponent(NavElementID.JSlider_mazeSizeMultiplier));
        maze.addSeparator();
        maze.add(initJComponent(NavElementID.JLabel_mazeFilePathLabel));
        maze.add(initJComponent(NavElementID.JLabel_mazeFilePath));
        maze.add(initJComponent(NavElementID.JMenuItem_changeMazeFilePath));
        //End Maze Menu
        
        //Start Generation Menu
        JMenu generation = (JMenu) initJComponent(NavElementID.JMenu_generation);
        generation.setEnabled(false);
        ButtonGroup buttonGroupForGeneration = new ButtonGroup();
        generation.add(initJComponent(NavElementID.JRadioButtonMenuItem_dfsRandomGeneration, buttonGroupForGeneration));
        //End Generation Menu
        
        
        //Start Solve Menu
        JMenu solve = (JMenu) initJComponent(NavElementID.JMenu_solve);
        solve.setEnabled(false);
        ButtonGroup buttonGroupForSolve = new ButtonGroup();
        solve.add(initJComponent(NavElementID.JRadioButtonMenuItem_dfsSolve, buttonGroupForSolve));
        solve.add(initJComponent(NavElementID.JRadioButtonMenuItem_bfsSolve, buttonGroupForSolve));
        //End Solve Menu
        
        //Start Go Button
        JToggleButton go = (JToggleButton) initJComponent(NavElementID.JToggleButton_go);
        go.registerKeyboardAction(go.getActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true)), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), JComponent.WHEN_FOCUSED);
        //End Go Button
        
        //Start Adding Menus to Menu Bar
        menuBar.add(options);
        menuBar.add(mode);
        menuBar.add(maze);
        menuBar.add(generation);
        menuBar.add(solve);
        menuBar.add(go);
        //Start Adding Menus to Menu Bar
        
        //Set JFrame Menu Bar
        frame.setJMenuBar(menuBar);
    }
    
    /**
     * Adds a JComponent defined in NavElementID to our GUI.
     * @param id the NavElementID of the JComponent to be added.
     * @return The jComponent associated with the NavElementID.
     */
    public JComponent initJComponent(NavElementID id) {
    	JComponent jComponent = id.getjComponent();
    	
    	if(jComponent instanceof AbstractButton) {
    		( (AbstractButton) jComponent).addActionListener(menuActionListener);
    	}
    	
    	return jComponent;
    }
    
    /**
     * Adds a JComponent defined in NavElementID to our GUI, and adds it to the specified button group.
     * @param id id the NavElementID of the JComponent to be added
     * @param buttonGroup
     * @return The jComponent associated with the NavElementID.
     */
    public JComponent initJComponent(NavElementID id, ButtonGroup buttonGroup) {
    	JComponent jComponent = id.getjComponent();
    	buttonGroup.add( (AbstractButton) jComponent);
    	return initJComponent(id);
    }
    
    /**
     * Called when the display space of the frame needs to be reset.
     */
    public void reset() {
    	frame.setTitle(defaultTitle);
    	NavElementID.JMenu_mode.getjComponent().setEnabled(true);
    	NavElementID.JMenu_maze.getjComponent().setEnabled(true);
    	if(getMode().equals(Mode.Custom_Mode)) {
    		NavElementID.JMenu_generation.getjComponent().setEnabled(true);
    		NavElementID.JMenu_solve.getjComponent().setEnabled(true);
    	}
    	currentLogicThread.suspend();
    	currentLogicThread.stop();
    	mazeJPanel.setMaze(null);
    	reset = true;
    	makeGoState(false);
    	mazeJPanel.repaint();
    	currentLogicThread = new LogicThread();
    	currentLogicThread.start();
    	currentLogicThread.suspend();
    }

    /**
     * Sets the state of the "Go" button.
     * true makes the go button be clicked as if you clicked go.
     * false makes the go button be clicked as if you clicked stop.
     * @param state what state to set the button to
     */
    public void makeGoState(Boolean state) {
    	//false = disable it
    	//true = enable it
    	JToggleButton go = (JToggleButton) NavElementID.JToggleButton_go.getjComponent();
    	if(go.isSelected()) {
    		if(state) {
    			return;
    		}
    		else if(!state) {
    			go.doClick();
    			return;
    		}
    	}
    	else if(!go.isSelected()) {
    		if(state) {
    			go.doClick();;
    			return;
    		}
    		else if(!state) {
    			return;
    		}
    	}
    }
    
    /**
     * Fetches the NavElementID enum based on the JComponent.
     * @param jComponent The JComponent to fetch the NavElementID from.
     * @return the NavElementID associated with the JComponent.
     */
    public NavElementID getNavElementIDfromJComponent(JComponent jComponent) {
    	for(NavElementID navElementID : NavElementID.values()) {
    		if(navElementID.getjComponent().equals(jComponent)) {
    			return navElementID;
    		}
    	}
    	return null;
    }
	
    public NavElementID getSelectedNavElementIDinMenu(NavElementID menuNavElementID) {
		JMenu menu = (JMenu) menuNavElementID.getjComponent();
		
		for(Component component : menu.getMenuComponents()) {
			if(component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				if(button.isSelected()) {
					return getNavElementIDfromJComponent(button);
				}
			}
		}
		return null;
	}
    
	public JFrame getFrame() {
		return frame;
	}
	
	public boolean isReset() {
		return reset;
	}
	
	public void setReset(boolean reset) {
		this.reset = reset;
	}
	
	public int getDelayInMilliseconds() {
		
		if(realTime == true) {
			return 0;
		}
		
		JSlider slider = (JSlider) NavElementID.JSlider_speed.getjComponent();
		return slider.getMaximum() + 1 - slider.getValue();
	}
	
	public Mode getMode() {
		NavElementID selected = getSelectedNavElementIDinMenu(NavElementID.JMenu_mode);
		return (Mode) selected.getConfigurationData();
	}

	public GenerationAlgorithm getGenerationAlgorithm() {
		NavElementID selected = getSelectedNavElementIDinMenu(NavElementID.JMenu_generation);
		return (GenerationAlgorithm) selected.getConfigurationData();
	}

	public SolveAlgorithm getSolveAlgorithm() {
		NavElementID selected = getSelectedNavElementIDinMenu(NavElementID.JMenu_solve);
		return (SolveAlgorithm) selected.getConfigurationData();
	}

	public int getGenerateSolveDelay() {
		JSlider slider = (JSlider) NavElementID.JSlider_generateSolveDelay.getjComponent();
		return slider.getValue();
	}
	
	public int getMazeSizeMultiplier() {
		JSlider slider = (JSlider) NavElementID.JSlider_mazeSizeMultiplier.getjComponent();
		return slider.getValue();
	}

	public MazeJPanel getMazeJPanel() {
		return mazeJPanel;
	}

	public boolean isRealTime() {
		return realTime;
	}

	public void setRealTime(boolean realTime) {
		this.realTime = realTime;
	}

	public Thread getCurrentLogicThread() {
		return currentLogicThread;
	}

	public void setCurrentLogicThread(Thread currentLogicThread) {
		this.currentLogicThread = currentLogicThread;
	}
	
	public String getCurrentMazeFilePath() {
		JLabel label = (JLabel) NavElementID.JLabel_mazeFilePath.getjComponent();
		return label.getText();
	}

}