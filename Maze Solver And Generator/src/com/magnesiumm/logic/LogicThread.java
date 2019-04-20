package com.magnesiumm.logic;

import com.magnesiumm.Run;
import com.magnesiumm.configurationData.*;
import com.magnesiumm.gui.GUI;

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
					GenerationAlgorithm[] generationAlgorithms = GenerationAlgorithm.values();
					SolveAlgorithm[] solveAlgorithms = SolveAlgorithm.values();
					while(true) {
						GenerationAlgorithm randomGenerationAlgorithm = generationAlgorithms[(int) (Math.random() * generationAlgorithms.length)];
						SolveAlgorithm randomSolveAlgorithm = solveAlgorithms[(int) (Math.random() * solveAlgorithms.length)];
						startMazeSolving(randomGenerationAlgorithm, randomSolveAlgorithm, generatedMazeFilePath);
					}
				
				case Custom_Mode:
					GUI gui = Run.getGUI();
					while(true) {
						startMazeSolving(gui.getGenerationAlgorithm(), gui.getSolveAlgorithm(), generatedMazeFilePath);
					}
				
			default:
				break;
			}			
			
			//startMazeSolving();
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startMazeSolving(GenerationAlgorithm generationAlgorithm, SolveAlgorithm solveAlgorithm, String mazeFilePath) throws IOException, InterruptedException {
		Dimension dimension = getDimension();
		Maze maze;
		MazeSolver mazeSolver;
		
		switch(mazeFilePath) {
			
			case generatedMazeFilePath:
				MazeGenerator mazeGenerator = new MazeGenerator(Run.getGUI(), generatedMazeFilePath, (int) dimension.getHeight(), (int) dimension.getWidth());
				switch(generationAlgorithm) {
					
					case DFS_random:
						mazeGenerator.DFSgenerate();
						break;
					
					default:
						break;
				
				}
				maze = new Maze(generatedMazeFilePath);
				break;
				
			default:
				maze = new Maze(mazeFilePath);
				break;
		
		}	
		
		mazeSolver = new MazeSolver(Run.getGUI(), maze);
		
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