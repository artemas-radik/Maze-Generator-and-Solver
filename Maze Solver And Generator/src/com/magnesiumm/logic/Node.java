package com.magnesiumm.logic;

import java.awt.Color;

/**
* This class is responsible for representing
* a node, or a square, of the maze. It holds static final constants, such
* the int states of a node and their meanings. Its instance data includes a state of the node,
* a row and column location of the node, a parent node, and a boolean isVisited flag so that 
* there are no infinite loops or inefficiencies in the maze-solving algorithms.
* This class also determines which color to paint each node, based on various data points. 
*
* @author  AJ Radik and Victoria Vigorito
* @version 6.0 
*/
public class Node {

	//States
	
	/**
	 * value={@value empty}; 
	 * This represents the state of an empty square/node as an int.
	 */
	public static final int empty= 0;
	
	/**
	 * value={@value wall}; 
	 * This represents the state of a wall square/node as an int.
	 */
	public static final int wall= 1;
	
	/**
	 * value={@value startNode};
	 * This represents the state of an entry point square/node as an int.
	 */
	public static final int startNode= 2;
	
	/**
	 * value={@value endNode};
	 * This represents the state of an exit point square/node as an int.
	 */
	public static final int endNode= 3;
	
	/**
	 * value={@value pathNode}; 
	 * This represents the state of a path square/node as an int.
	 * This is used at the end of the program when a path must 
	 * be backtracked in order to find a solution. Nodes on the solution
	 * path are set to this state.
	 */
	public static final int pathNode= 4;
	
	private boolean currentNode = false;
	
	/**
	 * The row coordinate of the node.
	 */
	private int row;
	
	/**
	 * The column coordinate of the node.
	 */
	private int col;
	
	/**
	 * The current state of the node. 
	 * See commenting on states for further documentation.
	 */
	private int state;
	
	/**
	 * The parent node of the current node.
	 * Parents are set by the maze-solving algorithms,
	 * and later used in order to backtrack a solution.
	 */
	private Node parent;
	
	/**
	 * This boolean is used as a flag by the maze-solving
	 * algorithms, in order to denote a node that has
	 * already been visited by the algorithm. This is used
	 * to avoid infinite loops as well as inefficiencies.
	 */
	private boolean isVisited;
	
	/**
	 * This constructs a new node. Used when
	 * loading data from files into the Maze class.
	 * NOTE: row/column data is not necessary but used
	 * to more easily print the end path at the end 
	 * path at the end of the program.
	 * @param state The state to set the node as.
	 * States are static constants in the Node class.
	 * States include: wall, empty, etc.
	 * @param row The row coordinate of the current node.
	 * @param col The column coordinate of the current node.
	 */
	public Node(int state, int row, int col) {
		setState(state);
		setRow(row);
		setCol(col);
	}
	
	/**
	 * This method is used by the GUI in order to decide
	 * what color it should paint each node.
	 * @return Color The color that this node should be represented by,
	 * this color is generally determined by the state of the node, etc.
	 */
	public Color getColor() {
		
		if(state==startNode) {
			return Color.cyan;
		}
		else if(state==endNode) {
			return Color.green;
		}
		else if(currentNode) {
			return Color.yellow;
		}
		else if(state==pathNode) {
			return Color.red;
		}
		else if(isVisited) {
			return Color.gray;
		}
		else if(state==empty) {
			return Color.white;
		}
		else if(state==wall) {
			return Color.black;
		}
		else {
			return null;
		}
	}
	
	/**
	 * Gets the state of the current node.
	 * @return int The state of the current node.
	 */
	public int getState() {
		return state;
	}
	
	/**
	 * Sets the state of the current node.
	 * @param state The state to be set.
	 */
	public void setState(int state) {
		this.state = state;
	}
	
	/**
	 * Gets the parent of the current node.
	 * Used to backtrack a solution at the 
	 * end of the program.
	 * @return Node The parent node of the current node.
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * Sets the parent of the current node.
	 * Used to backtrack a solution at the 
	 * end of the program.
	 * @param parent The node to be set as the parent.
	 */
	public void setParent(Node parent) {
		this.parent = parent;
	}

	/**
	 * Returns a string representation of the node,
	 * based on its state.
	 * @return String A string representation of this node,
	 * based on its state.
	 */
	public String toString() {
		return "" + this.state;
	}

	/**
	 * Tells whether the node has been visited by a 
	 * maze-solving algorithm yet.
	 * @return boolean Whether or not the node has been visited by
	 * a maze-solving algorithm
	 */
	public boolean isVisited() {
		return isVisited;
	}

	/**
	 * Used to denote the current node as visited,
	 * by a maze-solving algorithm.
	 * @param isVisited Whether or not the node should be set
	 * as visited.
	 */
	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}
	
	/**
	 * Gets the row coordinate of the current node in the maze.
	 * @return int The row coordinate of the current node in the maze.
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * Sets the row coordinate of the current node in the maze.
	 * @param row The row coordinate to be set.
	 */
	public void setRow(int row) {
		this.row = row;
	}
	
	/**
	 * Gets the column coordinate of the current node in the maze.
	 * @return int The column coordinate of the current node in the maze.
	 */
	public int getCol() {
		return col;
	}
	
	/**
	 * Sets the column coordinate of the current node in the maze.
	 * @param col The column coordinate to be set.
	 */
	public void setCol(int col) {
		this.col = col;
	}
	
	/**
	 * Represents this nodes coordinate as a String.
	 * Used when printing the solution path at the end
	 * of the program, and for troubleshooting.
	 * @return String A string representation of
	 * this nodes row and column coordinates.
	 */
	public String getCoordinateString() {
		return "Coordinate [row=" + row + ", col=" + col + "]";
	}

	public boolean isCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(boolean currentNode) {
		this.currentNode = currentNode;
	}
	
}