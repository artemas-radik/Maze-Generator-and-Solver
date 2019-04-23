package com.magnesiumm.logic;

import java.util.LinkedList;

import com.magnesiumm.Run;

/**
* This class is responsible for representing a Maze,
* that needs to be solved, by the program. It is used by the MazeSolver
* class as input data on what to solve. It is also stores the 2D array of nodes,
* that represent the maze. It can also print the nodes as a method of troubleshooting.
*
* @author  AJ Radik and Victoria Vigorito
* @version 5.0 
*/
public class Maze {

	/**
	 * A 2D array representation of the nodes of the maze.
	 */
	private Node[][] nodes;
	
	/**
	 * Constructs a new maze.
	 * @param mazeFilePath the file path of the maze to be constructed.
	 */
	public Maze(String mazeFilePath) {
		nodes = Run.getFileOperations().loadNodes(mazeFilePath);
		Run.getGUI().getMazeJPanel().setMaze(this);
		Run.getGUI().getMazeJPanel().repaint();
	}

	public Maze(int rows, int cols) {
		nodes = new Node[rows][cols];
	}
	
	/**
	 * This method is used for troubleshooting and
	 * representing the solved maze as output in the console.
	 * @param endPath the end path, or solution, to be printed
	 * to the maze.
	 */
	public void printSolvedMaze(LinkedList<Node> endPath) {
		for(Node[] nodeArr : nodes) {
			for(Node node : nodeArr) {
				if(endPath.indexOf(node) != -1) {
					
					System.out.print("X  ");
				}
				else if(node.isVisited()) {
					System.out.print("x  ");
				}
				else {
					System.out.print(node + "  ");
				}
			}
			System.out.println();
			System.out.println();
		}
		
	}

	/**
	 * Returns a 2D array representation of the nodes of the maze.
	 * @return The nodes of the maze.
	 */
	public Node[][] getNodes() {
		return nodes;
	}

	/**
	 * Sets the nodes for the Maze.
	 * @param nodeMaze The nodes to be set.
	 */
	public void setNodes(Node[][] nodeMaze) {
		this.nodes = nodeMaze;
	}

}