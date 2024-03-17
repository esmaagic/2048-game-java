package logika;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
/**
 * Sva logika igre.
 * @author Esma
 *
 */
public class Igra2048 {
	private int tabela[][];
	private boolean nastavak;
	private int trenutniTopRez;
	/**
	 * 
	 *  konstruktor sa parametrom koji odredjuje dimenziju grida.
	 */
	public Igra2048(int n){
		nastavak = false;
		tabela = new int[n][n];
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				tabela[i][j] = 0;
			}
		}
	}

	/**
	 * konstruktor bez parametara postavlja dimenzije na 4*4
	 */
	public Igra2048(){
		this(4);

	}

	/**
	 * funkcija setter, postavi nastavak, koji je false po defaultu, na true ako je korisnik dosao do 2048, ali zeli nastaviti.
	 * @param x
	 */
	public void setJeNastavak(boolean x) {
		nastavak = x;
	}

	/**
	 * funkcija setter, postavlja element iz tabele na poziciji [i][j] na vrijednost parametra br.
	 * @param i
	 * @param j
	 * @param br
	 */
	public void setTabela(int i, int j, int br) {
		tabela[i][j] = br;
	}

	/**
	 * funkcija getter, vraca trenutni najbolji rezultat igre.
	 * @return
	 */
	public int getTrenutniTopRez() {
		return trenutniTopRez;
	}

	/**
	 * funkcija setter, postavlja trenutni top rezultat na proslijedjenu vrijednost iz paramet.
	 * @param x
	 */
	public void setTrenutniTopRez(int x) {
		trenutniTopRez = x;
	}

	/**
	 * funkcija getter, vraca true ako je igra nastavljena nakon pobjede, inace vraca false.
	 * @return
	 */
	public boolean getJeNastavak() {
		return nastavak;
	}

	/**
	 * funkcija vraca true ako je moguce napraviti makar jedan potez u tabeli, false ako nije moguce.
	 * @return
	 */
	public boolean kraj() {
		// Check for empty spaces on the board
		for (int i = 0; i < tabela.length; i++) {
			for (int j = 0; j < tabela[i].length; j++) {
				if (tabela[i][j] == 0) {
					return false; // There is an empty space, so there are moves left
				}
			}
		}

		// Check for adjacent tiles with the same value horizontally
		for (int i = 0; i < tabela.length; i++) {
			for (int j = 0; j < tabela[i].length - 1; j++) {
				if (tabela[i][j] == tabela[i][j + 1]) {
					return false; // There are adjacent tiles with the same value, so there are moves left
				}
			}
		}

		// Check for adjacent tiles with the same value vertically
		for (int i = 0; i < tabela.length - 1; i++) {
			for (int j = 0; j < tabela[i].length; j++) {
				if (tabela[i][j] == tabela[i + 1][j]) {
					return false; // There are adjacent tiles with the same value, so there are moves left
				}
			}
		}

		// No empty spaces or adjacent tiles with the same value found, so no moves left
		trenutniTopRez = 0;
		return true;
	}

	/**
	 * funkcija vraca true ako je makar jedan broj u tabeli 2048, false ako nije niti jedan. 
	 * @return
	 */
	public boolean pobjeda() {
		for (int i = 0; i < tabela.length; i++) {
			for (int j = 0; j < tabela[i].length; j++) {
				if (tabela[i][j] == 2048) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * funkcija postavlja tabelu na defaultno stanje, svi brojevi su nula.
	 */
	public void restartuj() {
		for(int i = 0; i < tabela.length; i++) {
			for(int j = 0; j < tabela.length; j++) {
				setTabela(i,j,0);
			}
		}
	}

	/**
	 * funkcija vraca trenutnu tabelu igre.
	 * @return
	 */
	public int[][] getTabela() {
		return tabela;
	}

	/**
	 * funkcija generise nasumican broj 2 ili 4. Poziva funkciju slobodnePozicije na osnovu
	 * koje nasumicno bira poziciju za novogenerisani broj.
	 */


	public void generisiBroj() {
		int[] slobodnePozicije = slobodnePozicije();
		Random random = new Random();
		if (slobodnePozicije.length > 0) {
			int randomPosition = slobodnePozicije[random.nextInt(slobodnePozicije.length)];

			int randomNumber = (random.nextInt(2) + 1) * 2;

			int row = randomPosition / tabela.length;
			int col = randomPosition % tabela.length;

			tabela[row][col] = randomNumber;
			sacuvajTabeluUFajl();
		}
	}

	/**
	 *  Funkcija kreira niz za pohranu slobodnih pozicija. 
	 * Prolazi kroz tabelu kako bi pronaša slobodne pozicije. Pretvori 2D poziciju u 1D.
	 * Prilagodi velicinu niza stvarnom broju slobodnih pozicija
	 * @return
	 */
	private int[] slobodnePozicije() {
		int[] slobodnePozicije = new int[tabela.length * tabela.length];
		int count = 0;

		for (int i = 0; i < tabela.length; i++) {
			for (int j = 0; j < tabela[i].length; j++) {
				if (tabela[i][j] == 0) {
					slobodnePozicije[count++] = i * tabela.length + j;
				}
			}
		}
		int[] rezultat = new int[count];
		System.arraycopy(slobodnePozicije, 0, rezultat, 0, count);
		return rezultat;
	}







	/**
	 * funcija na osnovu proslijedjenog parametra pravi potez u odgovarajucem smjeru.
	 * @param smjer
	 */
	public void napraviPotez(char smjer) {
		int[][] preMoveTable = kopirajTabelu(tabela);

		switch (smjer) {
		case 'a':
			pomjeriLijevo();
			break;
		case 'w':
			pomjeriGore();
			break;
		case 's':
			pomjeriDolje();
			break;
		case 'd':
			pomjeriDesno();
			break;
		default:
			System.out.println("Invalid move. Use 'a', 'w', 's', or 'd'.");
		}
		if (!tabeleSuJednake(preMoveTable, tabela)) {

			int x = trenutniNajveciElement();
			if(x> trenutniTopRez) {
				trenutniTopRez = x;
			}
			generisiBroj();
			sacuvajTabeluUFajl();
			azurirajNajboljeBodove();
		}

	}

	private boolean tabeleSuJednake(int[][] table1, int[][] table2) {
		for (int i = 0; i < table1.length; i++) {
			for (int j = 0; j < table1[i].length; j++) {
				if (table1[i][j] != table2[i][j]) {
					return false;
				}
			}
		}
		return true;
	}

	private int[][] kopirajTabelu(int[][] original) {
		int[][] copy = new int[original.length][original[0].length];
		for (int i = 0; i < original.length; i++) {
			System.arraycopy(original[i], 0, copy[i], 0, original[i].length);
		}
		return copy;
	}

	/**
	 * Funkcija koja pomjera elemente tabele prema lijevo.
	 * Pronalazi krajnju lijevu praznu poziciju ili podudarnu plocicu. Spaja plocicu ako se podudaraju.
	 * Premjesti plocicu na krajnju lijevu poziciju
	 */

	private void pomjeriLijevo() {
		for (int red = 0; red < tabela.length; red++) {
			for (int kolona = 1; kolona < tabela[red].length; kolona++) {
				if (tabela[red][kolona] != 0) {

					int spojiKolone = kolona - 1;
					while (spojiKolone >= 0 && (tabela[red][spojiKolone] == 0 || tabela[red][spojiKolone] == tabela[red][kolona])) {
						if (tabela[red][spojiKolone] == 0) {
							spojiKolone--;
						} else {

							tabela[red][spojiKolone] *= 2;
							tabela[red][kolona] = 0;
							break;
						}
					}

					if (spojiKolone + 1 != kolona) {
						tabela[red][spojiKolone + 1] = tabela[red][kolona];
						tabela[red][kolona] = 0;
					}
				}
			}
		}
	}

	/**
	 * Funkcija koja pomjera elemente tabele prema desno.
	 * Pronalazi krajnju desnu praznu poziciju ili podudarnu plocicu. Spaja plocicu ako se podudaraju.
	 * Premjesti plocicu na krajnju desnu poziciju
	 */
	private void pomjeriDesno() {
		for (int red = 0; red < tabela.length; red++) {
			for (int kolona = tabela[red].length - 2; kolona >= 0; kolona--) {
				if (tabela[red][kolona] != 0) {

					int spojiKolone = kolona + 1;
					while (spojiKolone < tabela[red].length && (tabela[red][spojiKolone] == 0 || tabela[red][spojiKolone] == tabela[red][kolona])) {
						if (tabela[red][spojiKolone] == 0) {
							spojiKolone++;
						} else {

							tabela[red][spojiKolone] *= 2;
							tabela[red][kolona] = 0;
							break;
						}
					}

					if (spojiKolone - 1 != kolona) {
						tabela[red][spojiKolone - 1] = tabela[red][kolona];
						tabela[red][kolona] = 0;
					}
				}
			}
		}
	}
	/**
	 * Funkcija koja pomjera elemente tabele prema gore.
	 * Pronalazi krajnju gornju praznu poziciju ili podudarnu plocicu. Spaja plocicu ako se podudaraju.
	 * Premjesti plocicu na krajnju gornju poziciju
	 */
	private void pomjeriGore() {
		for (int kolona = 0; kolona < tabela[0].length; kolona++) {
			for (int red = 1; red < tabela.length; red++) {
				if (tabela[red][kolona] != 0) {

					int spojiRed = red - 1;
					while (spojiRed >= 0 && (tabela[spojiRed][kolona] == 0 || tabela[spojiRed][kolona] == tabela[red][kolona])) {
						if (tabela[spojiRed][kolona] == 0) {
							spojiRed--;
						} else {

							tabela[spojiRed][kolona] *= 2;
							tabela[red][kolona] = 0;
							break;
						}
					}


					if (spojiRed + 1 != red) {
						tabela[spojiRed + 1][kolona] = tabela[red][kolona];
						tabela[red][kolona] = 0;
					}
				}
			}
		}
	}
	/**
	 * Funkcija koja pomjera elemente tabele prema dolje.
	 * Pronalazi krajnju donju praznu poziciju ili podudarnu plocicu. Spaja plocicu ako se podudaraju.
	 * Premjesti plocicu na krajnju donju poziciju
	 */
	private void pomjeriDolje() {
		for (int kolona = 0; kolona < tabela[0].length; kolona++) {
			for (int red = tabela.length - 2; red >= 0; red--) {
				if (tabela[red][kolona] != 0) {

					int spojiRed = red + 1;
					while (spojiRed < tabela.length && (tabela[spojiRed][kolona] == 0 || tabela[spojiRed][kolona] == tabela[red][kolona])) {
						if (tabela[spojiRed][kolona] == 0) {
							spojiRed++;
						} else {

							tabela[spojiRed][kolona] *= 2;
							tabela[red][kolona] = 0;
							break;
						}
					}


					if (spojiRed - 1 != red) {
						tabela[spojiRed - 1][kolona] = tabela[red][kolona];
						tabela[red][kolona] = 0;
					}
				}
			}
		}
	}

	/**
	 * funkcija spasi trenutno stanje tabele u fajl.
	 */
	public void sacuvajTabeluUFajl() {
		try (FileWriter writer = new FileWriter("path to datoteke stanjeTabele.txt",false)) {
			for (int i = 0; i < tabela.length; i++) {
				for (int j = 0; j < tabela[i].length; j++) {
					writer.write(tabela[i][j] + " ");
				}
				writer.write("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * funkcija ucita stanje tabele prethodne igre u tabelu trenutne igre
	 * 
	 */
	public void ucitajTabeluIzFajla() {
		try (BufferedReader reader = new BufferedReader(new FileReader("path to datoteke stanjeTabele.txt"))) {

			for (int i = 0; i < tabela.length; i++) {
				String[] line = reader.readLine().split(" ");
				for (int j = 0; j < tabela[i].length; j++) {
					tabela[i][j] = Integer.parseInt(line[j]);
				}
			}
		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
		}
	}

	/**
	 * funkcija iz fajla cita podatke i vraca velicinu tabele.
	 *
	 */
	public static int velicinaTabele() {
		int n = 0;

		try (BufferedReader reader = new BufferedReader(new FileReader("path to datoteke stanjeTabele.txt"))) {
			while (reader.readLine() != null) {
				n++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return n;
	}

	/**
	 * funkcija vraca true ako je tabela prazna, false ako nije.
	 * @return
	 */
	public static boolean tabelaJePrazna() {
		try (BufferedReader reader = new BufferedReader(new FileReader("path to datoteke stanjeTabele.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] numbers = line.split("\\s+");
				for (String number : numbers) {
					int value = Integer.parseInt(number);
					if (value != 0) {
						return false;
					}
				}
			}
		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
		}
		return true;
	}


	/**
	 * funkcija azurira trenutnu najbolji rezultat
	 */
	public void azurirajNajboljeBodove() {
		int topNumbers[] = ucitajBodove();
		int newScore = trenutniNajveciElement();

		if (newScore > topNumbers[0] && newScore != topNumbers[1] && newScore != topNumbers[2]) {
			topNumbers[0] = newScore;
			Arrays.sort(topNumbers); 
			spasiNajboljeBodoveUFajl(topNumbers);
		}

	}

	/**
	 * funkcija spasava proslijedjeni niz u fajl za najbolje bodove.
	 * @param topNumbers
	 */
	private void spasiNajboljeBodoveUFajl(int topNumbers[]) {
		try (FileWriter writer = new FileWriter("path to datoteke najboljiBodovi.txt")) {
			for (int i = topNumbers.length - 1; i >= 0; i--) {
				writer.write(topNumbers[i] + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * funkcija vrava najveci element iz trenutne tabele.
	 * @return
	 */
	private int trenutniNajveciElement() {
		int trenutniNajveciElement = 0;
		for (int[] row : tabela) {
			for (int num : row) {
				trenutniNajveciElement = Math.max(trenutniNajveciElement, num);
			}
		}
		return trenutniNajveciElement;
	}

	private void azurirajTrenutniTopRez() {
		int x = trenutniNajveciElement();
		if(x>trenutniTopRez) {
			trenutniTopRez = x;
		}
	}


	/**
	 * funkcija vraca najbolje rezultate.
	 * @return
	 */
	public int[] ucitajBodove() {
		int topNumbers[] = new int[3];
		try (BufferedReader reader = new BufferedReader(new FileReader("path to datoteke najboljiBodovi.txt"))) {
			for (int i = topNumbers.length - 1; i >= 0; i--) {
				topNumbers[i] = Integer.parseInt(reader.readLine());
			}
		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
		}
		Arrays.sort(topNumbers);
		return topNumbers;
	}
}
