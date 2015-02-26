package Q23;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnalyserThreadPool {
	/**
	 * find (keyword,URL) method should
	 * 
	 * (1) mark a URL as 'visited' by adding it to visited list (2) if the
	 * content of a given URL contains selected keyword then the you should (a)
	 * add this URL to result set and (b) record how many times the keyword
	 * occurs on this page. If the keyword does not occur then skip (a) and (b)
	 * 
	 * This method should skip URLs that have been visited by other threads
	 * 
	 * @param keyword
	 *            a string for case-insensitive keyword search
	 * @param URL
	 *            starting from this seed page
	 * @throws InterruptedException
	 */
	public void find(String word, String URL) throws InterruptedException {
		
	}
}
