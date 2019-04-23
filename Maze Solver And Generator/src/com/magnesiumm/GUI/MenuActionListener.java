package com.magnesiumm.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.AbstractButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;

import com.magnesiumm.Run;

public class MenuActionListener implements ActionListener{

	/**
     * Denotes whether easter egg has been triggered. Can only happen once.
     */
    private boolean easterEggTriggered = false;
	
	/**
     * Handles actions on nav items.
     * @param e The ActionEvent to be handled
     */
	@Override
	public void actionPerformed(ActionEvent e) {
		AbstractButton source = (AbstractButton) e.getSource();
		NavElementID navElementID = Run.getGUI().getNavElementIDfromJComponent(source);

		switch(navElementID) {
			
			case JMenuItem_realTime:
				//easter egg
				if(!easterEggTriggered) {
					Run.getGUI().makeGoState(true);
					new Thread(new Runnable() {
					    public void run() {
					    	try {
					    		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(GUI.musicFilePath).getAbsoluteFile());
						        Clip clip = AudioSystem.getClip();
						        clip.open(audioInputStream);
						        clip.start();
						        Thread.sleep(clip.getMicrosecondLength()/1000);
						    } catch(Exception e) {
						        e.printStackTrace();
						    }
					    }
					  }).start();
					easterEggTriggered = true;
				}
				
				JSlider speedSlider = (JSlider) NavElementID.JSlider_speed.getjComponent();
				
				if(!Run.getGUI().isRealTime()) {
					speedSlider.setEnabled(false);
					Run.getGUI().setRealTime(true);
			    }
			    
				else if(Run.getGUI().isRealTime()) {
			    	speedSlider.setEnabled(true);
			    	Run.getGUI().setRealTime(false);
			    }
				break;
			
			case JMenuItem_saveAs:
				String saveFileName = JOptionPane.showInputDialog("Enter file name to save as:");
				try {
					Run.getFileOperations().writeNodes(saveFileName, Run.getGUI().getMazeJPanel().getMaze().getNodes());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				break;
				
			case JMenuItem_reset:
				Run.getGUI().reset();
				break;
			
			case JRadioButtonMenuItem_demoMode:
				NavElementID.JMenu_generation.getjComponent().setEnabled(false);
				NavElementID.JMenu_solve.getjComponent().setEnabled(false);
				break;
			
			case JRadioButtonMenuItem_customMode:
				NavElementID.JMenu_generation.getjComponent().setEnabled(true);
				NavElementID.JMenu_solve.getjComponent().setEnabled(true);
				break;
			
			case JMenuItem_changeMazeFilePath:
				JLabel label = (JLabel) NavElementID.JLabel_mazeFilePath.getjComponent();
				String newMazeFilePath = JOptionPane.showInputDialog(source.getText() + " - For a randomly generated maze allow for default path: \""+GUI.generatedMazeFilePath+"\"", GUI.generatedMazeFilePath);
				label.setText(newMazeFilePath);
				break;
				
			case JToggleButton_go:
				String sourceText = source.getText();
				if(sourceText.equals("Go")) {
					//Go clicked
					NavElementID.JMenu_maze.getjComponent().setEnabled(false);
					NavElementID.JMenu_mode.getjComponent().setEnabled(false);
					NavElementID.JMenu_generation.getjComponent().setEnabled(false);
					NavElementID.JMenu_solve.getjComponent().setEnabled(false);
					source.setSelected(true);
					source.setText("Stop");
					Run.getGUI().getCurrentLogicThread().resume();
				}
				else if(sourceText.equals("Stop")) {
					//Stop clicked
					source.setSelected(false);
					source.setText("Go");
					Run.getGUI().getCurrentLogicThread().suspend();
				}
				break;
			
			default:
				break;
		}
	}
	
}
