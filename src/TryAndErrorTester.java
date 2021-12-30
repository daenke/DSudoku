import java.util.*;

/*
 * Created on 06.06.2007
 *
 */

/**
 * @author heida
 *
 */
public class TryAndErrorTester {

	// Variablen für Try&Error (tae)
	private int sudokuTry [][] = new int [9][9]; // TODO eigentlich keine funktionelle Notwendigkeit mehr -> rausstreichen bei Gelegenheit
	private Vector taeStart = new Vector(); // try and error

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

	private static Random r = new Random ( System.currentTimeMillis() );
	
	/** kopiert in Vorbereitung des try&error-Algorithmus das Sudoku-Array in sudokuTry
	 *  und erstellt eine sortedList tae aller ungelösten Felder mit ihren SudokuVektoren (sv)*/
	public TryAndErrorTester (SudokuItem[][] testMatrix, Vector sv[][]) {
		
		solveFast = false;
		
		taeStart.removeAllElements();
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				sudokuTry[i][j] = testMatrix[i][j].getValue();
				if ( ! testMatrix[i][j].isKnown() ) {
					SudokuUnsolvedItem buffer = new SudokuUnsolvedItem ( i, j, sv[i][j].size(), sv[i][j] );
					int pos=0, same=0; // boolean equal = false;
					for (int k=0; k<taeStart.size(); k++) {
						// if ( ((SudokuItem) tae.get(k)).equals(buffer))
							// equal = true; // ursprünglich für Einmaligkeit gedacht nun eigentlich nicht mehr notwendig
						if ( ((SudokuUnsolvedItem) taeStart.get(k)).compareTo(buffer) < 0 )
							pos++; // aufsteigende Liste hiermit realisiert
						if ( ((SudokuUnsolvedItem) taeStart.get(k)).compareTo(buffer) == 0 )
							same++; // Anzahl gleiche Einträge wegen zufälliger Untermischung
					}
					// if ( ! equal ) 
					pos = pos + r.nextInt(same+1); // mischt zufällig unter
					taeStart.add (pos, buffer); // jetzt stehen die kleinen Vektoren vorne im tae-Vektor
				}
			}
		}
	}

	/** versucht mit Try&Error Lösungsmöglichkeiten zu suchen oder auszuschließen
	 * firstCall bei Nutzeraufruf immer "true"
	 * @return Status-werte (ALLRIGHT, UNCRACKED oder ERRORFOUND)
	 */
	public int taeSolve () {
		checkForAll = GIVE_ALL_SOLUTIONS;
		return taeSolveRecursive(true, taeStart);
	}

	public int getSolution() {
		if (okayValue == 0) taeSolve();
		
		return okayValue;
	}
	
	public int getRowOfSolution() {
		
		return rowOfSolution;
	}
	
	public int getColumnOfSolution() {
		
		return colOfSolution;
	}
	
	public int getCountOfSolutionsWithMax100() {
		return countOfSolvesInTry;
	}
	
	public boolean isAnySolution (int row, int col, int value) {
		checkForAll = GIVE_ONLY_SINGULAR_SOLUTION;
		lookInVectors(row, col, taeStart, new Integer (value));
		return (taeSolveRecursive(true, taeStart) == ALLRIGHT) ? true : false;
	}
	
	/** sucht rekursiv eine oder mehrere Lösungen
	 * 
	 * @param firstCall - immer true (benötigt für Rekursivität)
	 * @param formerTae - ein geordneter tae-Vektor (siehe Konstruktor) 
	 * @return Statuswert als int
	 */
	private int taeSolveRecursive (boolean firstCall, Vector formerTae) {

		status = UNDEFINED;

		// 1. Eintrag extrahieren - dann löschen
		SudokuUnsolvedItem firstItem = copyFirstIteminSudokuVector (formerTae);
		formerTae.remove(0);

		// Verzweigungsende: keine mögliche Zahl
		if ( firstItem.openPossibilities() == 0 ) return UNCRACKED;

		// letztes offenes Feld mit genau einer Eintragmöglichkeit => alles okay
		if ( formerTae.size() == 0)
			if ( firstItem.openPossibilities() == 1 ) {
				countOfSolvesInTry ++;
				if ( ! firstTimeAllRightWasSeen) {
					if (testedValue != 0) okayValue = testedValue;
					else okayValue = ((Integer) firstItem.getValues().get(0)).intValue(); // falls nur noch ein Feld offen war
					firstTimeAllRightWasSeen = true;
				} 
				return ALLRIGHT;
		}

		for (int i=0; i < firstItem.openPossibilities() ; i++) {
			Vector tae = copyAndOrderANewSudokuVector (formerTae); 
			Integer lookFor = new Integer ( ((Integer) firstItem.getValues().get(i)).intValue() );

			if (firstCall) { // beim ersten Einstieg wird dieser Bereich abgearbeitet
				rowOfSolution = firstItem.row; colOfSolution = firstItem.column;
				testedValue = lookFor.intValue();
			} 

			sudokuTry[firstItem.row][firstItem.column] = lookFor.intValue(); // wozu wird sudokuTry noch verwendet??
			
			// doppelte Zahlen löschen in Vektoren eines Blockes (row, col, triple)
			//	boolean aus remove wäre auswertbar (wenn false continue hier)
			lookInVectors (firstItem.row, firstItem.column, tae, lookFor);

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

	private void lookInVectors (int row, int col, Vector tae, Integer lookFor) {
		
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

}
