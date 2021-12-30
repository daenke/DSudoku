/*
 * Created on 02.06.2007
 *
 */

/**
 * Klasse mit allen Eigenschaften eines Sudoku-Feldes, wie dem Status (nicht initialisiert, kommentiert,
 * gesucht / unbekannt, bekannt, gegeben), dem enthaltenen Zahlenwert und einem Kommentar-Text 
 * @author Daniel Enke
 */
class SudokuItem {

	static private final int NOT_INITIALIZED = -2;
	static private final int COMMENT = -1;
	static private final int UNKNOWN = 0;
	static private final int KNOWN = 1; // known all have to be bigger then 0 
	static private final int WASGIVEN = 2;

	private int value;
	private int state;
	private String comment;
	
	SudokuItem () { // zum reset und gezielten leeren
		this.state = NOT_INITIALIZED;
		this.value = 0;
		this.comment = "";
	}
	
	SudokuItem (int value) {
		
		if ( (value > 0) && (value < 10) ) {
			this.state = WASGIVEN;
			this.value = value;
		} 
		else {
			this.state = UNKNOWN;
			this.value = 0;
		} 
		this.comment = "";
	}
	
	public void setValue (int value) {
		
		if (this.wasGiven()) return;
		
		if ( (value > 0) && (value < 10) ) {
			this.state = KNOWN;
			this.value = value;
		} 
		else {
			this.state = UNKNOWN;
			this.value = 0;
		} 
		this.comment = "";
	}
	
	public void setComment (String  comment) {
		
		if ( (comment.length() > 0) && ( ! comment.endsWith("]") ) ) {
			this.state = COMMENT;
			this.comment = comment;
			this.value = 0;
		} else {
			this.state = UNKNOWN;
			this.value = 0;
			this.comment = "";
		}
	} 
	
	public int getValue() {
		return this.value;
	}
	
	public String getComment () {
		return comment;
	}

	public boolean isKnown () {
		return (state > 0 ? true : false);
	}

	public boolean wasGiven () {
		return (state == WASGIVEN ? true : false);
	}

	public boolean isComment () {
		return (state == COMMENT ? true : false);
	}

	public boolean isNotInitialized () {
		return (state == NOT_INITIALIZED ? true : false);
	}
}
