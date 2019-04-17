import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
* This class is responsible for displaying the info panel
* at the end of the the program. Mainly displaying the run time.
* An object of type EndPanel is constructed at the end of the program
* by Main in order to display the run time of the program.
*
* @author  AJ Radik and Victoria Vigorito
* @version 5.0 
*/
public class EndPanel {

	/**
	 * This constructor creates and launches a JFrame that
	 * displays the run time of the program.
	 * @param runtime The run time of the program.
	 */
	public EndPanel(long runtime) {
		JFrame frame = new JFrame();
		JLabel label = new JLabel("The run time was: " + runtime/1000.0 + " seconds");
		label.setFont(label.getFont ().deriveFont (64.0f));
		frame.add(label);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation( (int) (0.3 * screenSize.width), (int) (0.3 * screenSize.height));
        frame.setSize(1200, 200); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
	}
	
}
