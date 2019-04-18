import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.HashMap;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
public class GUI extends JPanel implements ActionListener, ChangeListener {
	
	/**
	 * value={@value title}; This value represents the title of the window
	 */
	public static final String title = "The a-MAZE-ing maze solver";
	
	/**
	 * value={@value iconFilePath}; This value represents the file path for the window icon
	 */
	public static final String iconFilePath = "square2.png";
	
	/**
     * value={@value buffer}; This value represents the buffer of space
     * on all sides of the GUI window in pixels.
     */ 
	public static final int buffer = 6;
	
	/**
	 * The current logical thread that this GUI is representing
	 */
	private Thread mainThread;
	
	/**
	 * The delay in milliseconds set by this GUI.
	 */
	private int delayInMilliseconds = 1;
	
	/**
	 * The mode set by this GUI.
	 */
	private String mode = "Demo Mode";
	
	/**
	 * The generation algorithm set by this GUI.
	 */
	private String generationAlgorithm = "DFS Random Generation (Depth-First-Search)";
	
	/**
	 * The solving algorithm set by this GUI.
	 */
	private String solveAlgorithm = "DFS (Depth-First-Search)";
	
	private int mazeSizeMultiplier = 10;
	
	/**
	 * This value represents the maze which the GUI is displaying.
	 */
	private Maze maze;
	
	/**
	 * This values represents the JFrame which the GUI is 
	 * using to display everything.
	 */
    private JFrame frame;
    
    /**
     * The stop and go button.
     */
  
    private boolean easterEggTriggered = false;
    
    /**
     * Used to tell paintComponent that it is reset time.
     */
    private boolean reset = false;

    /**
     * This constructor is called by the Main class,
     * it sets up everything needed in order later draw the maze.
     * @param maze This is the maze which the GUI
     * will be responsible for displaying.
     */   
    public GUI() {
       mainThread = new Main();
       mainThread.start();
       mainThread.suspend();
       frame = new JFrame();
       frame.setTitle(title);
       ImageIcon icon = new ImageIcon(iconFilePath);
       frame.setIconImage(icon.getImage());
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.add(this);
       frame.setLocation(0, 0);
       frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
       //frame.setUndecorated(true);
       
       drawMenu();
       frame.setVisible(true);
    }
    
    private HashMap<String, JComponent> mappedJComponents = new HashMap<String, JComponent>();
    
    public JComponent initJComponent(String name, JComponent jComponent) {
    	jComponent.setName(name);
    	
    	if(jComponent instanceof AbstractButton) {
    		( (AbstractButton) jComponent).addActionListener(this);
    	}
    	
    	else if(jComponent instanceof JSlider) {
    		( (JSlider) jComponent).addChangeListener(this);
    	}
    	
    	mappedJComponents.put(name, jComponent);
    	return jComponent;
    }
    
    public JComponent initJComponent(JComponent jComponent) {
    	String name = null;
    	
    	if(jComponent instanceof AbstractButton) {
    		name = ( (AbstractButton) jComponent).getText();
    	}
    	
    	else if(jComponent instanceof JLabel) {
    		name = ( (JLabel) jComponent).getText();
    	}
    	
    	return initJComponent(name, jComponent);
    }
    
    public JComponent initJComponent(JComponent jComponent, ButtonGroup buttonGroup) {
    	buttonGroup.add( (AbstractButton) jComponent);
    	return initJComponent(jComponent);
    }
    
    /**
     * Draws the menu at the top of the window. Should only be called once.
     */
    public void drawMenu() {
    	JMenuBar menuBar = (JMenuBar) initJComponent("Menu Bar", new JMenuBar());
    	
        JMenu options = (JMenu) initJComponent(new JMenu("Options"));
        options.add(initJComponent(new JLabel("Speed")));
        options.add(initJComponent("Speed Slider", new JSlider(JSlider.HORIZONTAL, 1, 200, 200)));
		((JSlider) mappedJComponents.get("Speed Slider")).setPreferredSize(new Dimension(460, 16));
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        options.add(initJComponent(new JMenuItem("Real Time (Seizure Warning)")));
        options.addSeparator();
        options.add(initJComponent(new JMenuItem("Reset")));
        
        JMenu mode = (JMenu) initJComponent(new JMenu("Mode"));
        ButtonGroup buttonGroupForMode = new ButtonGroup();
        mode.add(initJComponent(new JRadioButtonMenuItem("Demo Mode", true), buttonGroupForMode));
        mode.add(initJComponent(new JRadioButtonMenuItem("Custom Mode", false), buttonGroupForMode));
        
        JMenu maze = (JMenu) initJComponent(new JMenu("Maze"));
        maze.add(initJComponent(new JLabel("Maze Size - WARNING: Large sizes may cause stack overflow!")));
        maze.add(initJComponent("Maze Size Multiplier Slider", new JSlider(JSlider.HORIZONTAL, 1, 20, mazeSizeMultiplier)));
        
        JMenu generation = (JMenu) initJComponent(new JMenu("Generation"));
        generation.setEnabled(false);
        ButtonGroup buttonGroupForGeneration = new ButtonGroup();
        generation.add(initJComponent(new JRadioButtonMenuItem("DFS Random Generation (Depth-First-Search)", true), buttonGroupForGeneration));
        
        JMenu solve = (JMenu) initJComponent(new JMenu("Solve"));
        solve.setEnabled(false);
        ButtonGroup buttonGroupForSolve = new ButtonGroup();
        solve.add(initJComponent(new JRadioButtonMenuItem("DFS (Depth-First-Search)", true), buttonGroupForSolve));
        solve.add(initJComponent(new JRadioButtonMenuItem("BFS (Breadth-First-Search)", false), buttonGroupForSolve));
        
        JToggleButton go = (JToggleButton) initJComponent(new JToggleButton("Go"));
        go.registerKeyboardAction(go.getActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true)), 
        		KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), JComponent.WHEN_FOCUSED);
        
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
    	( (JMenu) mappedJComponents.get("Mode")).setEnabled(true);
    	( (JMenu) mappedJComponents.get("Maze")).setEnabled(true);
    	if(mode.equals("Custom Mode")) {
    		( (JMenu) mappedJComponents.get("Generation")).setEnabled(true);
    		( (JMenu) mappedJComponents.get("Solve")).setEnabled(true);
    	}
    	mainThread.suspend();
    	mainThread.stop();
    	maze = null;
    	reset = true;
    	makeGoState("Go");
    	repaint();
    	mainThread = new Main();
    	mainThread.start();
    	mainThread.suspend();
    }

    public void makeGoState(String state) {
    	JToggleButton mockGo = (JToggleButton) mappedJComponents.get("Go");
    	if(!mockGo.getText().equals(state)) {
    		mockGo.doClick();
    	}
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		AbstractButton item = (AbstractButton) e.getSource();
		String name = item.getName();
		
		switch(name) {
			
			case "Real Time (Seizure Warning)":
				//easter egg
				if(!easterEggTriggered) {
					makeGoState("Stop");
					new Thread(new Runnable() {
					    public void run() {
					    	try {
					    		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("John Williams Duel of the Fates Star Wars Soundtrack.wav").getAbsoluteFile());
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
				
				if(delayInMilliseconds != 0) {
			    	delayInMilliseconds = 0;
			    }
			    else {
			    	JSlider speedSlider = (JSlider) mappedJComponents.get("Speed Slider");
			    	delayInMilliseconds = speedSlider.getMaximum()+1 - speedSlider.getValue();
			    }
				break;
			
			case "Reset":
				reset();
				break;
			
			case "Demo Mode":
				this.mode = "Demo Mode";
				( (JMenu) mappedJComponents.get("Generation")).setEnabled(false);
				( (JMenu) mappedJComponents.get("Solve")).setEnabled(false);
				break;
			
			case "Custom Mode":
				this.mode = "Custom Mode";
				( (JMenu) mappedJComponents.get("Generation")).setEnabled(true);
				( (JMenu) mappedJComponents.get("Solve")).setEnabled(true);
				break;
			
			case "DFS Random Generation (Depth-First-Search)":
				this.generationAlgorithm = "DFS Random Generation (Depth-First-Search)";
				break;
			
			case "DFS (Depth-First-Search)":
				this.solveAlgorithm = "DFS (Depth-First-Search)";
				break;
			
			case "BFS (Breadth-First-Search)":
				this.solveAlgorithm = "BFS (Breadth-First-Search)";
				break;
			
			case "Go":
				String textState = item.getText();
				if(textState.equals("Go")) {
					//Go clicked
					( (JMenu) mappedJComponents.get("Maze")).setEnabled(false);
					( (JMenu) mappedJComponents.get("Mode")).setEnabled(false);
					( (JMenu) mappedJComponents.get("Generation")).setEnabled(false);
					( (JMenu) mappedJComponents.get("Solve")).setEnabled(false);
					item.setText("Stop");
					mainThread.resume();
				}
				else if(textState.equals("Stop")) {
					//Stop clicked
					item.setText("Go");
					mainThread.suspend();
				}
				break;
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider item = ( (JSlider) e.getSource());
		String name = item.getName();
		
		switch(name) {
			
			case "Speed Slider":
				delayInMilliseconds = item.getMaximum()+1 - item.getValue();
				break;
				
			case "Maze Size Multiplier Slider":
				mazeSizeMultiplier = item.getValue();
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
        int w = getWidth();
        int h = getHeight();
       
        if(maze == null) {
        	return;
        }
        
        if(reset == true) {
        	graphics2D.clearRect(0, 0, w, h);
        	reset = false;
        }
        
        Node[][] nodes = maze.getNodes();
        
        double xInc = (double) (w - 2*buffer)/nodes[0].length;
        double yInc = (double) (h - 2*buffer)/nodes.length;
        
        //Draw Squares
        for(int row = 0; row<nodes.length; row++) {
        	for(int col = 0; col<nodes[0].length; col++) {
        		double x = buffer + col*xInc;
        		double y = buffer + row*yInc;
        		double rectWidth = xInc;
        		double rectHeight = yInc;
        		Rectangle2D rect = new Rectangle2D.Double(x, y, rectWidth, rectHeight);
        		graphics2D.setPaint(nodes[row][col].getColor());
        		graphics2D.fill(rect);
        	}
        }
       
        // Draw vertical lines
        graphics2D.setPaint(Color.blue);
        for(int i = 0; i <= nodes[0].length; i++) {
            double x = buffer + i*xInc;
            graphics2D.draw(new Line2D.Double(x, buffer, x, h-buffer));
        }
        
        // Draw horizontal lines.
        for(int i = 0; i <= nodes.length; i++) {
            double y = buffer + i*yInc;
            graphics2D.draw(new Line2D.Double(buffer, y, w-buffer, y));
        }
    }
	
	public JFrame getFrame() {
		return frame;
	}
	
	public void setMaze(Maze maze) {
		this.maze = maze;
	}
	
	public int getDelayInMilliseconds() {
		return delayInMilliseconds;
	}

	public String getMode() {
		return mode;
	}

	public String getGenerationAlgorithm() {
		return generationAlgorithm;
	}

	public String getSolveAlgorithm() {
		return solveAlgorithm;
	}

	public int getMazeSizeMultiplier() {
		return mazeSizeMultiplier;
	}

}