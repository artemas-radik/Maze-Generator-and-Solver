import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.File;

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
public class GUI extends JPanel implements ActionListener, ItemListener, ChangeListener {
	
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
	private String mode = DEMO_MODE;
	
	/**
	 * The generation algorithm set by this GUI.
	 */
	private String generationAlgorithm = GENERATION_ALGORITHM_DFS_RANDOM;
	
	/**
	 * The solving algorithm set by this GUI.
	 */
	private String solveAlgorithm = SOLVE_ALGORITHM_DFS;
	
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
    private JToggleButton go;
    private JSlider delay;
    private JMenuBar menuBar;
    private boolean easterEggTriggered = false;
    
    /**
     * Used to tell paintComponent that it is reset time.
     */
    private boolean reset = false;
    
    public static final String DEMO_MODE = "DEMO_MODE";
    public static final String CUSTOM_MODE = "CUSTOM_MODE";
    public static final String GENERATION_ALGORITHM_DFS_RANDOM = "GENERATION_ALGORITHM_DFS_RANDOM";
    public static final String SOLVE_ALGORITHM_DFS = "SOLVE_ALGORITHM_DFS";
    public static final String SOLVE_ALGORITHM_BFS = "SOLVE_ALGORITHM_BFS";
    
    public static final String SLIDER_DELAY = "SLIDER_DELAY";
    public static final String SLIDER_MAZE_SIZE_MULTIPLIER = "SLIDER_MAZE_SIZE_MULTIPLIER";
    public static final String BUTTON_REAL_TIME = "BUTTON_REAL_TIME";
    public static final String BUTTON_RESET = "BUTTON_RESET";
    public static final String BUTTON_NAME_GO_STOP = "GO_STOP";
    public static final String BUTTON_STOP = "Stop";
    public static final String BUTTON_GO = "Go";
    
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
    
    /**
     * Draws the menu at the top of the window. Should only be called once.
     */
    public void drawMenu() {
    	menuBar = new JMenuBar();

        JMenu options = new JMenu("Options");
        menuBar.add(options);
        delay = new JSlider(JSlider.HORIZONTAL, 1, 200, 200);
        delay.addChangeListener(this);
        delay.setName(SLIDER_DELAY);
        JLabel delayLabel = new JLabel("Speed");
        options.add(delayLabel);
        options.add(delay);
        JMenuItem realTimeButton = new JMenuItem("Real Time (Seizure Warning)");
        realTimeButton.setName(BUTTON_REAL_TIME);
        realTimeButton.addActionListener(this);
        options.add(realTimeButton);
        options.addSeparator();
        JMenuItem resetButton = new JMenuItem("Reset");
        resetButton.setName(BUTTON_RESET);
        resetButton.addActionListener(this);
        options.add(resetButton);
        
        JMenu mode = new JMenu("Mode");
        menuBar.add(mode);
        ButtonGroup buttonGroupMode = new ButtonGroup();
        JRadioButtonMenuItem demoMode = new JRadioButtonMenuItem("Demo Mode", true);
        demoMode.setName(DEMO_MODE);
        demoMode.addItemListener(this);
        buttonGroupMode.add(demoMode);
        mode.add(demoMode);
        JRadioButtonMenuItem customMode = new JRadioButtonMenuItem("Custom Mode", false);
        customMode.setName(CUSTOM_MODE);
        customMode.addItemListener(this);
        buttonGroupMode.add(customMode);
        mode.add(customMode);
        
        JMenu maze = new JMenu("Maze");
        menuBar.add(maze);
        JSlider mazeSizeMultiplierSlider = new JSlider(JSlider.HORIZONTAL, 1, 20, mazeSizeMultiplier);
        mazeSizeMultiplierSlider.addChangeListener(this);
        mazeSizeMultiplierSlider.setName(SLIDER_MAZE_SIZE_MULTIPLIER);
        JLabel mazeSizeMultiplierLabel = new JLabel("Maze Size");
        maze.add(mazeSizeMultiplierLabel);
        maze.add(mazeSizeMultiplierSlider);
        
        JMenu generate = new JMenu("Generate");
        menuBar.add(generate);
        ButtonGroup buttonGroupGenerate = new ButtonGroup();
        JRadioButtonMenuItem dfsRandomGenerate = new JRadioButtonMenuItem("DFS Random Generation (Depth-First-Search)", true);
        dfsRandomGenerate.setName(GENERATION_ALGORITHM_DFS_RANDOM);
        dfsRandomGenerate.addItemListener(this);
        buttonGroupGenerate.add(dfsRandomGenerate);
        generate.add(dfsRandomGenerate);
        
        JMenu solve = new JMenu("Solve");
        menuBar.add(solve);
        ButtonGroup buttonGroupSolve = new ButtonGroup();
        JRadioButtonMenuItem dfs = new JRadioButtonMenuItem("DFS (Depth-First-Search)", true);
        dfs.setName(SOLVE_ALGORITHM_DFS);
        dfs.addItemListener(this);
        buttonGroupSolve.add(dfs);
        solve.add(dfs);
        JRadioButtonMenuItem bfs = new JRadioButtonMenuItem("BFS (Breadth-First-Search)", false);
        bfs.setName(SOLVE_ALGORITHM_BFS);
        bfs.addItemListener(this);
        buttonGroupSolve.add(bfs);
        solve.add(bfs);
        
        go = new JToggleButton(BUTTON_GO);
        go.setName(BUTTON_NAME_GO_STOP);
        go.addItemListener(this);
        go.registerKeyboardAction(go.getActionForKeyStroke(
                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true)),
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true),
                JComponent.WHEN_FOCUSED);
        menuBar.add(go);
        
        frame.setJMenuBar(menuBar);
    }
    
    
    /**
     * Called when the display space of the frame needs to be reset.
     */
    public void reset() {
    	setEnabledNonVitalMenus(true);
    	mainThread.suspend();
    	mainThread.stop();
    	maze = null;
    	reset = true;
    	String state = go.getText();
    	if(state.equals(BUTTON_STOP)) {
    		go.doClick();
    	}
    	repaint();
    	mainThread = new Main();
    	mainThread.start();
    	mainThread.suspend();
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		AbstractButton item = ((AbstractButton) e.getSource());
		String name = item.getName();
		
		if(name.equals(BUTTON_RESET)) {
		    reset();
		}
		
		else if(name.equals(BUTTON_REAL_TIME)) {
			
			//easter egg
			if(!easterEggTriggered) {
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
		    	delayInMilliseconds = delay.getMaximum()+1 - delay.getValue();
		    }
		}
	}

	public void setEnabledNonVitalMenus(boolean enable) {
		for(int x = 1; x < menuBar.getMenuCount()-1; x++) {
			menuBar.getMenu(x).setEnabled(enable);
		}
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		AbstractButton item = ((AbstractButton) e.getSource());
		String name = item.getName();
		int state = e.getStateChange();
		
		if(name.equals(BUTTON_NAME_GO_STOP)) {
			if(state == ItemEvent.SELECTED) {
				//Go clicked
				setEnabledNonVitalMenus(false);
				item.setText(BUTTON_STOP);
				mainThread.resume();
			}
			else if(state == ItemEvent.DESELECTED) {
				//Stop clicked
				item.setText(BUTTON_GO);
				mainThread.suspend();
			}
		}
		
		else if(name.equals(DEMO_MODE)) {
			if(state == ItemEvent.SELECTED) {
				this.mode = DEMO_MODE;
			}
		}
		
		else if(name.equals(CUSTOM_MODE)) {
			if(state == ItemEvent.SELECTED) {
				this.mode = CUSTOM_MODE;
			}
		}
		
		else if(name.equals(GENERATION_ALGORITHM_DFS_RANDOM)) {
			if(state == ItemEvent.SELECTED) {
				this.generationAlgorithm = GENERATION_ALGORITHM_DFS_RANDOM;
			}
		}
		
		else if(name.equals(SOLVE_ALGORITHM_DFS)) {
			if(state == ItemEvent.SELECTED) {
				this.solveAlgorithm = SOLVE_ALGORITHM_DFS;
			}
		}
		
		else if(name.equals(SOLVE_ALGORITHM_BFS)) {
			if(state == ItemEvent.SELECTED) {
				this.solveAlgorithm = SOLVE_ALGORITHM_BFS;
			}
		}
		
		
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider item = ((JSlider) e.getSource());
		String name = item.getName();
		
		if(name.equals(SLIDER_DELAY)) {
			delayInMilliseconds = item.getMaximum()+1 - item.getValue();
		}
		
		else if(name.equals(SLIDER_MAZE_SIZE_MULTIPLIER)) {
			mazeSizeMultiplier = item.getValue();
			System.out.println("cke");
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