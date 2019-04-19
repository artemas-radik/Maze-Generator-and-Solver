package com.magnesiumm.logic;

import com.magnesiumm.gui.GUI;

import java.io.IOException;
import java.util.LinkedList;

/**
* This class is responsible for generating mazes
* for the program to solve. This class is not written yet,
* but it will be responsible for generating a maze using a reversed DFS
* type of algorithm. It will be used by the Main class and Control Panel
* if the option to generate a random maze is selected.
*
* @author  AJ Radik and Victoria Vigorito
* @version 5.0 
*/
public class MazeGenerator {
	
	private GUI gui;
	private String filePath;
	private Maze maze;
	private Node[][] nodes;
	private Node startNode;
	public static final int inSafeBounds = 2;
	public static final int inBounds = 1;
	public static final int outOfBounds = 0;
	
	public MazeGenerator(GUI gui, String filePath, int rows, int cols) {
		this.filePath = filePath;
		
		maze = new Maze(filePath, rows, cols);
		nodes = maze.getNodes();
		
		for(int row = 0; row < nodes.length; row++) {
			for(int col = 0; col < nodes[0].length; col++) {
				nodes[row][col] = new Node(Node.wall, row, col);
			}
		}
		
		this.gui = gui;
		this.gui.setMaze(maze);
		startNode = nodes[1][0];
	}
	
	public void DFSgenerate() throws IOException, InterruptedException {
		gui.getFrame().setTitle(GUI.defaultTitle+" - DFS Random Generation");
		DFSgenerateRecursive(startNode, nodes);
		startNode.setState(Node.startNode);
		setEndNode(nodes);
		FileOperations.writeNodes(filePath, nodes);
	}
	
	public void DFSgenerateRecursive(Node current, Node[][] nodes) throws InterruptedException {
		
		current.setCurrentNode(true);
		Node middle = getMiddleNode(current, current.getParent(), nodes);
		middle.setState(Node.pathNode);
		Thread.sleep(gui.getDelayInMilliseconds());
		gui.repaint();
		LinkedList<Node> selection = getNeighbors(current, nodes);
		
		while(selection.size() > 0) {
			int random = (int) (Math.random() * selection.size());
			Node randomNeighbor = selection.get(random);
			randomNeighbor.setParent(current);
			current.setCurrentNode(false);
			current.setState(Node.pathNode);
			DFSgenerateRecursive(randomNeighbor, nodes);
			selection = getNeighbors(current, nodes);
		}
		
		middle.setCurrentNode(true);
		current.setCurrentNode(false);
		current.setState(Node.empty);
		Thread.sleep(gui.getDelayInMilliseconds());
		gui.repaint();
		middle.setCurrentNode(false);
		middle.setState(Node.empty);
	}
	
	public void setEndNode(Node[][] nodes) {
		for(int row = nodes.length-1; row >= 0; row--) {
			for(int col = nodes[0].length-1; col >= 0; col--) {
				Node current = nodes[row][col];
				if(current.getState() == Node.empty) {
					current.setState(Node.endNode);
					return;
				}
			}
		}
	}
	
	public LinkedList<Node> getNeighbors(Node node, Node[][] nodes) {
		LinkedList<Node> neighbors = new LinkedList<Node>();
		int[][][] intNeighbors = { 
				{{ 0, 2 }, {0, 1}}, // TOP 
				{{ 2, 0 }, {1, 0}}, //RIGHT
				{{ 0, -2 }, {0, -1}}, // BOTTOM
				{{ -2, 0 }, {-1, 0}} //LEFT
				};
		for(int[][] direction: intNeighbors) {
			LinkedList<Node> directionalNodes = new LinkedList<Node>();
			
			
			for(int[] individualCoordinate : direction) {
				int newRow = node.getRow() + individualCoordinate[0];
				int newCol = node.getCol() + individualCoordinate[1];
				if(testBounds(newRow, newCol, nodes) > outOfBounds) {
					Node newNode = nodes[newRow][newCol];
					if(newNode.getState() == Node.wall) {
						directionalNodes.add(newNode);
					}
				}
			}
			
			if(directionalNodes.size() == 2) {
				Node far = directionalNodes.get(0);
				int farBounds = testBounds(far.getRow(), far.getCol(), nodes);
				if(farBounds == inSafeBounds) {
					neighbors.add(far);
				}
//				else if(farBounds == inBounds) {
//					Node close = directionalNodes.get(1);
//					System.out.println(close.getCoordinateString());
//					neighbors.add(directionalNodes.get(1));
//				}
			}
//			else if(directionalNodes.size() == 1) {
//				Node test = directionalNodes.get(0);
//				if(testBounds(test.getRow(), test.getCol(), nodes) > onInternalEdge) {
//					neighbors.add(test);
//				}
//
//			}
			
		}
		return neighbors;
	}
	
	public int testBounds(int row, int col, Node[][] nodes) {
		if(row < 0 || row >= nodes.length || col < 0 || col >= nodes[0].length) {
			return outOfBounds;
		}
		else if (row >= 1 && row < nodes.length-1 && col >= 1 && col < nodes[0].length-1){
			return inSafeBounds;
		}
		else {
			return inBounds;
		}
	}
	
	public Node getMiddleNode(Node node1, Node node2, Node[][] nodes) {
		if(node1==null || node2==null) {
			return nodes[1][0];
		}
		
		int row1 = node1.getRow();
		int col1 = node1.getCol();
		int row2 = node2.getRow();
		int col2 = node2.getCol();
		
		int newRow = (row1 + row2) / 2;
		int newCol = (col1 + col2) / 2;
		return nodes[newRow][newCol];
	}
	
	/**
	 * This method gets the GUI object from this class
	 * for other classes to use.
	 * @return GUI This returns the GUI object being used
	 * by the program - used for repainting / updating.
	 */
	public GUI getGUI() {
		return gui;
	}

}