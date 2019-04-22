package com.magnesiumm.GUI;

import com.magnesiumm.configurationData.*;
import com.magnesiumm.logic.LogicThread;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

/**
* The GUI class is responsible for displaying the 
* maze-solving algorithms with a delay. An object of the GUI class will be stored
* in an object of Main, and then it will also often be repainted in the MazeSolver
* class by the maze-solving algorithms, in order to refresh the GUI and display
* the algorithms at work.
*
* @author  AJ Radik and Victoria Vigorito
* @version 5.0 
*/
public class GUI implements ActionListener {
	
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
	public static final String generatedMazeFilePath = "resources/generatedMaze.txt";
	
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
     * Denotes whether easter egg has been triggered. Can only happen once.
     */
    private boolean easterEggTriggered = false;
    
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
       currentLogicThread = new LogicThread();
       currentLogicThread.start();
       currentLogicThread.suspend();
       frame = new JFrame();
       frame.setTitle(defaultTitle);
       ImageIcon icon = new ImageIcon(iconFilePath);
       frame.setIconImage(icon.getImage());
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       mazeJPanel = new MazeJPanel(this);
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
     * Adds a JComponent defined in NavElementID to our GUI.
     * @param id the NavElementID of the JComponent to be added.
     * @return The jComponent associated with the NavElementID.
     */
    public JComponent initJComponent(NavElementID id) {
    	JComponent jComponent = id.getjComponent();
    	
    	if(jComponent instanceof AbstractButton) {
    		( (AbstractButton) jComponent).addActionListener(this);
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
     * Draws the menu at the top of the window. Should only be called once.
     */
    public void drawMenu() {
    	JMenuBar menuBar = (JMenuBar) initJComponent(NavElementID.JMenuBar_menuBar);
    	
        JMenu options = (JMenu) initJComponent(NavElementID.JMenu_options);
        options.add(initJComponent(NavElementID.JLabel_speed));
        options.add(initJComponent(NavElementID.JSlider_speed));
		NavElementID.JSlider_speed.getjComponent().setPreferredSize(new Dimension(460, 16));
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        options.add(initJComponent(NavElementID.JMenuItem_realTime));
        options.addSeparator();
        options.add(initJComponent(NavElementID.JMenuItem_reset));
        
        JMenu mode = (JMenu) initJComponent(NavElementID.JMenu_mode);
        ButtonGroup buttonGroupForMode = new ButtonGroup();
        mode.add(initJComponent(NavElementID.JRadioButtonMenuItem_demoMode, buttonGroupForMode));
        mode.add(initJComponent(NavElementID.JRadioButtonMenuItem_customMode, buttonGroupForMode));
        
        JMenu maze = (JMenu) initJComponent(NavElementID.JMenu_maze);
        maze.add(initJComponent(NavElementID.JLabel_mazeSize));
        maze.add(initJComponent(NavElementID.JSlider_mazeSizeMultiplier));
        maze.addSeparator();
        maze.add(initJComponent(NavElementID.JLabel_mazeFilePathLabel));
        maze.add(initJComponent(NavElementID.JLabel_mazeFilePath));
        maze.add(initJComponent(NavElementID.JMenuItem_changeMazeFilePath));
        
        JMenu generation = (JMenu) initJComponent(NavElementID.JMenu_generation);
        generation.setEnabled(false);
        ButtonGroup buttonGroupForGeneration = new ButtonGroup();
        generation.add(initJComponent(NavElementID.JRadioButtonMenuItem_dfsRandomGeneration, buttonGroupForGeneration));
        
        JMenu solve = (JMenu) initJComponent(NavElementID.JMenu_solve);
        solve.setEnabled(false);
        ButtonGroup buttonGroupForSolve = new ButtonGroup();
        solve.add(initJComponent(NavElementID.JRadioButtonMenuItem_dfsSolve, buttonGroupForSolve));
        solve.add(initJComponent(NavElementID.JRadioButtonMenuItem_bfsSolve, buttonGroupForSolve));
        
        JToggleButton go = (JToggleButton) initJComponent(NavElementID.JToggleButton_go);
        go.registerKeyboardAction(go.getActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true)), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), JComponent.WHEN_FOCUSED);
        
        menuBar.add(options);
        menuBar.add(mode);
        menuBar.add(maze);
        menuBar.add(generation);
        menuBar.add(solve);
        menuBar.add(go);
        
        frame.setJMenuBar(menuBar);
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
    
    /**
     * Handles actions on nav items.
     * @param e The ActionEvent to be handled
     */
	@Override
	public void actionPerformed(ActionEvent e) {
		AbstractButton source = (AbstractButton) e.getSource();
		NavElementID navElementID = getNavElementIDfromJComponent(source);

		switch(navElementID) {
			
			case JMenuItem_realTime:
				//easter egg
				if(!easterEggTriggered) {
					makeGoState(true);
					new Thread(new Runnable() {
					    public void run() {
					    	try {
					    		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(musicFilePath).getAbsoluteFile());
						        Clip clip = AudioSystem.getClip();
						        clip.open(audioInputStream);
						        clip.start();
						        Thread.sleep(clip.getMicrosecondLength()/1000);
						    } catch(Exception e) {
						        e.printStackTrace();
						    }
					    }
					  }).start();
					easterEggTriggered = true;
				}
				
				JSlider speedSlider = (JSlider) NavElementID.JSlider_speed.getjComponent();
				
				if(!realTime) {
					speedSlider.setEnabled(false);
					realTime = true;
			    }
			    
				else if(realTime) {
			    	speedSlider.setEnabled(true);
			    	realTime = false;
			    }
				break;
			
			case JMenuItem_reset:
				reset();
				break;
			
			case JRadioButtonMenuItem_demoMode:
				NavElementID.JMenu_generation.getjComponent().setEnabled(false);
				NavElementID.JMenu_solve.getjComponent().setEnabled(false);
				break;
			
			case JRadioButtonMenuItem_customMode:
				NavElementID.JMenu_generation.getjComponent().setEnabled(true);
				NavElementID.JMenu_solve.getjComponent().setEnabled(true);
				break;
			
			case JMenuItem_changeMazeFilePath:
				JLabel label = (JLabel) NavElementID.JLabel_mazeFilePath.getjComponent();
//				String currentMazeFilePath = label.getText();
				String newMazeFilePath = JOptionPane.showInputDialog(source.getText() + " - For a randomly generated maze allow for default path: \""+generatedMazeFilePath+"\"", generatedMazeFilePath);
				label.setText(newMazeFilePath);
				break;
				
			case JToggleButton_go:
				String sourceText = source.getText();
				if(sourceText.equals("Go")) {
					//Go clicked
					NavElementID.JMenu_maze.getjComponent().setEnabled(false);
					NavElementID.JMenu_mode.getjComponent().setEnabled(false);
					NavElementID.JMenu_generation.getjComponent().setEnabled(false);
					NavElementID.JMenu_solve.getjComponent().setEnabled(false);
					source.setSelected(true);
					source.setText("Stop");
					currentLogicThread.resume();
				}
				else if(sourceText.equals("Stop")) {
					//Stop clicked
					source.setSelected(false);
					source.setText("Go");
					currentLogicThread.suspend();
				}
				break;
			
			default:
				break;
		}
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

	public int getMazeSizeMultiplier() {
		JSlider slider = (JSlider) NavElementID.JSlider_mazeSizeMultiplier.getjComponent();
		return slider.getValue();
	}

	public MazeJPanel getMazeJPanel() {
		return mazeJPanel;
	}

}