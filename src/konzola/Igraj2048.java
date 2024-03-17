package konzola;

import java.util.Arrays;
import java.util.Scanner;

import logika.Igra2048;
/**
 * Pokrece igru u konzoli
 * @author Esma
 *
 */
public class Igraj2048 {


	/**
	 * main funkcija koja pokrece igricu u konzoli.
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Igra2048 i2048;
		Scanner sc = new Scanner(System.in);
		if(!Igra2048.tabelaJePrazna()) {
			System.out.println("Ako zelite nastaviti prehodnu igru unesite 'da', u suprotnom unesite 'ne'.");
			String nastaviIgru = sc.next().toLowerCase();
			if(nastaviIgru.equals("da")) {
				i2048 = new Igra2048(Igra2048.velicinaTabele());
				i2048.ucitajTabeluIzFajla();
			}else {
				System.out.println("Unesite velicinu ploce n: ");
				int velicina = sc.nextInt();
				i2048 = new Igra2048(velicina == 5 ? 5 : 4);
				i2048.generisiBroj();
				i2048.generisiBroj();
			}
		}else {
			System.out.println("Unesite velicinu ploce n: ");
			int velicina = sc.nextInt();
			i2048 = new Igra2048(velicina == 5 ? 5 : 4);
			i2048.generisiBroj();
			i2048.generisiBroj();
		}






		int topBodovi[] = new int[3];
		int bodovi;
		while(!i2048.kraj()) {
			topBodovi= i2048.ucitajBodove();
			bodovi = i2048.getTrenutniTopRez();
			System.out.println("top bodovi: "+ Arrays.toString(topBodovi));
			System.out.println("bodovi: "+ bodovi);



			ispisiTabelu(i2048.getTabela());
			System.out.println("Napravite potez lijevo: = 'a', gore = 'w', dolje = 's', desno = 'd'");
			char potez = sc.next().charAt(0);
			i2048.napraviPotez(Character.toLowerCase(potez));
			if(i2048.kraj()) {
				System.out.println("Izgubili ste. Da bi ste ponovo igrali unesite 'r'");
				String restartuj = sc.next();
				i2048.setJeNastavak(false);
				i2048.restartuj();
				i2048.sacuvajTabeluUFajl();
				if(!restartuj.contains("r")) {
					System.out.println("kraj igre.");
					break;
				}else {
					i2048.generisiBroj();
					i2048.generisiBroj();
					i2048.sacuvajTabeluUFajl();
				}


			}
			if(!i2048.getJeNastavak()) {
				if(i2048.pobjeda()) {
					ispisiTabelu(i2048.getTabela());
					System.out.println("Cestitam, pobjedili ste! Ako zelite nastavit unesite 'da', inace unesite 'ne'");
					String nastavi = sc.next().toLowerCase();
					if(nastavi.equals("da")) {
						i2048.setJeNastavak(true);
					}else {
						System.out.println("Kraj igre");
						break;
					}
				}
			}
		}
	}

	/**
	 * funkcija koja ispisuje trenutno stanje tabele u konzoli
	 * @param tabela
	 */
	private static void ispisiTabelu(int tabela[][]) {
		pomocniIspis(tabela);
		for (int i = 0; i < tabela.length; i++) {
			for (int j = 0; j < tabela[i].length; j++) {
				System.out.print("| " + String.format("%-5s", (tabela[i][j] == 0 ? " " : tabela[i][j])) + " ");
			}
			System.out.println("|");
			pomocniIspis(tabela);
		}
	}

	private static void pomocniIspis(int[][] tabela) {
		for (int k = 0; k < tabela.length; k++) {
			System.out.print("+-------");
		}
		System.out.println("+");
	}

}
