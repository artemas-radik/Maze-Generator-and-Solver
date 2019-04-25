package com.magnesiumm.GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import com.magnesiumm.Run;
import com.magnesiumm.logic.Maze;
import com.magnesiumm.logic.Node;

/**
* This class handles the maze part of the GUI.
*
* @author  AJ Radik and Victoria Vigorito
* @version 6.0 
*/
public class MazeJPanel extends JPanel {
	
	/**
     * value={@value buffer}; This value represents the buffer of space
     * on all sides of the GUI maze area, in pixels.
     */ 
	public static final int buffer = 6;
	
	/**
	 * This value represents the maze which the GUI is representing.
	 */
	private Maze maze;
	
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
        int width = getWidth();
        int height = getHeight();
       
        if(maze == null) {
        	return;
        }
        
        if(Run.getGUI().isReset()) {
        	graphics2D.clearRect(0, 0, width, height);
        	Run.getGUI().setReset(true);
        }
        
        Node[][] nodes = maze.getNodes();
        
        double xIncrement = (double) (width - 2 * buffer) / nodes[0].length;
        double yIncrement = (double) (height - 2 * buffer) / nodes.length;
        
        //Draw Squares
        for(int row = 0; row<nodes.length; row++) {
        	for(int col = 0; col<nodes[0].length; col++) {
        		double x = buffer + col * xIncrement;
        		double y = buffer + row * yIncrement;
        		double rectWidth = xIncrement;
        		double rectHeight = yIncrement;
        		Rectangle2D rect = new Rectangle2D.Double(x, y, rectWidth, rectHeight);
        		graphics2D.setPaint(nodes[row][col].getColor());
        		graphics2D.fill(rect);
        	}
        }
       
        //Draw Vertical Lines
        graphics2D.setPaint(Color.blue);
        for(int col = 0; col <= nodes[0].length; col++) {
            double x = buffer + col * xIncrement;
            graphics2D.draw(new Line2D.Double(x, buffer, x, height-buffer));
        }
        
        //Draw Horizontal Lines
        for(int row = 0; row <= nodes.length; row++) {
            double y = buffer + row * yIncrement;
            graphics2D.draw(new Line2D.Double(buffer, y, width-buffer, y));
        }
    }
	
    public void setMaze(Maze maze) {
		this.maze = maze;
	}
    
    public Maze getMaze() {
		return maze;
	}
    
}
