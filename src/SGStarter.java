/*
 * File: SGStarter.java
 * Created for Projekt: SudokuSolver
 * Created on 02.09.2007 by Daniel Enke
 */

/**
 * Klasse zum Auslösen der Threads zum Generieren des Sudoku
 * und als Unterhaltung zum Starten verschiedener Animationen als Thread
 * @author Daniel Enke
 */
public class SGStarter implements Runnable {

	private Thread generating = null;
	SudokuGenerator sg;
	int smatrix [][] = new int [9][9];
	SudokuMainframe parent;

	public synchronized void start (int matrix[][], SudokuMainframe sm) {
		if (generating == null) {
			generating = new Thread (this);
			smatrix = matrix; parent = sm;
			generating.start();
		} else {
			try {
				generating.interrupt();
				generating = null;
			} catch (Exception ex) { }
		}
	}

	@Override
	public void run() {

		SudokuGenerator sg = new SudokuGenerator(parent);
		sg.generate(smatrix);
		parent.fillGeneratedSudoku();

		try {
			generating.interrupt();
			generating = null;
		} catch (Exception ex) { }

	}

}
