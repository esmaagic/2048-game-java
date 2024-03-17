package gui;

import javax.swing.SwingUtilities;

/**
 * pokrece igru u GUI.
 * @author Esma
 *
 */
public class Igraj2048 {

	/**
	 * main funkcija koja pokrece gui interface
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Frame2048 game2048GUI = new Frame2048();
			game2048GUI.setVisible(true);
		});
	}

}
