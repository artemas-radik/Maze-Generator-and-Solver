import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*; 
import javax.swing.*; 

/**
* This class is responsible for displaying the
* options panel at the beginning of the program. This class's
* static method is run in order to launch the control panel.
* Data entered in the control panel is stored as static data of this class.
* This data can later be accessed by get methods, for instance in the Main class.
* This class also creates an object of Main as a new thread.
* This is required is required because the java GUI Event Dispatch Thread needs to be free
* in order to handle scheduled repaint events.
*
* @author  AJ Radik and Victoria Vigorito
* @version 5.0 
*/
public class ControlPanel extends JFrame implements ActionListener { 
	/**
	 * The text field in which the delay time is inputed.
	 */
	private static JTextField delayText; 
	
	/**
	 * The text field in which a custom maze file path may be inputed.
	 */
	private static JTextField customMazeText;
	
	/**
	 * The label which shows the currently set delay time in milliseconds.
	 */
	private static JLabel delayLabelEnd;
	
	/**
	 * The label which shows the currently set algorithm.
	 */
	private static JLabel algorithmLabelEnd;
	
	/**
	 * The label which shows the currently set maze file path.
	 */
	private static JLabel mazeLabelEnd;
	
	/**
	 * The frame which is the Control Panel.
	 */
	private static JFrame frame;
	
	/**
	 * The String which represents the currently set algorithm.
	 */
	//private static String algorithm = "DFS";
	
	/**
	 * The String which represents the currently set maze file path.
	 */
	//private static String mazeFilePath = "maze3.txt";
	
	/**
	 * The long which represents the currently set delay in milliseconds.
	 */
	//private static long delayInMilliseconds = 1;
	
	/**
	 * This method is called by Main when the program begins,
	 * it is responsible for displaying and running the Control Panel.
	 */
	public static void runControlPanel() 
    { 
        // create a new frame to stor text field and button 
        frame = new JFrame("Control Panel"); 
        JPanel outer = new JPanel(); 
        JPanel algorithmPane = new JPanel();
        JPanel delayPane = new JPanel();
        JPanel mazePane = new JPanel();
        JPanel customMazePane = new JPanel();
        JPanel finalSubmitPane = new JPanel();
        ControlPanel controlPanel = new ControlPanel();
        
        JLabel algorithmLabel = new JLabel("Select an algorithm: ");
        algorithmPane.add(algorithmLabel);
        JButton bfs = new JButton("BFS");
        bfs.addActionListener(controlPanel);
        algorithmPane.add(bfs);
        JButton dfs = new JButton("DFS");
        dfs.addActionListener(controlPanel);
        algorithmPane.add(dfs);
        algorithmLabelEnd = new JLabel("Algorithm set to DFS");
        algorithmPane.add(algorithmLabelEnd);
        
        JLabel mazeLabel = new JLabel("Please select a maze: ");
        mazePane.add(mazeLabel);
        JButton maze1 = new JButton("maze1.txt");
        maze1.addActionListener(controlPanel);
        mazePane.add(maze1);
        JButton maze2 = new JButton("maze2.txt");
        maze2.addActionListener(controlPanel);
        mazePane.add(maze2);
        JButton maze3 = new JButton("maze3.txt");
        maze3.addActionListener(controlPanel);
        mazePane.add(maze3);
        mazeLabelEnd = new JLabel("Maze File Path set to \"maze3.txt\"");
        mazePane.add(mazeLabelEnd);
        
        JLabel customMazeLabel = new JLabel("Please enter a custom maze file path (optional): ");
        customMazePane.add(customMazeLabel);
        customMazeText = new JTextField(26);
        customMazePane.add(customMazeText);
        JButton submitCustomMaze = new JButton("Submit Custom Maze");
        submitCustomMaze.addActionListener(controlPanel);
        customMazePane.add(submitCustomMaze);
        
        JLabel delayLabel = new JLabel("Please enter a delay amount in milliseconds: ");
        delayPane.add(delayLabel);
        delayText = new JTextField(6);
        delayPane.add(delayText);
        JButton submitMilliseconds = new JButton("Submit Milliseconds");
        submitMilliseconds.addActionListener(controlPanel);
        delayPane.add(submitMilliseconds);
        delayLabelEnd = new JLabel("Delay set to 1 millisecond(s)");
        delayPane.add(delayLabelEnd);
        
        JButton finalSubmit = new JButton("Submit");
        finalSubmit.addActionListener(controlPanel);
        finalSubmitPane.add(finalSubmit);
        outer.add(algorithmPane);
        outer.add(mazePane);
        outer.add(customMazePane);
        outer.add(delayPane);
        outer.add(finalSubmitPane);
        
        frame.add(outer);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation( (int) (0.3 * screenSize.width), (int) (0.3 * screenSize.height));
        frame.setSize(825, 300); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
    } 
  
	/**
	 * This method is called when an action is performed,
	 * for example a button is clicked.
	 * @param actionEvent the action event which is performed.
	 */
	@Override
    public void actionPerformed(ActionEvent actionEvent) 
    { 
    	Thread main = new Main(); //?
        String command = actionEvent.getActionCommand(); 
        if (command.equals("BFS")) { 
           // algorithm = "BFS";
            algorithmLabelEnd.setText("Algorithm set to BFS");
        }
        else if(command.equals("DFS")) {
        	//algorithm = "DFS";
        	algorithmLabelEnd.setText("Algorithm set to DFS");
        }
        else if(command.equals("maze1.txt")) {
        	//mazeFilePath = "maze1.txt";
        	mazeLabelEnd.setText("Maze File Path set to \"maze1.txt\"");
        }
        else if(command.equals("maze2.txt")) {
        	//mazeFilePath = "maze2.txt";
        	mazeLabelEnd.setText("Maze File Path set to \"maze2.txt\"");
        }
        else if(command.equals("maze3.txt")) {
        	//mazeFilePath = "maze3.txt";
        	mazeLabelEnd.setText("Maze File Path set to \"maze3.txt\"");
        }
        else if(command.equals("Submit Custom Maze")) {
        	//mazeFilePath = customMazeText.getText();
        //	mazeLabelEnd.setText("Maze File Path set to " + "\"" + mazeFilePath + "\"");
        }
        else if(command.equals("Submit Milliseconds")) {
        	String delayString = delayText.getText();
        	if(delayString.equals("")) {

        	}
        	else {
        		long delayLong = Long.parseLong(delayString);
            	//delayInMilliseconds = delayLong;
            	delayLabelEnd.setText("Delay set to " + delayLong + " milliseconds.");
        	}
        }
        else if(command.equals("Submit")) {
        	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        	frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        	main.start();
        }
       
    } 
    
    /**
     * Used to get the algorithm that was selected by the user in the control panel.
     * @return String The user selected algorithm as a String.
     */
  //  public static String getAlgorithm() {
	//	return algorithm;
	//}

    /**
     * Used to get the maze file path that was selected by the user in the control panel.
     * @return String The user selected maze file path.
     */
//	public static String getMazeFilePath() {
	//	return mazeFilePath;
	//}

	/**
	 * Used to get the delay in milliseconds that was specified by the user in the control panel.
	 * @return long The user specified delay in milliseconds.
	 */
	//public static long getDelayInMilliseconds() {
	//	return delayInMilliseconds;
	//}

} 
