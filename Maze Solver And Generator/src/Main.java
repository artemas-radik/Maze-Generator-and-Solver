import java.io.IOException;
import java.util.LinkedList;

/**
* This main class is responsible for using all the other classes
* in order to solve a maze. Being the main class, it is the class that starts the program.
* The MazeSolver class often accesses an object of this class in order to repaint the GUI.
* This class is also instantiated as a Thread in the control panel class.
* This is required is required because the java GUI Event Dispatch Thread needs to be free
* in order to handle scheduled repaint events.
*
* @author  AJ Radik and Victoria Vigorito
* @version 5.0 
*/
public class Main extends Thread{
	
	private static GUI gui;
	
	public static final String[] algorithms = {"BFS (Breadth-First-Search)", "DFS (Depth-First-Search)"};
	
	 /**
	   * This is the main method of the program, it starts the Control Panel.
	   * @param args Unused.
	   * @throws InterruptedException because of sleep times.
	   * @throws IOException file I/O
	   * @see InterruptedException
	   */
	public static void main(String[] args) throws InterruptedException, IOException {
		
		gui = new GUI();
		
		//ControlPanel.runControlPanel();;
	}
	
	public void demoMode() throws IOException, InterruptedException {
		while(true) {
			String filePath = "generatedMaze.txt";
			MazeGenerator mazeGenerator = new MazeGenerator(gui, filePath, gui.getMazeSizeMultiplier() * 10 + 1, gui.getMazeSizeMultiplier() * 15 +1);
			mazeGenerator.DFSgenerate();
			
			Maze maze = new Maze(filePath);
			MazeSolver mazeSolver = new MazeSolver(gui, maze);
			activateWithRandomAlgorithm(mazeSolver);
		}
	}
	
	public void activateWithRandomAlgorithm(MazeSolver mazeSolver) throws InterruptedException {
		int random = (int) (Math.random() * algorithms.length);
		String algorithm = algorithms[random];
		
		if(algorithm.equals("DFS (Depth-First-Search)")) {
			mazeSolver.DFS();
		}
		else if(algorithm.equals("BFS (Breadth-First-Search)")) {
			mazeSolver.BFS();
		}
	}
	
	/**
	 * This method is inherited from Thread.
	 * It is called when a new thread of Main
	 * is started by the Control Panel. 
	 * Multithreading is required because the java 
	 * GUI Event Dispatch Thread needs to be free
	 * in order to handle scheduled repaint events.
	 */
	@Override
	public void run() {
		try {
			
			if(gui.getMode().equals("Demo Mode")) {
				demoMode();
			}
			
			else if(gui.getMode().equals("Custom Mode")) {
				startMazeSolving();
			}
			
			//startMazeSolving();
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is called by a new thread of Main once
	 * the user hits submit on the Control Panel.
	 * This method is responsible for beginning the
	 * maze solving process.
	 * @throws IOException 
	 * @throws InterruptedException because of sleep times.
	 * @see InterruptedException
	 */
	public void startMazeSolving() throws IOException, InterruptedException {
		while(true) {
			String generationAlgorithm = gui.getGenerationAlgorithm();
			String solveAlgorithm = gui.getSolveAlgorithm();
			String filePath = "generatedMaze.txt";
			MazeGenerator mazeGenerator = new MazeGenerator(gui, filePath, gui.getMazeSizeMultiplier() * 10 + 1, gui.getMazeSizeMultiplier() * 18 +1);
			
			if(generationAlgorithm.equals("DFS Random Generation (Depth-First-Search)")) {
				mazeGenerator.DFSgenerate();
			}
			
			Maze maze = new Maze(filePath);
			MazeSolver mazeSolver = new MazeSolver(gui, maze);
			
			if(solveAlgorithm.equals("DFS (Depth-First-Search)")) {
				mazeSolver.DFS();
			}
			
			else if(solveAlgorithm.equals("BFS (Breadth-First-Search)")) {
				mazeSolver.BFS();
			}
			
		}
	}

}
