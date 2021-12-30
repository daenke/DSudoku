import java.util.*;

/*
 * Created on 06.06.2007
 *
 */

/**
 * Klasse zum Erstellen eines lösbaren Sudokus
 * @author Daniel Enke
 *
 */
public class SudokuGenerator {
	
	// nur zum debuggen
	static int test = 0;

	// Variablen für Try&Error (tae)
	private int sudokuTry [][] = new int [9][9];
	private int generatedSudoku [][] = new int [9][9];
	private Vector taeFind = new Vector(); // sv zum Lösg. suchen mit zufälliger Untermischung
	private Vector taeCheck = new Vector(); // sv zum checken (Felder streichen)

	private int status;  	// Stati für tae
	private static final int UNDEFINED = 0;
	private static final int ALLRIGHT = 1;
	private static final int UNCRACKED = 2;
	// static final int ERRORFOUND = 3;
	
	private boolean checkForAll;
	private static final boolean GIVE_ALL_SOLUTIONS = true;
	private static final boolean GIVE_ONLY_SINGULAR_SOLUTION = false;
	
	private int testedValue=0; // untersuchte Zahl in taeStart (höchste Ebene)
	private int okayValue=0; // Zahl bei der Sudoku zur Lösung führte
	private int countOfSolvesInTry=0;
	private boolean firstTimeAllRightWasSeen;
	private boolean solveFast;
	private int rowOfSolution, colOfSolution;
	
	Vector svMax = new Vector ();

	private static Random r = new Random ( System.currentTimeMillis() );
	SudokuMainframe pparent;
	
	/** kopiert in Vorbereitung des try&error-Algorithmus das Sudoku-Array in sudokuTry
	 *  und erstellt eine sortedList tae aller ungelösten Felder mit ihren SudokuVektoren (sv)*/
	public SudokuGenerator (SudokuMainframe sm) {
		
		pparent  = sm;
		solveFast = false;
		
		taeFind.removeAllElements();
		
		for (int i=1; i<10; i++) svMax.add (new Integer(i));
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				sudokuTry[i][j] = 0; generatedSudoku[i][j] = 0;

				SudokuUnsolvedItem buffer = new SudokuUnsolvedItem ( i, j, 9, svMax );
				int pos=0, same=0; // boolean equal = false;
				for (int k=0; k<taeFind.size(); k++) {
					// if ( ((SudokuItem) tae.get(k)).equals(buffer))
						// equal = true; // ursprünglich für Einmaligkeit gedacht nun eigentlich nicht mehr notwendig
					if ( ((SudokuUnsolvedItem) taeFind.get(k)).compareTo(buffer) < 0 )
						pos++; // aufsteigende Liste hiermit realisiert
					if ( ((SudokuUnsolvedItem) taeFind.get(k)).compareTo(buffer) == 0 )
						same++; // Anzahl gleiche Einträge wegen zufälliger Untermischung
				}
				// if ( ! equal ) 
				pos = pos + r.nextInt(same+1); // mischt zufällig unter
				taeFind.add (pos, buffer); // jetzt stehen die kleinen Vektoren vorne im tae-Vektor
			}
		}
	}

	/** erzeugt Sudoku-Lösung und löscht zu erratende Felder	 */
	public void generate (int destinationMatrix[][]) {

		findSolve(generatedSudoku); // findet eine zufällige Lösung
		pparent.startAnimation();
		
		// LESEZEICHEN ziemlich träge, entweder beschl. oder Zahl verkleinern
		int fieldsToRemove = 49 + r.nextInt(6);
		int ze=0, sp=0;
		int harakiri = 0;
		int symmetrieMode=r.nextInt(3), symmetrie = 0; // Mode zum Löschen von Mustern
		// TODO 1 Mode fehlt noch
		final int NONSYMMETRIC = 0;

		final int DIAMETRAL = 1; // gespiegelt an senkrecht / waagerecht Achsen
		int zema =0, spma=0; // Zeilen/Spalten-Mittenabstand

		final int DIAGONAL = 2; // 4xgedreht um 90°
		int ha=0, de=0; // Hebelarm, Deichsel

		final int TRIMERIADIAL = 3; // 3x gedreht um 120°

		for (int h = 0; h < fieldsToRemove; ) { // löscht ab hier Felder MIT Prüfung, zufällige Schwierigkeit
			if (symmetrieMode == NONSYMMETRIC) {
				ze=r.nextInt(9); sp=r.nextInt(9);
				if (SC.debug) System.out.print("\n unsymmetrisch :  "+ze+" "+sp);
			} else if (symmetrieMode == DIAMETRAL) {
				switch (symmetrie%4) { 
					case 0: zema=r.nextInt(5); spma=r.nextInt(5);
								ze = 4-zema; sp = 4-spma; 
								if (SC.debug) System.out.print("\n diametral (Zeilen-Mittenabstand = "+zema+" Spalten-Mittenabstand = "+spma+"  :  ");
								break;
					case 1: ze = 4-zema; sp = 4+spma; break;
					case 2: ze = 4+zema; sp = 4-spma; break;
					case 3: ze = 4+zema; sp = 4+spma; break;
				}
			} else if (symmetrieMode == DIAGONAL) {
				switch (symmetrie%4) { 
					case 0: ha=r.nextInt(5); de=r.nextInt(5); 
								ze = 4-ha; sp = 4-de; 
								if (SC.debug) System.out.print("\n diagonal (Hebelarm = "+ha+" Deichsel = "+de+"  :  ");
								break;
					case 1: sp = 4-ha; ze = 4+de; break;
					case 2: ze = 4+ha; sp = 4+de; break;
					case 3: sp = 4+ha; ze = 4-de; break;
				}
			}
			symmetrie++; harakiri ++;
			if (SC.debug) System.out.print("    "+ze+" "+sp+"    ");
			if ( removeField (generatedSudoku, ze, sp) ) {
				h++; // leert Felder zufällig für Anwender zum Raten
				harakiri = 0;
				if (h <= 8) pparent.startAnimation(); 
				else if (h >= (fieldsToRemove-8))	pparent.endAnimation();
			}
			if ( harakiri > 75 ) break;
		}

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				destinationMatrix[i][j] = generatedSudoku[i][j];
			}
		}
	}

	/** versucht mit Try&Error Lösungsmöglichkeiten zu suchen oder auszuschließen
	 * firstCall bei Nutzeraufruf immer "true"
	 * @return Status-werte (ALLRIGHT, UNCRACKED oder ERRORFOUND)
	 */
	public int taeSolveAllSolutions () {
		checkForAll = GIVE_ALL_SOLUTIONS;
		solveFast=false;
		return taeSolveRecursive(true, taeCheck);
	}

	public int checkSolve() { // nach Aufruf dieser Routine ist taeStart unverändert
		//SudokuUnsolvedItem taeFirstItemBackup = copyFirstIteminSudokuVector(taeCheck);

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				sudokuTry[i][j] = generatedSudoku[i][j];
			}
		}

		int test = taeSolveAllSolutions();
		//taeCheck.add(0, taeFirstItemBackup); // 1. Element wieder hinzu, da bei taeRekursive gelöscht
		return test;
	}

	public int taeSolveOneSolution () {
		checkForAll = GIVE_ONLY_SINGULAR_SOLUTION;
		return taeSolveRecursive(true, taeFind);
	}

	public int findSolve(int [][] matrix) { // nach Aufruf dieser Routine ist taeStart unverändert
		SudokuUnsolvedItem taeFirstItemBackup = copyFirstIteminSudokuVector(taeFind);
		int test = taeSolveOneSolution();
		taeFind.add(0, taeFirstItemBackup); // 1. Element wieder hinzu, da bei taeRekursive gelöscht

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				matrix[i][j] = sudokuTry[i][j];
			}
		}

		return test;
	}

	public int getSolution() {
		if (okayValue == 0) taeSolveAllSolutions();
		
		return okayValue;
	}
	
	/** sucht rekursiv eine oder mehrere Lösungen
	 * 
	 * @param firstCall - immer true (benötigt für Rekursivität)
	 * @param formerTae - ein geordneter tae-Vektor (siehe Konstruktor) 
	 * @return Statuswert als int
	 */
	private int taeSolveRecursive (boolean firstCall, Vector formerTae) {

		status = UNDEFINED;
		
		// test ++; System.out.print(test + " "); //debugging

		// 1. Eintrag extrahieren - dann löschen
		SudokuUnsolvedItem firstItem = copyFirstIteminSudokuVector (formerTae);
		formerTae.remove(0);

		// Verzweigungsende: keine mögliche Zahl
		if ( firstItem.openPossibilities() == 0 ) return UNCRACKED;

		// letztes offenes Feld mit genau einer Eintragmöglichkeit => alles okay
		if ( formerTae.size() == 0) {
			if ( firstItem.openPossibilities() == 1 ) {
				countOfSolvesInTry ++;
				if ( ! firstTimeAllRightWasSeen) {
					okayValue = testedValue;
					firstTimeAllRightWasSeen = true;
				}
				sudokuTry[firstItem.row][firstItem.column] =((Integer) (firstItem.getValues().get(0))).intValue();
				return ALLRIGHT;
			}
		}

		for (int i=0; i < firstItem.openPossibilities() ; i++) {
			Vector tae = copyAndOrderANewSudokuVector (formerTae); 
			Integer lookFor = new Integer ( ((Integer) firstItem.getValues().get(i)).intValue() );

			if (firstCall) { // beim ersten Einstieg wird dieser Bereich abgearbeitet
				rowOfSolution = firstItem.row; colOfSolution = firstItem.column;
				testedValue = lookFor.intValue();
				countOfSolvesInTry = 0;
			} 

			sudokuTry[firstItem.row][firstItem.column] = lookFor.intValue();
			
			// doppelte Zahlen löschen in Vektoren eines Blockes (row, col, triple)
			//	boolean aus remove wäre auswertbar (wenn false continue hier)
			removeInVectors (firstItem.row, firstItem.column, tae, lookFor);

			// rekursiv aufrufen
			status = taeSolveRecursive(false, tae); // wenn noch offene vorhanden durch Annahmen weiter vertiefen
			
			if (countOfSolvesInTry > 99) solveFast = true; 
			if ( (solveFast == true) && (status == ALLRIGHT) ) { 
				// nach einmaligem ganzen durchsuchen des Sudokus bricht er hier beim ALLRIGHT ab, wegen Schnelligkeit
				// sonst würde die Anzahl von Lösungen nicht kennen
				return status;
			}
		if ( (checkForAll == GIVE_ONLY_SINGULAR_SOLUTION) && ( status == ALLRIGHT ) )
			return status; // ist eine Abkürzung, findet aber nicht alle Lösungen
		}
		return status;
	}

	private void removeInVectors (int row, int col, Vector tae, Integer lookFor) {
		
		// suche Vektor tae(zeile, spalte) durch und streiche 
		for (int i =0; i<tae.size(); i++ ) {
			
			int r = ((SudokuUnsolvedItem) tae.get(i)).row; int c = ((SudokuUnsolvedItem) tae.get(i)).column;

			if ( (r == row) || (c == col) ) {
				((SudokuUnsolvedItem)tae.get(i)).getValues().remove(lookFor);
			}
				 
			//dasselbe für triple
			else if ( (r/3 == row/3) && (c/3 == col/3) )  {
				((SudokuUnsolvedItem)tae.get(i)).getValues().remove(lookFor);
			}
		}
	}

/*	private void addInVectors (int row, int col, Vector tae, Integer lookFor) {
		
		// suche Vektor tae(zeile, spalte) durch und füge Möglichkeiten hinzu 
		for (int i =0; i<tae.size(); i++ ) {
			
			int r = ((SudokuUnsolvedItem) tae.get(i)).row; int c = ((SudokuUnsolvedItem) tae.get(i)).column;

			if ( (r == row) || (c == col) ) {
				((SudokuUnsolvedItem)tae.get(i)).getValues().add(lookFor);
			}
				 
			//dasselbe für triple
			else if ( ((int) r/3 == (int) row/3) && ((int) c/3 == (int) col/3) )  {
				((SudokuUnsolvedItem)tae.get(i)).getValues().add(lookFor);
			}
		}

		Vector v = new Vector();
		v.add (lookFor); 
		SudokuUnsolvedItem buff = new SudokuUnsolvedItem (row, col, 1, v);
		tae.add(0, buff);
	}*/

	private Vector copyAndOrderANewSudokuVector (Vector from) {
		
		Vector to = new Vector(); // try and error

		// taeStart in tae kopieren
		for (int i =0; i<from.size(); i++ ) {
			SudokuUnsolvedItem buffer = new SudokuUnsolvedItem (
				((SudokuUnsolvedItem) from.get(i)).row, ((SudokuUnsolvedItem) from.get(i)).column, 
				((SudokuUnsolvedItem)from.get(i)).openPossibilities(), 
				(Vector) ((SudokuUnsolvedItem)from.get(i)).values.clone()
			);

			int pos=0, same=0;
			for (int k=0; k<to.size(); k++) {
				if ( ((SudokuUnsolvedItem) to.get(k)).compareTo(buffer) < 0 )
					pos++; // aufsteigende Liste hiermit realisiert
				if ( ((SudokuUnsolvedItem) to.get(k)).compareTo(buffer) == 0 )
					same++; // Anzahl gleiche Einträge wegen zufälliger Untermischung
			}
			pos = pos + r.nextInt(same+1); // mischt zufällig unter
			to.add (pos, buffer); // jetzt stehen die kleinen Vektoren vorne im tae-Vektor
		}
		return to;
	} 

	private SudokuUnsolvedItem copyFirstIteminSudokuVector (Vector from) {

		SudokuUnsolvedItem to = new SudokuUnsolvedItem (
			((SudokuUnsolvedItem) from.get(0)).row, ((SudokuUnsolvedItem) from.get(0)).column, 
			((SudokuUnsolvedItem)from.get(0)).openPossibilities(), 
			(Vector) ((SudokuUnsolvedItem)from.get(0)).values.clone()
		);
		to.shakeVector();
		return to;
	}

	/** löscht aus sv die ausschließbaren Zahlen, sodass nur die offenen übrig bleiben */
	private void initVectors(SudokuItem s[][], Vector sv[][]) {

		for (int zeile=0; zeile<9; zeile++) {
			for (int spalte=0; spalte<9; spalte++) {
				if ( s[zeile][spalte].isKnown() ) continue;

				// Zeile & Spalte
				for (int i=0; i<9; i++) {
					bereinige (zeile, spalte, s[zeile][i].getValue(), sv);
					bereinige (zeile, spalte, s[i][spalte].getValue(), sv);
				}

				// Quadrat
				int tripleX = ( zeile/3 ) *3; int tripleY = ( spalte/3 ) *3;
				for (int j1=0; j1<3; j1++)
					for (int j2=0; j2<3; j2++)
						bereinige (zeile, spalte, s[tripleX+j1][tripleY+j2].getValue(), sv);

			}
		}
	}

	private void bereinige (int zeile, int spalte, int wert, Vector sv[][]) {
		if (wert != 0) {
			sv[zeile][spalte].remove( new Integer (wert) );
		}
	}
	
	private void initCheckVector(SudokuItem s[][], Vector sv[][]) {
		
		solveFast = false;
		taeCheck.removeAllElements();
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				// sudokuTry[i][j] = generated[i][j].getValue();
				if (!s[i][j].isKnown()) {

					SudokuUnsolvedItem buffer = new SudokuUnsolvedItem(i, j, sv[i][j].size(), sv[i][j]);
					int pos = 0; // boolean equal = false;
					for (int k = 0; k < taeCheck.size(); k++) {
						if (((SudokuUnsolvedItem) taeCheck.get(k)).compareTo(buffer) < 0)
							pos++; // aufsteigende Liste hiermit realisiert
					}
					taeCheck.add(pos, buffer); // jetzt stehen die kleinen Vektoren vorne im tae-Vektor
				}
			}
		}
	}



	/*private void writeTryResultsForDebuggingReasons (Vector tae) {
		
		resetSudoku();

		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				stf[r][c].setBackground(Color.LIGHT_GRAY);
				stf[r][c].setText ("" + sudokuTry[r][c]);
			}
		}

		for (int i = 0; i < tae.size(); i++) {

			int row = ((SudokuUnsolvedItem) tae.get(i)).row;
			int col = ((SudokuUnsolvedItem) tae.get(i)).column;
			String vals = ((SudokuUnsolvedItem) tae.get(i)).getValues().toString();
			stf[row][col].setText (stf[row][col].getText () + " " + vals);
		}
	}
		*/

	private boolean removeField (int [][]matrix, int zeile, int spalte) {
		
		int buffer = matrix[zeile][spalte];

		if ( buffer == 0 ) return false;
		
		matrix[zeile][spalte] = 0;
		
		SudokuItem s[][] = new SudokuItem[9][9];
		Vector sv [][] = new Vector[9][9];

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				s[i][j] = new SudokuItem (matrix[i][j]);

				if ( ! s[i][j].isKnown() ) {
					sv[i][j] = new Vector(9);
					for (int k = 1; k < 10; k++)
						sv[i][j].addElement(new Integer (k));
				}
			}
		}

		initVectors(s, sv);
		initCheckVector(s, sv); // TODO was passiert hier, kann sv oder andere Sachen zu Beschl. mehrfach genutzt werden etc.??

		countOfSolvesInTry = 0;
		int test = checkSolve();

		if ( (test != ALLRIGHT) || (countOfSolvesInTry != 1) ) {
			matrix[zeile][spalte] = buffer;
			return false;
		} else return true;

		/*TryAndErrorTester tae = new TryAndErrorTester(s, sv);

		if (tae.getSolution() != 0) {
			if (tae.getCountOfSolutionsWithMax100() > 1) {
				matrix[zeile][spalte] = buffer;
				//removeInVectors(zeile, spalte, taeCheck, new Integer (buffer));
				//taeCheck.remove(0);
			}
		}*/
		
		
	}

}
