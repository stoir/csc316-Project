package edu.ncsu.csc316.movie.ui;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

import edu.ncsu.csc316.movie.manager.ReportManager;

/**
 * movie manager ui class for bbts
 * 
 * @author sanjanacheerla
 *
 */
public class MovieManagerUI {

	/**
	 * main method for user interface
	 * 
	 * @param args not used
	 * @throws FileNotFoundException if trouble reading files
	 * @throws ParseException        if trouble reading files
	 */
	public static void main(String[] args) throws FileNotFoundException, ParseException {
		@SuppressWarnings("resource")
		Scanner s = new Scanner(System.in);

		System.out.println("Welcome to Movie Manager!");
		ReportManager rm = null;

		while (true) {
			System.out.print("Enter q to quit at anytime.\n");
			System.out.print("Enter movie record file: ");
			String inMovieRecord = s.next();

			if (inMovieRecord.equals("q")) {
				System.out.println("Bye!");
				return;
			}

			System.out.print("Enter watch record file: ");
			String inWatchRecord = s.next();

			try {
				rm = new ReportManager(inMovieRecord, inWatchRecord);
			} catch (Exception e) {
				System.out.println("One or both of your files are not valid. Try again.");
			}

			if (rm != null) {
				break;
			}
		}

		System.out.print(printMenu());

		System.out.print("Enter your option: ");
		String str = s.next();

		while (!str.equals("q")) {

			if (str.equals("a")) {
				System.out.println("You have selected to view most frequently streamed movies");
				System.out.print("Enter how many movies you would like to view: ");
				
				try {
					int numMovies = s.nextInt();
					System.out.println(rm.getTopMoviesReport(numMovies));
				} catch (InputMismatchException e) {
					System.out.println("Invalid Number of Movies");
				}

			}

			else if (str.equals("b")) {
				System.out.println("You have selected to view unfinished movies");
				System.out.print("Enter the percent of finished movies you want to see: ");
				
				try {
					int threshold = s.nextInt();
					System.out.println(rm.getMovieCompletionReport(threshold));
				} catch (InputMismatchException e) {
					System.out.println("Invalid Percent Threshold");
				}
			}

			else if (str.equals("c")) {
				System.out.println("You have selected to view watch history of a movie");
				System.out.print("Enter the name of the movie you want to see the history for: ");
				String name = s.next();
				System.out.println(rm.getWatchDates(name));
			}

			System.out.print(printMenu());
			System.out.print("Enter your option: ");
			str = s.next();
		}

		s.close();
		System.out.println("Bye!");
	}

	/**
	 * print out ui menu
	 * 
	 * @return string of ui menu
	 */
	private static String printMenu() {
		String s = "";
		s += "\n";
		s += "Select an option below \n";
		s += "-----------------------\n";
		s += "Menu Options - type a, b, c or q\n";
		s += "a: Most Frequently Streamed Movies\n";
		s += "b: View Unifinished Movies\n";
		s += "c: Watch History for a Movie\n";
		s += "q: quit (you can type this at any point to quit)\n";
		s += "-----------------------\n";

		return s;
	}

}
