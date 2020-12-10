package edu.ncsu.csc316.movie.manager;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.text.ParseException;

import org.junit.Test;

/**
 * movie mgr test
 * 
 * @author sanjanacheerla
 *
 */
public class MovieManagerTest {

	/**
	 * tests movie mgr
	 * 
	 * @throws FileNotFoundException if it has trouble making a movie mgr
	 * @throws ParseException        if it has trouble making a movie mgr
	 */
	@Test
	public void test() throws FileNotFoundException, ParseException {
		MovieManager m = new MovieManager("input/movieRecord_sample.csv", "input/watchRecord_sample.csv");
		assertNotNull(m);
		m.getMostFrequentlyWatchedMovies(3);
		m.getMoviesByWatchDuration(50);
		m.getWatchHistory("Pete's Dragon");
		assertNull(m.getWatchHistory("a"));
		System.out.println("done");
		System.out.println("start of million: ");
		MovieManager million = new MovieManager("input/_movies.csv", "_records.csv");
	}

}
