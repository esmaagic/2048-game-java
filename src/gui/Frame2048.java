package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import logika.Igra2048;

/**
 * Glavni prozor za GUI.
 * @author Esma
 *
 */
public class Frame2048 extends JFrame implements KeyListener{

	private Igra2048 i2048;
	private JLabel[][] gridLabels;
	private JLabel topRezultat;
	private JLabel trenutniRez;

	public Frame2048() {
		setTitle("2048 Igrica");
		setSize(400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		nastaviZadnjuIgru();
		azurirajUI();
		addKeyListener(this);
		setFocusable(true);
		setLocationRelativeTo(null);
	}

	/**
	 * funkcija generise prozor za odredjivanje da li ce korisnik nastaviti prethodnu igru.
	 */
	private void nastaviZadnjuIgru() {
		if(!Igra2048.tabelaJePrazna()) {
			int option = JOptionPane.showConfirmDialog(
					this, "Zelite li nastavi prethodnu igru?", "Nastavi prethodnu igru", JOptionPane.YES_NO_OPTION);

			if (option == JOptionPane.YES_OPTION) {
				i2048 = new Igra2048(Igra2048.velicinaTabele());

				i2048.ucitajTabeluIzFajla(); // Load the saved game state
				inicijalizacija();
			} else {
				traziGridVelicinu(); // Prompt for grid size
			}
		}else {
			traziGridVelicinu();
		}
	}

	/**
	 * funkcija za inicijalizaciju glavne ploce sa grid-om i rezultatima.
	 */
	private void inicijalizacija() {
		setLayout(new BorderLayout());

		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(i2048.getTabela().length, i2048.getTabela()[0].length));

		gridLabels = new JLabel[i2048.getTabela().length][i2048.getTabela()[0].length];
		for (int i = 0; i < i2048.getTabela().length; i++) {
			for (int j = 0; j < i2048.getTabela()[0].length; j++) {
				gridLabels[i][j] = new JLabel("", SwingConstants.CENTER);
				gridLabels[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				gridLabels[i][j].setOpaque(true);
				gridLabels[i][j].setBackground(bojaPloce(i2048.getTabela()[i][j]));
				gridLabels[i][j].setFont(getTileFont());
				gridPanel.add(gridLabels[i][j]);
			}
		}

		topRezultat = new JLabel("Top rezultat: ", SwingConstants.LEFT);
		trenutniRez = new JLabel("Rezultat: 0", SwingConstants.LEFT);

		int padding = 10;
		topRezultat.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
		trenutniRez.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));

		add(gridPanel, BorderLayout.CENTER);
		add(trenutniRez, BorderLayout.NORTH);
		add(topRezultat, BorderLayout.SOUTH);
	}

	/**
	 * funkcija azurira glavnu plocu sa novim podacima.
	 */
	private void azurirajUI() {
		int[][] table = i2048.getTabela();

		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				int value = table[i][j];
				String text = (value == 0) ? "" : String.valueOf(value);

				gridLabels[i][j].setText(text);
				gridLabels[i][j].setBackground(bojaPloce(value));
				gridLabels[i][j].setFont(getTileFont());
			}
		}

		topRezultat.setText("najbolji rez:  " + Arrays.toString(i2048.ucitajBodove()));
		trenutniRez.setText("Rezultat:  " + i2048.getTrenutniTopRez());
	}

	/**
	 * funkcija za odredjivanje velicina grid-a ploce.
	 */
	private void traziGridVelicinu() {
		JDialog dialog = new JDialog(this, "Select Grid Size", true);
		dialog.setLayout(new GridLayout(2, 1));

		JButton button4x4 = new JButton("4x4");
		JButton button5x5 = new JButton("5x5");

		button4x4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				i2048 = new Igra2048(4);
				i2048.generisiBroj();
				i2048.generisiBroj();
				dialog.dispose();
				inicijalizacija();
			}
		});

		button5x5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				i2048 = new Igra2048(5);
				i2048.generisiBroj();
				i2048.generisiBroj();
				dialog.dispose();
				inicijalizacija();
			}
		});

		dialog.add(button4x4);
		dialog.add(button5x5);

		dialog.setSize(200, 100);
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	/**
	 * funkcija kada korisnik pritisne odgovarajucu tipku, obavlja akciju
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_LEFT:
			i2048.napraviPotez('a');
			azurirajUI();
			break;
		case KeyEvent.VK_UP:
			i2048.napraviPotez('w');
			azurirajUI();
			break;
		case KeyEvent.VK_DOWN:
			i2048.napraviPotez('s');
			azurirajUI();
			break;
		case KeyEvent.VK_RIGHT:
			i2048.napraviPotez('d');
			azurirajUI();
			break;
		}
		if(i2048.kraj()) {
			i2048.setJeNastavak(false);
			i2048.restartuj();
			i2048.sacuvajTabeluUFajl();
			krajIgreDijalog();
		}
		if(!i2048.getJeNastavak() && i2048.pobjeda()) {
			nastavakDijalog();
		}
	}

	/**
	 * funkcija za odredjivanje da li ce se igra nastaviti nakon pobjede ili ne.
	 */
	private void nastavakDijalog() {
		int option = JOptionPane.showConfirmDialog(this, "Congratulations! You reached the 2048 tile. Do you want to continue?", "2048 Tile Reached", JOptionPane.YES_NO_OPTION);

		if (option == JOptionPane.NO_OPTION) {
			i2048.setJeNastavak(false);
			i2048.restartuj();
			krajIgreDijalog();
		}else {
			i2048.setJeNastavak(true);
		}
		// You can add additional logic for handling the user's response to continue the game.
	}



	private Color bojaPloce(int value) {
		// Customize the colors based on the tile value
		switch (value) {
		case 2:    return new Color(0xeeeee4);
		case 4:    return new Color(0xede0c8);
		case 8:    return new Color(0xf2b179);
		case 16:   return new Color(0xf59563);
		case 32:   return new Color(0xf67c5f);
		case 64:   return new Color(0xf65e3b);
		case 128:  return new Color(0xedcf72);
		case 256:  return new Color(0xedcc61);
		case 512:  return new Color(0xedc850);
		case 1024: return new Color(0xedc53f);
		case 2048: return new Color(0xedc22e);
		default:   return new Color(0xcdc1b4); // Default color for other values
		}
	}

	private Font getTileFont() {
		return new Font("SansSerif", Font.BOLD, 20);
	}    

	/**
	 * funkcija generise dijalog za kraj igre
	 */
	private void krajIgreDijalog() {
		int option = JOptionPane.showConfirmDialog(this, "Game Over! Do you want to play again?", "Game Over", JOptionPane.YES_NO_OPTION);

		if (option == JOptionPane.YES_OPTION) {
			traziGridVelicinu();
			azurirajUI();

		} else {
			System.exit(0); // Exit the application if the user chooses not to play again
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}



}
