package com.magnesiumm.logic;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
* This class is responsible for all file I/O, 
* mainly importing a 2D array of nodes. This class is used
* by the Maze class when a new maze is constructed. It loads
* the nodes into the maze using file I/O.
*
* @author  AJ Radik and Victoria Vigorito
* @version 5.0 
*/
public class FileOperations {

	/**
	 * This method loads data from a file and returns an array
	 * of new Nodes for the program to solve as a maze.
	 * @param fileName The String file path that locates
	 * the file to be used.
	 * @return Node[][] this returns a 2D array representation
	 * of the nodes, loaded from the file path.
	 */
	public static Node[][] loadNodes(String fileName) {
		//read!!!!!!
		try {
			FileReader fileReader = new FileReader(fileName);
			StringBuffer stringBuffer = new StringBuffer();
			int charsRead;
			char[] charArr = new char[1024];
			while((charsRead = fileReader.read(charArr)) > 0) {
				stringBuffer.append(charArr, 0, charsRead);
			}
			fileReader.close();
			String[] arr1D =  stringBuffer.toString().split("\r\n");
			String[][] arr2D = new String[arr1D.length][arr1D[0].length()];
			for(int row = 0; row < arr2D.length; row++) {
				arr2D[row] = arr1D[row].split("");
			}
			
			Node[][] toReturn = new Node[arr2D.length][arr2D[0].length];
			
			for(int row = 0; row < toReturn.length; row++) {
				for(int col = 0; col < toReturn[0].length; col++) {
					toReturn[row][col] = new Node(Integer.parseInt(arr2D[row][col]), row, col);
				}
			}
			return toReturn;
		}catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	public static void writeNodes(String fileName, Node[][] nodes) throws IOException {
		String toWrite = "";
		for(int row = 0; row < nodes.length; row++) {
			for(int col = 0; col < nodes[0].length; col++) {
				toWrite +=nodes[row][col];
			}
			if(row < nodes.length-1) {
				toWrite += "\r\n";
			}
		}
		toWrite.trim();
		FileWriter fileWriter = new FileWriter(fileName);
		fileWriter.write(toWrite);
		fileWriter.close();
	}
	
}