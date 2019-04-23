package com.magnesiumm;

import com.magnesiumm.GUI.GUI;
import com.magnesiumm.logic.FileOperations;

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