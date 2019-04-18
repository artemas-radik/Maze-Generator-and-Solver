import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

public enum NavElementID {
	
	JMenuBar_menuBar(new JMenuBar()),
	
	JMenu_options(new JMenu("Options")),
	JLabel_speed(new JLabel("Speed")),
	JSlider_speed(new JSlider(JSlider.HORIZONTAL, 1, 200, 200)),
	JMenuItem_realTime(new JMenuItem("Real Time (Seizure Warning) - Enable/Disable")),
	JMenuItem_reset(new JMenuItem("Reset")),
	
	JMenu_mode(new JMenu("Mode")),
	JRadioButtonMenuItem_demoMode(new JRadioButtonMenuItem("Demo Mode", true), Mode.Demo_Mode),
	JRadioButtonMenuItem_customMode(new JRadioButtonMenuItem("Custom Mode", false), Mode.Custom_Mode),
	
	JMenu_maze(new JMenu("Maze")),
	JLabel_mazeSize(new JLabel("Maze Size - WARNING: Large sizes may cause stack overflow!")),
	JSlider_mazeSizeMultiplier(new JSlider(JSlider.HORIZONTAL, 1, 20, 10)),
	
	JMenu_generation(new JMenu("Generation")),
	JRadioButtonMenuItem_dfsRandomGeneration(new JRadioButtonMenuItem("DFS Random Generation (Depth-First-Search)", true), GenerationAlgorithm.DFS_random),
	
	JMenu_solve(new JMenu("Solve")),
	JRadioButtonMenuItem_dfsSolve(new JRadioButtonMenuItem("DFS (Depth-First-Search)", true), SolveAlgorithm.DFS),
	JRadioButtonMenuItem_bfsSolve(new JRadioButtonMenuItem("BFS (Breadth-First-Search)", false), SolveAlgorithm.BFS),
	
	JToggleButton_go(new JToggleButton("Go"));
	
	
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
