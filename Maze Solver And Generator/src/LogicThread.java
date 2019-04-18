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
public class LogicThread extends Thread{
	
	
	public static final SolveAlgorithm[] algorithms = {SolveAlgorithm.DFS, SolveAlgorithm.BFS};
	
	 /**
	   * This is the main method of the program, it starts the Control Panel.
	   * @param args Unused.
	   * @throws InterruptedException because of sleep times.
	   * @throws IOException file I/O
	   * @see InterruptedException
	   */
	
	public void demoMode() throws IOException, InterruptedException {
		while(true) {
			String filePath = "generatedMaze.txt";
			MazeGenerator mazeGenerator = new MazeGenerator(Run.getGUI(), filePath, Run.getGUI().getMazeSizeMultiplier() * 10 + 1, Run.getGUI().getMazeSizeMultiplier() * 15 +1);
			mazeGenerator.DFSgenerate();
			Maze maze = new Maze(filePath);
			MazeSolver mazeSolver = new MazeSolver(Run.getGUI(), maze);
			activateWithRandomAlgorithm(mazeSolver);
		}
	}
	
	public void activateWithRandomAlgorithm(MazeSolver mazeSolver) throws InterruptedException {
		int random = (int) (Math.random() * algorithms.length);
		SolveAlgorithm solveAlgorithm = algorithms[random];
		
		switch(solveAlgorithm) {
		
			case DFS:
				mazeSolver.DFS();
				break;
			
			case BFS:
				mazeSolver.BFS();
				break;
				
			default:
				break;
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
			Mode mode = Run.getGUI().getMode();
			
			switch(mode) {
			
				case Demo_Mode:
					demoMode();
					break;
				
				case Custom_Mode:
					startMazeSolving();
					break;
				
			default:
				break;
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
			GenerationAlgorithm generationAlgorithm = Run.getGUI().getGenerationAlgorithm();
			SolveAlgorithm solveAlgorithm = Run.getGUI().getSolveAlgorithm();
			String filePath = "generatedMaze.txt";
			MazeGenerator mazeGenerator = new MazeGenerator(Run.getGUI(), filePath, Run.getGUI().getMazeSizeMultiplier() * 10 + 1, Run.getGUI().getMazeSizeMultiplier() * 15 +1);
			
			switch(generationAlgorithm) {
			
				case DFS_random:
					mazeGenerator.DFSgenerate();
					break;
				
				default:
					break;
			}
			
			Maze maze = new Maze(filePath);
			MazeSolver mazeSolver = new MazeSolver(Run.getGUI(), maze);
			
			switch(solveAlgorithm) {
			
				case DFS:
					mazeSolver.DFS();
					break;
				
				case BFS:
					mazeSolver.BFS();
					break;
					
				default:
					break;
			}
			
		}
	}

}
