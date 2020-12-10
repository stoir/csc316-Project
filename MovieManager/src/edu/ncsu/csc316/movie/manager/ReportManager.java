package edu.ncsu.csc316.movie.manager;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;

import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.movie.data.Movie;
import edu.ncsu.csc316.movie.data.WatchRecord;

/**
 * report manager
 * 
 * @author sanjanacheerla
 *
 */
public class ReportManager {

	/**
	 * manager
	 */
	private MovieManager manager;
	/**
	 * indent
	 */
	private static final String INDENT = "   ";

	/**
	 * Creates a new ReportManager for generating reports for the MovieManager
	 * software
	 * 
	 * @param pathToMovieFile the path to the file that contains movie records
	 * @param pathToWatchFile the path to the file that contains watch records
	 * @throws FileNotFoundException if either input file cannot be
	 *                               found/read/opened
	 * @throws ParseException        if the watch record file contains incorrectly
	 *                               formatted date information
	 */
	public ReportManager(String pathToMovieFile, String pathToWatchFile) throws FileNotFoundException, ParseException {
		manager = new MovieManager(pathToMovieFile, pathToWatchFile);
	}

	/**
	 * Returns a report of the most frequently watched movies that contains the top
	 * n movies most watched
	 * 
	 * @param numberOfMovies the number of movies to include in the report
	 * @return a report of the most frequently watched movies
	 */
	public String getTopMoviesReport(int numberOfMovies) {
		if (numberOfMovies <= 0) {
			return "Please enter a number > 0.";
		}

		List<Movie> mList = manager.getMostFrequentlyWatchedMovies(numberOfMovies);

		if (mList.size() == 0) {
			return "No movies have been streamed.";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("The ");
		sb.append(numberOfMovies);
		sb.append(" most frequently watched movies [\n");

		for (Movie m : mList) {
			StringBuilder s = new StringBuilder();
			s.append(INDENT);
			s.append(m.getTitle());
			s.append(" (");
			s.append(m.getYear());
			s.append(")\n");
			sb.append(s);
		}
		sb.append("]\n");

		return sb.toString();
	}

	/**
	 * Returns a report of movies below a specific watch percentage threshold.
	 * 
	 * @param threshold the percentage threshold (as a whole number)
	 * @return a report of movies below a specific watch percentage threshold
	 */
	public String getMovieCompletionReport(int threshold) {
		if (threshold <= 0 || threshold > 100) {
			return "Please enter a percentage completion between 1 and 100.";
		}

		List<Movie> mList = manager.getMoviesByWatchDuration(threshold);
		if (mList.size() == 0) {
			return "No movies are less than " + threshold + "% completed.";
		}

		StringBuilder sb = new StringBuilder();
		sb.append("The movies that have been watched less than ");
		sb.append(threshold);
		sb.append("% [\n");

		for (Movie m : mList) {
			StringBuilder s = new StringBuilder();
			s.append(INDENT);
			s.append(m.getTitle());
			s.append(" (");
			s.append(m.getYear());
			s.append(")\n");
			sb.append(s);
		}
		sb.append("]\n");

		// System.out.println(s);
		return sb.toString();
	}

	/**
	 * Return a report of dates on which a specific movie was watched
	 * 
	 * @param title the title of the movie for which to retrieve watch dates
	 * @return a report of dates on which a specific movie was watched
	 */
	public String getWatchDates(String title) {
		var rList = manager.getWatchHistory(title);

		if (rList == null || rList.size() == 0) {
			return "No watch history for \"" + title + "\".";
		}

		StringBuilder sb = new StringBuilder();
		sb.append("The movie \"");
		sb.append(title);
		sb.append("\" was streamed on [\n");

		for (WatchRecord r : rList) {
			sb.append(INDENT);
			sb.append(r.getDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
			sb.append("\n");
		}
		sb.append("]\n");

		return sb.toString();
	}
}
