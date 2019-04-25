package com.magnesiumm.GUI;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

import com.magnesiumm.configurationData.*;
import com.magnesiumm.configurationData.actualData.GenerationAlgorithm;
import com.magnesiumm.configurationData.actualData.Mode;
import com.magnesiumm.configurationData.actualData.SolveAlgorithm;

/**
* This enum represents a nav element.
*
* @author  AJ Radik and Victoria Vigorito
* @version 6.0 
*/
public enum NavElementID {
	//ensures every NavElementID has a JComponent, plus init values
	
	//Menu Bar
	JMenuBar_menuBar(new JMenuBar()),
	
	//Start Options Menu
	JMenu_options(new JMenu("Options")),
	//Speed Section
	JLabel_speed(new JLabel("Speed")),
	JSlider_speed(new JSlider(JSlider.HORIZONTAL, 1, 200, 200)),
	JMenuItem_realTime(new JMenuItem("Real Time (Seizure Warning) - Enable/Disable")),
	//Generate Solve Delay Section
	JLabel_generateSolveDelay(new JLabel("Delay inbetween generation and solve steps")),
	JSlider_generateSolveDelay(new JSlider(JSlider.HORIZONTAL, 0, 12000, 0)),
	//Save as menu item
	JMenuItem_saveAs(new JMenuItem("Save As")),
	//Reset Menu Item
	JMenuItem_reset(new JMenuItem("Reset")),
	//End Options Menu
	
	//Start Mode Menu
	JMenu_mode(new JMenu("Mode")),
	JRadioButtonMenuItem_demoMode(new JRadioButtonMenuItem("Demo Mode", true), Mode.Demo_Mode),
	JRadioButtonMenuItem_customMode(new JRadioButtonMenuItem("Custom Mode", false), Mode.Custom_Mode),
	//End Mode Menu
	
	//Start Maze Menu
	JMenu_maze(new JMenu("Maze")),
	JLabel_mazeSize(new JLabel("Generated Maze Size - WARNING: Large sizes may cause stack overflow!")),
	JSlider_mazeSizeMultiplier(new JSlider(JSlider.HORIZONTAL, 1, 20, 10)),
	JLabel_mazeFilePathLabel(new JLabel("Current Maze File Path: ")),
	JLabel_mazeFilePath(new JLabel(GUI.generatedMazeFilePath)),
	JMenuItem_changeMazeFilePath(new JMenuItem("Change Maze File Path")),
	//End Maze Menu
	
	//Start Generation Menu
	JMenu_generation(new JMenu("Generation")),
	JRadioButtonMenuItem_dfsRandomGeneration(new JRadioButtonMenuItem("DFS Random Generation (Depth-First-Search)", true), GenerationAlgorithm.DFS_random),
	//End Generation Menu
	
	//Start Solve Menu
	JMenu_solve(new JMenu("Solve")),
	JRadioButtonMenuItem_dfsSolve(new JRadioButtonMenuItem("DFS (Depth-First-Search)", true), SolveAlgorithm.DFS),
	JRadioButtonMenuItem_bfsSolve(new JRadioButtonMenuItem("BFS (Breadth-First-Search)", false), SolveAlgorithm.BFS),
	//End Solve Menu
	
	//Start Go Button
	JToggleButton_go(new JToggleButton("Go"));
	//End Go Button
	
	private JComponent jComponent;
	private ConfigurationData configurationData;
	
	NavElementID(JComponent jComponent) {
		this.jComponent = jComponent;
	}
	
	NavElementID(JComponent jComponent, ConfigurationData configurationData) {
		this.jComponent = jComponent;
		this.configurationData = configurationData;
	}

	public JComponent getjComponent() {
		return jComponent;
	}

	public ConfigurationData getConfigurationData() {
		return configurationData;
	}
}
