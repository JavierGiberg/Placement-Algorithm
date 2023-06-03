import java.util.Scanner;

public class main {
	/*
	 * Javier Giberg
	 */
	public static void main(String[] args) {
		boolean EXIT = false;
		Scanner sc = new Scanner(System.in);

		// 1. Initialization
		String[] matrix = Initialization(sc);
		String Algorithm = matrix[0];
		matrix[0] = "EMPTY";
		if (Algorithm.equals("NextFit"))
			matrix[0] = "NEXT";
		PrintStatus(matrix);
		// Initialization

		// 2. Runnable
		runningManager(matrix, sc, EXIT, Algorithm);
		// Runnable

		// 3. Release of resources
		sc.close();
		// Release of resources
	}

	/*
	 * Algorithm FirstFit
	 */
	static String[] FirstFit(String command, String[] matrix, Process process) {
		switch (command) {
		case "Enter process": {
			int sumFreeMemory = 0;
			int fragmentation = 0;
			int location = 0;
			boolean flag = false;
			for (int i = 0; i < matrix.length; i++) {
				if (matrix[i].equals("EMPTY")) {
					sumFreeMemory++;
					fragmentation++;
				} else
					sumFreeMemory = 0;
				if (sumFreeMemory == process.sizeBytes) {
					location = i + 1 - process.sizeBytes;
					flag = true;
					break;
				}
			}
			if (flag) {
				flag = false;
				for (int i = location; i < location + process.sizeBytes; i++) {
					matrix[i] = process.getName();
				}
			} else {
				if (process.getSizeBytes() < fragmentation)
					System.out.println("the fragmentation size is : " + fragmentation);
			}
			return matrix;
		}
		case "Exit process": {
			for (int i = 0; i < matrix.length; i++) {
				if (matrix[i].equals(process.getName()))
					matrix[i] = "EMPTY";
			}
		}
		default:
			return matrix;
		}
	}
	/*
	 * Algorithm NextFit
	 */

	static String[] NextFit(String command, String[] matrix, Process process) {
		int NEXT = 0;

		for (int i = 0; i < matrix.length; i++) {
			if (matrix[i].equals("NEXT")) {
				NEXT = i;
				break;
			}
		}
		switch (command) {
		case "Enter process": {
			int sumFreeMemory = 0;
			int fragmentation = 0;
			int location = 0;
			boolean flag = false;
			for (int i = NEXT; i < matrix.length + NEXT; i++) {
				if (matrix[i % matrix.length].equals("EMPTY") || matrix[i % matrix.length].equals("NEXT")) {
					sumFreeMemory++;
					fragmentation++;
				} else
					sumFreeMemory = 0;
				if (sumFreeMemory == process.sizeBytes) {
					location = i + 1 - process.sizeBytes;
					flag = true;
					break;
				}
			}
			if (flag) {
				flag = false;
				for (int i = location; i < location + process.sizeBytes; i++) {
					matrix[i % matrix.length] = process.getName();
				}
				if (matrix[(location + process.sizeBytes) % matrix.length].equals("EMPTY"))
					matrix[(location + process.sizeBytes) % matrix.length] = "NEXT";
			} else {
				if (process.getSizeBytes() <= fragmentation)
					System.out.println("the fragmentation size is : " + fragmentation);
			}
			return matrix;
		}

		case "Exit process": {
			for (int i = 0; i < matrix.length; i++) {
				if (matrix[i].equals(process.getName()))
					matrix[i] = "EMPTY";
			}
		}
		default:
			return matrix;
		}

	}

	/*
	 * Algorithm BestFit
	 */
	// ------------------------------------------------------------------------------------------------
	static String[] BestFit(String command, String[] matrix, Process process) {
		switch (command) {
		case "Enter process": {
			int fragmentation = 0;
			int minHole = matrix.length;
			int spaceFreeCounter = 0;
			int Index = 0;
			boolean flag = false;
			boolean indexFlag = true;

			for (int i = 0; i < matrix.length; i++) {
				if (matrix[i].equals("EMPTY")) {
					if (indexFlag == false && spaceFreeCounter < minHole
							&& spaceFreeCounter >= process.getSizeBytes()) {
						indexFlag = true;
						Index = i - spaceFreeCounter;
					}
					if (spaceFreeCounter >= process.getSizeBytes())
						flag = true;
					spaceFreeCounter++;
					fragmentation++;
				} else {
					indexFlag = false;
					if (spaceFreeCounter < minHole && spaceFreeCounter >= process.getSizeBytes()) {
						minHole = spaceFreeCounter;
						Index = i - spaceFreeCounter;
						flag = true;
					}
					spaceFreeCounter = 0;
				}
			}
			if (spaceFreeCounter < minHole && spaceFreeCounter >= process.getSizeBytes()) {
				minHole = spaceFreeCounter;
				Index = matrix.length - spaceFreeCounter;
				flag = true;
			}
			System.out.println("the index is : " + Index);
			if (flag) {
				flag = false;
				for (int i = Index; i < process.sizeBytes + Index; i++) {
					matrix[i] = process.getName();
				}

			} else {
				if (process.getSizeBytes() <= fragmentation)
					System.out.println("the fragmentation size is : " + fragmentation);
			}
			return matrix;
		}

		case "Exit process": {
			for (int i = 0; i < matrix.length; i++) {
				if (matrix[i].equals(process.getName()))
					matrix[i] = "EMPTY";
			}
			System.out.println("process: " + process.getName() + " need delete");
		}
		default:
			return matrix;
		}

	}

	/*
	 * PrintStatus
	 */
	static void PrintStatus(String[] matrix) {

		System.out.println("       SIZE MEMORY ");

		for (int i = 0; i < matrix.length; i++) {
			System.out.println("                       ______________");
			System.out.println("      [" + String.format("%03d", i) + "]" + " byte -->  |   " + matrix[i] + "      |");
			System.out.println("                       ¯¯¯¯¯¯¯¯¯¯¯¯¯¯  ");
		}

	}

	/*
	 * runningManager
	 */
	static void runningManager(String[] matrix, Scanner sc, boolean EXIT, String Algorithm) {

		while (!EXIT) {
			try {
				int key = Menu(sc);

				switch (key) {
				case 1: {
					System.out.println("Enter num of Process:");
					int numPro = sc.nextInt();
					System.out.println("Enter size of Memory this procees needs:");
					int memory = sc.nextInt();

					switch (Algorithm) {// FirstFit Algorithm
					case "FirstFit": {
						Process process = new Process(numPro, memory);
						matrix = FirstFit("Enter process", matrix, process);
						break;
					}
					case "NextFit": {// NextFit Algorithm
						Process process = new Process(numPro, memory);
						matrix = NextFit("Enter process", matrix, process);
						break;
					}
					case "BestFit": {// BestFit Algorithm
						Process process = new Process(numPro, memory);
						matrix = BestFit("Enter process", matrix, process);
						break;
					}

					}

					break;
				}
				case 2: {
					System.out.println("Enter num of Process:");
					int numPro = sc.nextInt();
					switch (Algorithm) {// FirstFit Algorithm
					case "FirstFit": {
						Process process = new Process(numPro, numPro);
						matrix = FirstFit("Exit process", matrix, process);
						break;
					}
					case "NextFit": {
						Process process = new Process(numPro, numPro);
						matrix = NextFit("Exit process", matrix, process);
						break;
					}
					case "BestFit": {// BestFit Algorithm
						Process process = new Process(numPro, numPro);
						matrix = BestFit("Exit process", matrix, process);
						break;
					}
					}

					break;
				}
				case 3: {
					PrintStatus(matrix);
					break;
				}
				case 4: {
					System.out.println("You choice done , tanks");
					EXIT = true;
					break;
				}
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	/*
	 * Initialization
	 */
	static String[] Initialization(Scanner sc) {
		boolean flag = true;
		// System.out.println("Enter size of memory (A number that is a power of two!!)
		// :");
		int size = 3;
		while (!powerOfTwoGeneral(size)) {
			try {
				System.out.println("Enter size of memory (A number that is a power of two!!) :");
				size = sc.nextInt();

			} catch (Exception e) {
				System.out.println("try again");
				sc.next();

			}
		}

		String[] matrix = new String[size];
		for (int i = 0; i < matrix.length; i++) {
			matrix[i] = "EMPTY";
		}

		while (flag) {
			try {
			System.out.println("Choice you Algo");
			System.out.println("1.FirstFit ");
			System.out.println("2.NextFit");
			System.out.println("3.BestFit");
			int choice = sc.nextInt();
			switch (choice) {

			case 1: {
				matrix[0] = "FirstFit";
				flag = false;
				break;
			}
			case 2: {
				matrix[0] = "NextFit";
				flag = false;
				break;
			}
			case 3: {
				matrix[0] = "BestFit";
				flag = false;
				break;
			}
			}
			if (choice < 1 || choice > 3)
				System.out.println("try again");
		} catch (Exception e) {
				sc.next();
			}

		}

		return matrix;
	}

	/*
	 * Menu
	 */
	static int Menu(Scanner sc) {
		int choice = 0;
		while (choice < 1 || choice > 4) {
			System.out.println("1.Enter process \n" + "2.Exit process \n" + "3.Print status \n" + "4.Exit \n"
					+ "Enter your choice please :");
			try {
				choice = sc.nextInt();
			} catch (Exception e) {
				System.out.println("try choice : 1 , 2 , 3 , 4");
				sc.next();
			}
		}
		return choice;
	}

	public static boolean powerOfTwoGeneral(int n) {
		while (n % 2 == 0) {
			n = n / 2;
		}
		if (n == 1) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * class Process
	 */
	public static class Process {
		private String process;
		private int sizeBytes;

		public Process(int name, int sizeBytes) {
			this.process = "P" + String.format("%02d", name) + "  ";
			this.sizeBytes = sizeBytes;
		}

		public String getName() {
			return process;
		}

		public int getSizeBytes() {
			return sizeBytes;
		}

	}
}
