package com.magnesiumm.logic;

import java.util.LinkedList;

import com.magnesiumm.Run;
import com.magnesiumm.GUI.GUI;

/**
* This class is responsible for solving the maze, 
* it holds all of the maze-solving and graph theory algorithms.
* It is constructed by the Main class, and is used to solve 
* a maze via various algorithms, such as a version of DFS (Depth-First-Search)
* and a version of BFS (Breadth-First-Search). It can also backtrack a solution
* by backtracking the parent node of each current node, from the end node.
* This class contains several helper methods as well.
*
* @author  AJ Radik and Victoria Vigorito
* @version 5.0 
*/
public class MazeSolver {

	/**
	 * The main class of the program. 
	 * This is used in order to access the GUI, for repaints. 
	 * NOTE: change GUI to static?
	 */
	private Maze maze;
	
	/**
	 * Constructs a new maze solver.
	 * @param main The main class of the program.
	 * The maze solver relies on a main class in order
	 * to access a GUI that can be repainted as the 
	 * algorithms work.
	 */
	public MazeSolver(GUI gui, Maze maze) {
		this.maze = maze;
		Run.getGUI().getMazeJPanel().setMaze(this.maze);
	}
	
	/**
	 * This method is called in order to solve a maze using a version
	 * of the DFS (Depth-First-Search) algorithm.
	 * <br> Thorough algorithm description: <br>
	 * 1. Locate the start node, also known as the entry node. <br>
	 * 2. The recursive part of the algorithm begins here, and is called on the start node. <br>
	 * 3. GUI is refreshed and timed delay is triggered in order for algorithm visualization. <br>
	 * 4. If the current node is the exit node, return the exit node. Otherwise continue. <br>
	 * 5. Set the current node as visited, in order to avoid infinite loops and inefficiencies. <br>
	 * 6. Get all valid, immediate neighbors in the up, down, left, and right directions, as a queue. <br>
	 * 7. Poll a neighbor from the queue. <br>
	 * 8. Set the parent node of this neighbor as the current node. <br>
	 * 9. Set a solution variable as the recursive algorithm performed on this neighbor. (Repeat from step 2) <br>
	 * 10. If the solution is not null, (if the base case has been reached), then return this solution. <br>
	 * 11. Otherwise, continue polling neighbors from the queue until there are no more neighbors to explore. (Step 7) <br>
	 * 12. Once there are no more neighbors, return null as a solution. <br>
	 * 13. This part is no longer recursive. <br>
	 * 14. Once parent nodes are set, solution can be backtracked. See getResultPath() method for further documentation. <br>
	 * 15. Done! <br>
	 * @param maze The maze which is to be solved.
	 * @return LinkedList<Node> This is the end path,
	 * also known as the solution, to the maze.
	 * @throws InterruptedException because of sleep times.
	 * @see InterruptedException
	 */
	public LinkedList<Node> DFS() throws InterruptedException {
		Run.getGUI().getFrame().setTitle(GUI.defaultTitle+" - DFS");
		Node startNode = getStartNode(maze.getNodes());
		DFSrecursive(startNode, maze.getNodes()); // Return of node not needed?
		return getResultPath();
	}
	
	/**
	 * This is a helper method to the DFS method. This method is called 
	 * recursively and is the actual algorithm part to the DFS method.
	 * @param current The current node which the DFS algorithm is exploring.
	 * @param nodes The 2D array of nodes which the DFS algorithm is exploring.
	 * @return Node The next node to be explored. If the method is on the end node,
	 * the end node is returned.
	 * @throws InterruptedException because of sleep times.
	 * @see InterruptedException
	 */
	public Node DFSrecursive(Node current, Node[][] nodes) throws InterruptedException {
		
		current.setCurrentNode(true);
		Run.getGUI().getMazeJPanel().repaint();
		Thread.sleep(Run.getGUI().getDelayInMilliseconds());
		
		if(current.getState() == Node.endNode) {
			return current;
		}
		
		current.setVisited(true);
		
		LinkedList<Node> neighbors = getNeighbors(current, nodes);
		while(neighbors.size() != 0) {
			Node currentNeighbor = neighbors.poll();
			currentNeighbor.setParent(current);
			current.setCurrentNode(false);
			Node potentialSolution = DFSrecursive(currentNeighbor, nodes);
			if(potentialSolution != null) {
				return potentialSolution;
			}
		}	
		current.setCurrentNode(false);
			
		return null;
	}
	
	/**
	 * This method is called in order to solve a maze using a version
	 * of the BFS (Breadth-First-Search) algorithm.
	 * <br> Thorough algorithm description: <br>
	 * 1. Form a queue of nodes to be explored. <br>
	 * 2. Locate the start node, also known as the entry node. <br>
	 * 3. Add the start node to the queue for iterative exploration. <br>
	 * 4. Repeat the following steps until the queue is empty: <br>
	 * 5. GUI is refreshed and timed delay is triggered in order for algorithm visualization. <br>
	 * 6. Poll a node from the queue for exploration. Set this as the current node. <br>
	 * 7. If the polled node is the end node, return the backtracked solution. See getResultPath() method for further documentation. <br>
	 * 8. Otherwise, get all valid, immediate neighbors in the up, down, left, and right directions, as another queue. <br>
	 * 9. Poll a neighbor. <br>
	 * 10. Set the parent of this neighbor as the current node. <br>
	 * 11. Add this neighbor to the original queue. (Step 1) <br>
	 * 12. Repeat from step 9 until there are no neighbors left to be added to the original queue. <br>
	 * 13. Set the current node as visited, in order to avoid infinite loops and inefficiencies. <br>
	 * 14. Repeat from step 5 until no nodes remain in the queue, or a solution is found. <br>
	 * 15. Return null if no nodes remain and no solution is found. <br>
	 * 16. Done! <br>
	 * @param maze The maze which is to be solved.
	 * @return LinkedList<Node> This is the end path,
	 * also known as the solution, to the maze.
	 * @throws InterruptedException because of sleep times.
	 * @see InterruptedException
	 */
	public LinkedList<Node> BFS() throws InterruptedException {
		Run.getGUI().getFrame().setTitle(GUI.defaultTitle+" - BFS");
		Node[][] nodes = maze.getNodes();
		LinkedList<Node> queue = new LinkedList<Node>();
		Node startNode = getStartNode(nodes);
		
		queue.add(startNode);
		while(queue.size() != 0) {
			Node current = queue.poll();
			current.setCurrentNode(true);
			
			Run.getGUI().getMazeJPanel().repaint();
			Thread.sleep(Run.getGUI().getDelayInMilliseconds());
			
			if(current.getState() == Node.endNode) {
				return getResultPath();
			}
			LinkedList<Node> neighbors = getNeighbors(current, nodes);
			while(neighbors.size() != 0) {
				Node currentNeighbor = neighbors.poll();
				currentNeighbor.setParent(current);
				queue.add(currentNeighbor);
			}	
			current.setVisited(true);
			current.setCurrentNode(false);
		}
		return null;
	}
	
	/**
	 * This is a helper method to our algorithm methods.
	 * It is used to obtain a set of immediate valid neighbors 
	 * in the up, down, left, and right directions.
	 * @param node The of which the neighbors are to be found.
	 * @param nodes The set of nodes the program is exploring.
	 * @return LinkedList<Node> The linked list of immediate, valid,
	 * neighbors in the up, down, left and right directions.
	 */
	public LinkedList<Node> getNeighbors(Node node, Node[][] nodes) {
		int[][] intNeighbors = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
		LinkedList<Node> neighbors = new LinkedList<Node>();
		for(int[] element: intNeighbors) {
			int newRow = node.getRow() + element[0];
			int newCol = node.getCol() + element[1];
			if(newRow >= 0 && newRow < nodes.length && newCol >= 0 && newCol < nodes[0].length) {
				Node newNode = nodes[newRow][newCol];
				if(!(newNode.getState() == Node.wall || newNode.isVisited())) {
					neighbors.add(newNode);
				}
			}
		}
		return neighbors;
	}
	
	/**
	 * This method uses a linear search in order to locate
	 * the starting node, or the entry point, of the maze.
	 * @param nodes The nodes of which the entry point is
	 * to be found.
	 * @return Node The starting node, also known as the entry point,
	 * of the maze.
	 */
	public Node getStartNode(Node[][] nodes) {
		Node startNode = null;
		for(Node[] nodeArr : nodes) {
			for(Node node : nodeArr) {
				if(node.getState() == Node.startNode) {
					startNode = node;
				}
			}
		}
		return startNode;
	}
	
	/**
	 * This method uses a linear search in order to locate
	 * the ending node, or the exit point, of the maze.
	 * @param nodes The nodes of which the exit point is
	 * to be found.
	 * @return Node The ending node, also known as the exit point,
	 * of the maze.
	 */
	public Node getEndNode(Node[][] nodes) {
		Node endNode = null;
		
		for(Node[] nodeArr : nodes) {
			for(Node node : nodeArr) {
				if(node.getState() == Node.endNode) {
					endNode = node;
				}
			}
		}
		return endNode;
	}
	
	/**
	 * This method is used in order to get a set of nodes which
	 * denote the path from the entry point to the exit point of
	 * the maze. Every node has a parent node, this method works
	 * by backtracking the parent of each current node.
	 * @param maze The maze of which the result path is to be obtained.
	 * @return LinkedList<Node> The linked list which denotes the result path from
	 * the entry point to the exit point of the maze.
	 * @throws InterruptedException because of sleep times.
	 * @see InterruptedException
	 */
	public LinkedList<Node> getResultPath() throws InterruptedException {
		Node endNode = getEndNode(maze.getNodes());
		LinkedList<Node> endPath = new LinkedList<Node>();
		Node current = endNode;
		
		while(current.getState() != Node.startNode) {
			current.setCurrentNode(true);
			endPath.addFirst(current);
			if(!(current.getState() == Node.endNode || current.getState() == Node.startNode)) {
				current.setState(Node.pathNode);
			}
			Run.getGUI().getMazeJPanel().repaint();
			Thread.sleep(Run.getGUI().getDelayInMilliseconds());
			current.setCurrentNode(false);
			current = current.getParent();
		}
		Run.getGUI().getMazeJPanel().repaint();
		endPath.addFirst(current);
		return endPath;		
	}
	
}