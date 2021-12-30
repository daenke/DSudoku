/*
 * Created on 19.05.2007
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author heida
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SudokuExamples {

	/* schwerste Sudoku der Welt lt. telegraph */
	private static  int [] example14 = new int [] {
			8, 0, 0,  0, 0, 0,  0, 0, 0,   
			0, 0, 3,  6, 0, 0,  0, 0, 0,   
			0, 7, 0,  0, 9, 0,  2, 0, 0,   

			0, 5, 0,  0, 0, 7,  0, 0, 0,   
			0, 0, 0,  0, 4, 5,  7, 0, 0,   
			0, 0, 0,  1, 0, 0,  0, 3, 0,   

			0, 0, 1,  0, 0, 0,  0, 6, 8,   
			0, 0, 8,  5, 0, 0,  0, 1, 0,   
			0, 9, 0,  0, 0, 0,  4, 0, 0,   
		};

	// 20.05.21 schweres sudoku aus dem SW testing Kurs von udacity
	private static  int [] example15 = new int [] {
			1, 0, 0,  0, 0, 7,  0, 9, 0,   
			0, 3, 0,  0, 2, 0,  0, 0, 8,   
			0, 0, 9,  6, 0, 0,  5, 0, 0,   

			0, 0, 5,  3, 0, 0,  9, 0, 0,   
			0, 1, 0,  0, 8, 0,  0, 0, 2,   
			6, 0, 0,  0, 0, 4,  0, 0, 0,   

			3, 0, 0,  0, 0, 0,  0, 1, 0,   
			0, 4, 0,  0, 0, 0,  0, 0, 7,   
			0, 0, 7,  0, 0, 0,  3, 0, 0,   
		};
	
	/*
		private static  int [] example00 = new int [] {
			0, 0, 0,  0, 0, 0,  0, 0, 0,   
			0, 0, 0,  0, 0, 0,  0, 0, 0,   
			0, 0, 0,  0, 0, 0,  0, 0, 0,   

			0, 0, 0,  0, 0, 0,  0, 0, 0,   
			0, 0, 0,  0, 0, 0,  0, 0, 0,   
			0, 0, 0,  0, 0, 0,  0, 0, 0,   

			0, 0, 0,  0, 0, 0,  0, 0, 0,   
			0, 0, 0,  0, 0, 0,  0, 0, 0,   
			0, 0, 0,  0, 0, 0,  0, 0, 0,   
		};
	 */

	private static int [] example01 = new int [] {
			0, 0, 0,  1, 0, 9,  0, 4, 0,   
			0, 7, 0,  0, 0, 6,  0, 0, 9,   
			0, 0, 2,  0, 4, 0,  0, 0, 0,   

			4, 0, 0,  9, 0, 0,  0, 5, 3,   
			0, 0, 6,  0, 0, 0,  9, 0, 0,   
			3, 2, 0,  0, 0, 7,  0, 0, 6,   

			0, 0, 0,  0, 5, 0,  6, 0, 0,   
			2, 0, 0,  8, 0, 0,  0, 3, 0,   
			0, 5, 0,  6, 0, 3,  0, 0, 0,   
		};
		
	private static int [] example02 = new int [] {
			0, 1, 7,  0, 0, 4,  2, 0, 0,   
			0, 0, 0,  0, 0, 0,  9, 7, 0,   
			0, 0, 0,  7, 3, 5,  0, 0, 0,   

			0, 0, 0,  9, 0, 1,  0, 0, 0,   
			9, 0, 0,  2, 0, 0,  6, 0, 0,   
			7, 0, 8,  0, 0, 0,  5, 0, 0,   

			8, 4, 0,  0, 0, 0,  1, 0, 0,   
			0, 0, 0,  3, 0, 0,  0, 8, 0,   
			0, 0, 5,  6, 0, 0,  0, 2, 0,   
		};

	private static int [] example03 = new int [] {
			0, 7, 0,  6, 0, 0,  0, 8, 0,   
			6, 0, 0,  5, 0, 0,  0, 0, 0,   
			0, 0, 9,  0, 0, 4,  0, 5, 0,   

			1, 2, 0,  0, 0, 0,  9, 6, 0,   
			0, 0, 0,  0, 2, 0,  4, 0, 0,   
			0, 0, 6,  0, 0, 0,  0, 0, 0,   

			0, 0, 0,  4, 3, 0,  1, 0, 7,   
			2, 0, 5,  1, 0, 0,  0, 0, 0,   
			0, 0, 0,  0, 0, 0,  8, 0, 0,   
		};

	private static int [] example04 = new int [] {
			0, 2, 6,  0, 9, 0,  4, 3, 0,   
			7, 8, 0,  0, 0, 0,  0, 9, 2,   
			0, 0, 3,  0, 0, 0,  1, 0, 0,   

			0, 4, 0,  1, 0, 9,  0, 5, 0,   
			0, 0, 0,  0, 4, 0,  0, 0, 0,   
			0, 5, 0,  7, 0, 6,  0, 4, 0,   

			0, 0, 5,  0, 0, 0,  3, 0, 0,   
			4, 7, 0,  0, 0, 0,  0, 8, 6,   
			0, 3, 9,  0, 7, 0,  2, 1, 0,   
		};

	private static int [] example05 = new int [] {
			5, 7, 0,  0, 0, 0,  0, 2, 1,   
			0, 0, 8,  0, 0, 0,  4, 0, 0,   
			0, 0, 0,  4, 0, 7,  0, 0, 0,   

			6, 0, 0,  5, 0, 2,  0, 0, 8,   
			8, 1, 0,  0, 0, 0,  0, 3, 5,   
			3, 0, 0,  8, 0, 1,  0, 0, 6,   

			0, 0, 0,  9, 0, 4,  0, 0, 0,   
			0, 0, 5,  0, 0, 0,  7, 0, 0,   
			2, 6, 0,  0, 0, 0,  0, 9, 4,   
		};

	private static int [] example06 = new int [] {
		0, 5, 0,  0, 6, 3,  9, 0, 4,   
		0, 0, 0,  0, 0, 0,  2, 0, 0,   
		0, 3, 0,  9, 0, 2,  0, 0, 8,   

		0, 2, 0,  0, 0, 7,  0, 0, 0,   
		0, 0, 1,  5, 0, 4,  7, 0, 0,   
		0, 0, 0,  2, 0, 0,  0, 5, 0,   

		2, 0, 0,  7, 0, 9,  0, 8, 0,   
		0, 0, 8,  0, 0, 0,  0, 0, 0,   
		4, 0, 5,  1, 2, 0,  0, 7, 0,   
	};

	private static int [] example07 = new int [] {
		0, 0, 0,  0, 6, 5,  0, 0, 0,   
		5, 0, 0,  4, 0, 9,  0, 0, 3,   
		3, 0, 0,  0, 0, 0,  4, 0, 6,   

		0, 0, 1,  2, 0, 0,  0, 8, 0,   
		0, 7, 0,  0, 0, 0,  0, 0, 4,   
		0, 0, 6,  7, 0, 0,  0, 3, 0,   

		6, 0, 0,  0, 0, 0,  5, 0, 1,   
		9, 0, 0,  6, 0, 3,  0, 0, 8,   
		0, 0, 0,  0, 8, 4,  0, 0, 0,   
	};

	private static int [] example08 = new int [] {
		0, 0, 0,  4, 0, 0,  1, 5, 0,   
		0, 8, 3,  0, 0, 5,  0, 0, 2,   
		4, 0, 1,  0, 0, 0,  0, 0, 9,   

		0, 0, 0,  0, 4, 0,  5, 0, 8,   
		0, 3, 0,  6, 0, 0,  0, 7, 0,   
		7, 0, 4,  0, 8, 0,  0, 0, 0,   

		1, 0, 0,  0, 0, 0,  2, 0, 7,   
		3, 0, 0,  2, 0, 0,  9, 6, 0,   
		0, 2, 6,  0, 0, 3,  0, 0, 0,   
	};

	private static  int [] example09 = new int [] {
		0, 1, 0,  0, 0, 0,  0, 0, 0,   
		6, 0, 0,  0, 1, 0,  0, 0, 0,   
		3, 0, 0,  4, 0, 2,  0, 0, 7,   

		0, 0, 2,  5, 0, 0,  0, 0, 0,   
		1, 0, 0,  0, 0, 0,  9, 7, 3,   
		0, 0, 6,  0, 0, 3,  0, 0, 0,   

		0, 0, 0,  0, 0, 0,  0, 0, 8,   
		0, 8, 7,  0, 0, 0,  0, 4, 2,   
		5, 0, 0,  0, 0, 6,  0, 0, 0,   
	};

	private static  int [] example10 = new int [] {
		2, 0, 0,  0, 0, 0,  0, 0, 1,   
		4, 0, 6,  7, 0, 3,  9, 0, 8,   
		0, 0, 9,  6, 0, 5,  3, 0, 0,   

		5, 0, 0,  1, 0, 4,  0, 0, 9,   
		0, 0, 1,  0, 3, 0,  7, 0, 0,   
		7, 0, 0,  5, 0, 6,  0, 0, 4,   

		0, 0, 8,  4, 0, 2,  1, 0, 0,   
		1, 0, 5,  3, 0, 7,  4, 0, 2,   
		3, 0, 0,  0, 0, 0,  0, 0, 6,   
	};

	private static  int [] example11 = new int [] {
		0, 0, 5,  0, 0, 0,  3, 0, 0,   
		6, 2, 0,  0, 0, 0,  0, 8, 1,   
		0, 7, 0,  6, 4, 1,  0, 9, 0,   

		0, 0, 0,  0, 7, 0,  0, 0, 0,   
		2, 0, 0,  9, 0, 8,  0, 0, 3,   
		0, 0, 0,  0, 6, 0,  0, 0, 0,   

		0, 4, 0,  2, 1, 5,  0, 3, 0,   
		5, 1, 0,  0, 0, 0,  0, 6, 4,   
		0, 0, 3,  0, 0, 0,  2, 0, 0,   
	};

	private static  int [] example12 = new int [] {
		6, 3, 0,  7, 0, 5,  0, 0, 8,   
		7, 0, 0,  0, 0, 9,  0, 0, 0,   
		0, 0, 2,  4, 0, 0,  3, 0, 0,   

		8, 0, 1,  0, 0, 0,  0, 0, 0,   
		0, 0, 0,  0, 0, 0,  0, 7, 1,   
		9, 4, 0,  0, 0, 7,  0, 0, 0,   

		0, 0, 6,  0, 0, 0,  0, 0, 5,   
		0, 0, 0,  0, 8, 0,  0, 3, 0,   
		5, 0, 0,  0, 2, 0,  8, 0, 0,   
	};

	private static  int [] example13 = new int [] {
		0, 0, 0,  0, 1, 2,  0, 6, 0,   
		0, 0, 0,  0, 0, 0,  0, 7, 3,   
		0, 0, 0,  0, 0, 0,  8, 0, 0,   

		0, 0, 0,  0, 0, 3,  0, 0, 8,   
		7, 0, 0,  0, 0, 4,  2, 5, 0,   
		5, 0, 0,  2, 8, 0,  0, 0, 0,   

		0, 0, 4,  0, 9, 0,  0, 0, 7,   
		2, 7, 0,  0, 5, 0,  0, 0, 0,   
		0, 8, 0,  6, 0, 0,  5, 0, 0,   
	};


	static int example[][] = { example01, example02,  example03, example04, example05, example06,
		example07, example08, example09, example10, example11, example12, example13, example14, example15};

	public static int[] getExample (int number) {
		return example[number];
	}

	public static int size () {
		return example.length;
	}

}
