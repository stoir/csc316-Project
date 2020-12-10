package edu.ncsu.csc316.movie.manager;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import edu.ncsu.csc316.dsa.list.ArrayBasedList;
import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.SkipListMap;
import edu.ncsu.csc316.dsa.sorter.MergeSorter;
import edu.ncsu.csc316.movie.data.Movie;
import edu.ncsu.csc316.movie.data.WatchRecord;
//import edu.ncsu.csc316.movie.factory.DSAFactory;
import edu.ncsu.csc316.movie.io.InputFileReader;

/**
 * movie manager
 * 
 * @author sanjanacheerla
 *
 */
public class MovieManager {

	/**
	 * movie arr
	 */
	private Movie[] mArr;

	/**
	 * map of movie name to movies
	 */
	private SkipListMap<String, Movie> movieNameToMovie;

	/**
	 * map of movie ids to movies
	 */
	private SkipListMap<String, Movie> idToMovie;

	/**
	 * map of movie id to list of records
	 */
	private SkipListMap<String, ArrayBasedList<WatchRecord>> movieIdToRecords;

	/**
	 * map of movie id to max watched
	 */
	private SkipListMap<String, Integer> movieIdToThreshold;

	/**
	 * map of movie id to size of records
	 */
	private SkipListMap<String, Integer> movieIdToRecordSize;

	/**
	 * movielist
	 */
	private List<Movie> movies;

	/**
	 * movie watch records
	 */
	private List<WatchRecord> records;

	/**
	 * Creates a MovieManager instance for handling utility functions
	 * 
	 * @param pathToMovies  the path to the file of movie records
	 * @param pathToHistory the path to the file of watch records
	 * @throws FileNotFoundException if the file cannot be found
	 * @throws ParseException        if the watch history file has incorrectly
	 *                               formatted date information
	 */
	public MovieManager(String pathToMovies, String pathToHistory) throws FileNotFoundException, ParseException {
		long readingTime = System.nanoTime();
		movies = InputFileReader.readMovieFile(pathToMovies);
		records = InputFileReader.readHistoryFile(pathToHistory);
		long endTime = System.nanoTime();
		long durationInNano = (endTime - readingTime);
		//@SuppressWarnings("unused")
		long duration = TimeUnit.NANOSECONDS.toSeconds(durationInNano);
		System.out.print("duration reading = " + duration + "\n");
		
		// System.out.println("done reading");
		long startTime = System.nanoTime();

		movieIdToRecords = new SkipListMap<String, ArrayBasedList<WatchRecord>>();
		movieNameToMovie = new SkipListMap<String, Movie>();
		idToMovie = new SkipListMap<String, Movie>();
		movieIdToThreshold = new SkipListMap<String, Integer>();
		movieIdToRecordSize = new SkipListMap<String, Integer>();

		long firstForLoop = System.nanoTime();
		for (Movie movie : movies) {
			movieNameToMovie.put(movie.getTitle(), movie); // timeout
			idToMovie.put(movie.getId(), movie);

			// method 2 map of movies to size of records
			movieIdToRecordSize.put(movie.getId(), 0);

			// method 3 use id to movie to return
			movieIdToThreshold.put(movie.getId(), 0);

			// method 1
			ArrayBasedList<WatchRecord> recordsForMovie = new ArrayBasedList<WatchRecord>();
			movieIdToRecords.put(movie.getId(), recordsForMovie);
		}
		endTime = System.nanoTime();
		durationInNano = (endTime - firstForLoop);
		//@SuppressWarnings("unused")
		duration = TimeUnit.NANOSECONDS.toSeconds(durationInNano);
		System.out.print("duration first loop = " + duration + "\n");

		
		long secondForLoop = System.nanoTime();
		for (WatchRecord record : records) {
			var r = movieIdToRecords.get(record.getMovieId());
			if (r != null) {
				r.addLast(record);
			}
		}
		endTime = System.nanoTime();
		durationInNano = (endTime - secondForLoop);
		//@SuppressWarnings("unused")
		duration = TimeUnit.NANOSECONDS.toSeconds(durationInNano);
		System.out.print("duration second for loop = " + duration + "\n");

		
		long thirdForLoop = System.nanoTime();
		for (var x : movieIdToRecords.entrySet()) {
			var sz = x.getValue().size();
			if (sz != 0) {
				movieIdToRecordSize.put(x.getKey(), sz);
			} else {
				continue;
			}

			var arr = x.getValue();
			var runTime = idToMovie.get(x.getKey()).getRuntime();

			int tmp = 0, max = 0;
			int watchedTime = 0;

			if (sz > 0) {
				tmp = arr.get(0).getWatchTime();

				watchedTime = ((100 * tmp) / runTime);

				max = watchedTime;
			}

			for (int i = 0; i < sz; i++) {
				tmp = arr.get(i).getWatchTime();
				watchedTime = ((100 * tmp) / runTime);
				if (watchedTime > max) {
					max = watchedTime;
				}
			}

			movieIdToThreshold.put(x.getKey(), max);

		}
		endTime = System.nanoTime();
		durationInNano = (endTime - thirdForLoop);
		//@SuppressWarnings("unused")
		duration = TimeUnit.NANOSECONDS.toSeconds(durationInNano);
		System.out.print("duration third for loop = " + duration + "\n");

		mArr = new Movie[movies.size()];
		for (int i = 0; i < movies.size(); i++) {
			mArr[i] = movies.get(i);
		}

		var comp = new MoviesComparatorFrequency();
		MergeSorter<Movie> sort = new MergeSorter<Movie>(comp);
		sort.sort(mArr);

		endTime = System.nanoTime();
		durationInNano = (endTime - startTime);
		//@SuppressWarnings("unused")
		duration = TimeUnit.NANOSECONDS.toSeconds(durationInNano);
		System.out.print("duration = " + duration + "\n");

	}

	/**
	 * Returns a list of watch records associated with the requested movie title
	 * 
	 * @param title the title of the movie for which to retrieve watch record
	 *              information
	 * @return a list of watch records associated with the requested movie title
	 */
	public List<WatchRecord> getWatchHistory(String title) {
		Movie m = movieNameToMovie.get(title);
		if (m == null) {
			return null;
		}

		List<WatchRecord> recordsList = movieIdToRecords.get(m.getId());

		WatchRecord[] recordsArr = new WatchRecord[recordsList.size()];
		for (int i = 0; i < recordsArr.length; i++) {
			recordsArr[i] = recordsList.get(i);
		}

		var comp = new WatchRecordsComparator();
		MergeSorter<WatchRecord> sort = new MergeSorter<WatchRecord>(comp);
		sort.sort(recordsArr);

		List<WatchRecord> out = new ArrayBasedList<WatchRecord>(recordsList.size());
		for (int i = 0; i < recordsArr.length; i++) {
			out.addLast(recordsArr[i]);
			recordsList.removeLast();
		}

		return out;
	}

	/**
	 * Return a list of movie records that contains the top n most frequently
	 * watched movies
	 * 
	 * @param numberOfMovies the n most frequently watched movies to include in the
	 *                       list
	 * @return a list of movie records that contains the top n most frequently
	 *         watched movies
	 */
	public List<Movie> getMostFrequentlyWatchedMovies(int numberOfMovies) {
		List<Movie> out = new ArrayBasedList<Movie>();

		if (mArr.length <= 0) {
			return out;
		}

		var tmpSize = mArr.length - 1;

		while (numberOfMovies != 0 && mArr.length > 0 && tmpSize >= 0) {
			if (movieIdToRecordSize.get(mArr[tmpSize].getId()) > 0) {
				out.addLast(mArr[tmpSize]);
				tmpSize -= 1;
				numberOfMovies -= 1;

				if (tmpSize < 0) {
					break;
				}
			} else {
				break;
			}

		}
		return out;
	}

	/**
	 * Return a list of movie records that have been watched less than a specific
	 * threshold percentage
	 * 
	 * @param threshold the percentages threshold to use, as a whole number
	 * @return a list of movie records that have been watched less than the
	 *         specified threshold percentage
	 */
	public List<Movie> getMoviesByWatchDuration(int threshold) {
		List<Movie> recordsList = new ArrayBasedList<Movie>();

		if (threshold <= 0 || threshold > 100) {
			return recordsList;
		}

		for (var x : movieIdToThreshold.entrySet()) {
			if (x.getValue() < threshold && x.getValue() >= 1) {
				recordsList.addLast(idToMovie.get(x.getKey()));
			}
		}

		Movie[] moviesArr = new Movie[recordsList.size()];
		for (int i = 0; i < moviesArr.length; i++) {
			moviesArr[i] = recordsList.get(i);
		}

		var comp = new MoviesComparatorPercentage();
		MergeSorter<Movie> sort = new MergeSorter<Movie>(comp);
		sort.sort(moviesArr);

		List<Movie> out = new ArrayBasedList<Movie>(recordsList.size());
		for (int i = 0; i < moviesArr.length; i++) {
			out.addLast(moviesArr[i]);
			recordsList.removeLast();
		}
		return out;
	}

	/**
	 * compare two movie frequencies
	 * 
	 * @author sanjanacheerla
	 *
	 */
	private class MoviesComparatorFrequency implements Comparator<Movie> {

		/**
		 * compare 2 movie records
		 * 
		 * @param m1 to compare
		 * @param m2 to compare
		 */
		@Override
		public int compare(Movie m1, Movie m2) {
			int m1sz = movieIdToRecordSize.get(m1.getId());
			int m2sz = movieIdToRecordSize.get(m2.getId());

			int sz = Integer.compare(m1sz, m2sz);

			int title = m2.getTitle().compareTo(m1.getTitle());

			int id = m2.getId().compareTo(m1.getId());

			if (sz != 0) {
				return sz;
			} else if (title != 0) {
				return title;
			} else if (id != 0) {
				return id;
			} else {
				return 0;
			}
		}
	}

	/**
	 * compare movie watch percentages
	 * 
	 * @author sanjanacheerla
	 *
	 */
	private class MoviesComparatorPercentage implements Comparator<Movie> {

		/**
		 * compare 2 movie records
		 * 
		 * @param m1 to compare
		 * @param m2 to compare
		 */
		@Override
		public int compare(Movie m1, Movie m2) {
			int m1p = movieIdToThreshold.get(m1.getId());
			int m2p = movieIdToThreshold.get(m2.getId());

			int p = Integer.compare(m2p, m1p);

			int title = m1.getTitle().compareTo(m2.getTitle());

			int id = m2.getId().compareTo(m1.getId());

			if (p != 0) {
				return p;
			} else if (title != 0) {
				return title;
			} else if (id != 0) {
				return id;
			} else {
				return 0;
			}
		}

	}

	/**
	 * watch records comparators
	 * 
	 * @author sanjanacheerla
	 *
	 */
	private class WatchRecordsComparator implements Comparator<WatchRecord> {

		/**
		 * compare 2 watch records
		 * 
		 * @param w1 to compare
		 * @param w2 to compare
		 */
		@Override
		public int compare(WatchRecord w1, WatchRecord w2) {
			var d1 = w1.getDate();
			var d2 = w2.getDate();
			int d = d2.compareTo(d1);
			var w1t = w1.getWatchTime();
			var w2t = w2.getWatchTime();
			int watchTime = Integer.compare(w2t, w1t);

			if (d != 0) {
				return d;
			} else if (watchTime != 0) { return watchTime; }
			return 0;
		}
	}
}
