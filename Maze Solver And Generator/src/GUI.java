import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.LinkedList;
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
public class GUI extends JPanel implements ActionListener {
	
	/**
	 * value={@value title}; This value represents the title of the window
	 */
	public static final String defaultTitle = "The a-MAZE-ing maze solver";
	
	/**
	 * value={@value iconFilePath}; This value represents the file path for the window icon
	 */
	public static final String iconFilePath = "square2.png";
	
	/**
	 * value={@value musicFilePath}; This value represents the file path for the duel of the fates easter egg music
	 */
	public static final String musicFilePath = "John Williams Duel of the Fates Star Wars Soundtrack.wav";
	
	/**
     * value={@value buffer}; This value represents the buffer of space
     * on all sides of the GUI window, in pixels.
     */ 
	public static final int buffer = 6;
	
	/**
	 * The current logical thread that this GUI is representing
	 */
	private Thread currentLogicThread;
	
	/**
	 * This value represents the maze which the GUI is representing.
	 */
	private Maze maze;
	
	/**
	 * This values represents the JFrame which the GUI is 
	 * using to display everything.
	 */
    private JFrame frame;
    
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
     * LinkedList containing all jComponents within the GUI.
     */
    private LinkedList<NavElementID> jComponents = new LinkedList<NavElementID>();
    
    
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
       frame.add(this);
       frame.setLocation(0, 0);
       frame.setSize(600, 600);
       frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
       //frame.setUndecorated(true);
       drawMenu();
       frame.setVisible(true);
    }
    
    
    public JComponent initJComponent(NavElementID id) {
    	JComponent jComponent = id.getjComponent();
    	
    	if(jComponent instanceof AbstractButton) {
    		( (AbstractButton) jComponent).addActionListener(this);
    	}
    	
    	jComponents.add(id);
    	return jComponent;
    }
    
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
    	maze = null;
    	reset = true;
    	makeGoState(false);
    	repaint();
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
    	JToggleButton mockGo = (JToggleButton) NavElementID.JToggleButton_go.getjComponent();
    	if(mockGo.getText().equals("Stop")) {
    		if(state) {
    			return;
    		}
    		else if(!state) {
    			mockGo.doClick();
    			return;
    		}
    	}
    	else if(mockGo.getText().equals("Go")) {
    		if(state) {
    			mockGo.doClick();;
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
    	for(NavElementID navElementID : jComponents) {
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
		//unqualified vs qualified enums
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
			    else if(realTime){
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
			
			case JToggleButton_go:
				String textState = source.getText();
				if(textState.equals("Go")) {
					//Go clicked
					NavElementID.JMenu_maze.getjComponent().setEnabled(false);
					NavElementID.JMenu_mode.getjComponent().setEnabled(false);
					NavElementID.JMenu_generation.getjComponent().setEnabled(false);
					NavElementID.JMenu_solve.getjComponent().setEnabled(false);
					source.setText("Stop");
					currentLogicThread.resume();
				}
				else if(textState.equals("Stop")) {
					//Stop clicked
					source.setText("Go");
					currentLogicThread.suspend();
				}
				break;
			
			default:
				break;
		}
	}

	/**
     * This method is responsible for drawing the maze
     * and all of its nodes in the correct states. It is called
     * (down the line) every time repaint() is called. This method
     * updates the GUI every time repaint() is called.
     * @param graphics The graphics object used by the method.
     */
    @Override
    protected void paintComponent(Graphics graphics) {
            
    	super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = getWidth();
        int height = getHeight();
       
        if(maze == null) {
        	return;
        }
        
        if(reset == true) {
        	graphics2D.clearRect(0, 0, width, height);
        	reset = false;
        }
        
        Node[][] nodes = maze.getNodes();
        
        double xIncrement = (double) (width - 2*buffer)/nodes[0].length;
        double yIncrement = (double) (height - 2*buffer)/nodes.length;
        
        //Draw Squares
        for(int row = 0; row<nodes.length; row++) {
        	for(int col = 0; col<nodes[0].length; col++) {
        		double x = buffer + col*xIncrement;
        		double y = buffer + row*yIncrement;
        		double rectWidth = xIncrement;
        		double rectHeight = yIncrement;
        		Rectangle2D rect = new Rectangle2D.Double(x, y, rectWidth, rectHeight);
        		graphics2D.setPaint(nodes[row][col].getColor());
        		graphics2D.fill(rect);
        	}
        }
       
        graphics2D.setPaint(Color.blue);
        for(int i = 0; i <= nodes[0].length; i++) {
            double x = buffer + i*xIncrement;
            graphics2D.draw(new Line2D.Double(x, buffer, x, height-buffer));
        }
        
        for(int i = 0; i <= nodes.length; i++) {
            double y = buffer + i*yIncrement;
            graphics2D.draw(new Line2D.Double(buffer, y, width-buffer, y));
        }
    }
	
	public JFrame getFrame() {
		return frame;
	}
	
	public void setMaze(Maze maze) {
		this.maze = maze;
	}
	
	public int getDelayInMilliseconds() {
		
		if(realTime == true) {
			return 0;
		}
		
		JSlider slider = (JSlider) NavElementID.JSlider_speed.getjComponent();
		return slider.getMaximum() + 1 - slider.getValue();
	}

	public void setDelayInMilliseconds(int milliseconds) {
		JSlider slider = (JSlider) NavElementID.JSlider_mazeSizeMultiplier.getjComponent();
		slider.setValue(slider.getMaximum() + 1 - milliseconds);
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

	public void setMazeSizeMultiplier(int mazeSizeMultiplier) {
		JSlider slider = (JSlider) NavElementID.JSlider_mazeSizeMultiplier.getjComponent();
		slider.setValue(mazeSizeMultiplier);
	}

}