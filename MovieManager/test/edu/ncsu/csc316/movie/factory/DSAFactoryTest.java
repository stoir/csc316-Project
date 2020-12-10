package edu.ncsu.csc316.movie.factory;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * dsa factory test
 * 
 * @author sanjanacheerla
 *
 */
public class DSAFactoryTest {

	/**
	 * test
	 */
	@SuppressWarnings("static-access")
	@Test
	public void test() {
		DSAFactory f = new DSAFactory();
		assertNotNull(f);

		assertNotNull(f.getComparisonSorter());
		assertNotNull(f.getIndexedList());
		assertNotNull(f.getMap());
		assertNotNull(f.getNonComparisonSorter());

	}

}
