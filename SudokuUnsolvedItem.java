import java.util.*;
/*
 * Created on 02.06.2007
 */

/**
 * Klasse mit allen Eigenschaften eines ungelösten Sudoku-Feldes zum Speichern in einem Vektor, 
 * wie der Feld-Position, einem Vektor mit allen möglichen Lösungen für dieses Feld, dieses Vektors 
 * Länge (sprich: wieviele offene Lösungen gibt es für dieses Feld) und einem Generator für 
 * Zufallszahlen, um die Vektor zu schütteln (shakeVector), damit ihre erste Lösungsmöglichkeit zufällig
 * belegt ist und nicht immer gleiche Lösungen erraten werden
 * @author Daniel Enke
 */
class SudokuUnsolvedItem implements Comparable {

	int vSize;
	int row, column;
	int sudokuValue;
	Vector values = new Vector();
	static Random r = new Random ( System.currentTimeMillis() );
	
	SudokuUnsolvedItem (int row, int column, int sudokuValue) {
		this.row = row; this.column = column; this.sudokuValue = sudokuValue; 
	}
	
	SudokuUnsolvedItem (int row, int column, int vectorSize, Vector values) {
		this.row = row; this.column = column; this.vSize = vectorSize; 
		this.values = (Vector) values.clone();
	}
	
	public boolean equals (SudokuUnsolvedItem other) {
		int thisRow = this.row; int otherRow = other.row;
		int thisColumn = this.column; int otherColumn = other.column;
		return (  (thisRow == otherRow) && (thisColumn == otherColumn) );
	}
	
	public int compareTo (SudokuUnsolvedItem other) {
		return (this.vSize<other.vSize ? -1 : (this.vSize==other.vSize ? 0 : 1));
	}
	
	@Override
	public int compareTo(Object other) {
		return compareTo((SudokuUnsolvedItem)other);
	}
	
	public Vector getValues() {
		return this.values;
	}
	
	public int openPossibilities () {
		return this.values.size();
	}

	public void shakeVector() {
		
		Vector buffer = (Vector) this.values.clone();
		this.values.clear();
		for (int i=0; i< buffer.size(); i++) this.values.add(r.nextInt(i+1), buffer.get(i));
	}
	

}
