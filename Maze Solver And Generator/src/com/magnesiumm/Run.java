package com.magnesiumm;

import com.magnesiumm.GUI.GUI;
import com.magnesiumm.logic.FileOperations;

/**
* This class is run when program is run.
*
* @author  AJ Radik and Victoria Vigorito
* @version 6.0 
*/
public class Run {
	
	private static GUI gui;
	private static FileOperations fileOperations;
	
	public static void main(String[] args) {
		fileOperations = new FileOperations();
		gui = new GUI();
	}
	
	public static GUI getGUI() {
		return gui;
	}
	
	public static FileOperations getFileOperations() {
		return fileOperations;
	}
}