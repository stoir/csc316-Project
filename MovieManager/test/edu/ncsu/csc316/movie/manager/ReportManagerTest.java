package edu.ncsu.csc316.movie.manager;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.text.ParseException;

import org.junit.Test;

/**
 * report mgr test
 * 
 * @author sanjanacheerla
 *
 */
public class ReportManagerTest {

	/**
	 * tests report mgr
	 * 
	 * @throws FileNotFoundException if it has trouble making a report mgr
	 * @throws ParseException        if it has trouble making a report mgr
	 */
	@Test
	public void test() throws FileNotFoundException, ParseException {

		var m = new ReportManager("input/movieRecord_sample1.csv", "input/watchRecord_sample1.csv");
		assertNotNull(m.getMovieCompletionReport(50));
		System.out.println(m.getMovieCompletionReport(50));

		assertNotNull(m.getTopMoviesReport(2));
		assertNotNull(m.getWatchDates("The Great Wall"));
		assertNotNull(m.getWatchDates("a"));

		var m1 = new ReportManager("input/movieRecord_sample.csv", "input/watchRecord_sample.csv");
		System.out.println(m1.getMovieCompletionReport(70));
		System.out.println(m1.getTopMoviesReport(100));
		System.out.println(m1.getWatchDates("Pete's Dragon"));
	}

}
