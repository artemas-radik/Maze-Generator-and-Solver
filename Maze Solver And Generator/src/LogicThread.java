import java.awt.Dimension;
import java.io.IOException;

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
	
	public static final SolveAlgorithm[] algorithms = SolveAlgorithm.values();
	public static final String generatedMazeFilePath = "generatedMaze.txt";
	
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
					customMode();
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
	
	public void demoMode() throws IOException, InterruptedException {
		while(true) {
			Dimension dimension = getDimension();
			MazeGenerator mazeGenerator = new MazeGenerator(Run.getGUI(), generatedMazeFilePath, (int) dimension.getHeight(), (int) dimension.getWidth());
			mazeGenerator.DFSgenerate();
			Maze maze = new Maze(generatedMazeFilePath);
			MazeSolver mazeSolver = new MazeSolver(Run.getGUI(), maze);
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
	public void customMode() throws IOException, InterruptedException {
		while(true) {
			GenerationAlgorithm generationAlgorithm = Run.getGUI().getGenerationAlgorithm();
			SolveAlgorithm solveAlgorithm = Run.getGUI().getSolveAlgorithm();
			Dimension dimension = getDimension();
			
			MazeGenerator mazeGenerator = new MazeGenerator(Run.getGUI(), generatedMazeFilePath, (int) dimension.getHeight(), (int) dimension.getWidth());
			
			switch(generationAlgorithm) {
			
				case DFS_random:
					mazeGenerator.DFSgenerate();
					break;
				
				default:
					break;
			}
			
			Maze maze = new Maze(generatedMazeFilePath);
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
	
	public Dimension getDimension() {
		double guiWidth = Run.getGUI().getWidth() - 2*GUI.buffer;
		double guiHeight = Run.getGUI().getHeight()- 2*GUI.buffer;
		double guiWidthToGuiHeightRatio = guiWidth / guiHeight;
		
		int rowsAspectRatio = 10;
		int colsAspectRatio = (int) Math.round(rowsAspectRatio * guiWidthToGuiHeightRatio);
		int rows = Run.getGUI().getMazeSizeMultiplier() * rowsAspectRatio + 1;
		int cols = Run.getGUI().getMazeSizeMultiplier() * colsAspectRatio + 1;
		//must be odd
		if(cols % 2 == 0) {
			cols--;
		}
		return new Dimension(cols, rows);
	}

}