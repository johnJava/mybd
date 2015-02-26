package Q23;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class WebAnalyser implements Runnable, WebStatistics {

	public static String keyword;
	public static int MAX_THREAD_NUM = 10;
	public static int MAX_PAGE_COUNT = 200;
	public static int MAX_WORDS_COUNT = 100;
	public static int TIME_OUT = 20000;
	public static LinkedBlockingQueue<String> Q;
	public static Vector<String> visited;//
	public static Hashtable<String, Integer> results;
	public static int TOTAL_WORDS_COUNT = 0;
	public static ExecutorService es;
	public static boolean printFlag = true;
	public static boolean timerFlag = true;
	public static int MAX_THREAD_NUM_COPY = MAX_THREAD_NUM;
	public static Timer t = null;

	/**
	 * init threadpool
	 */
	private void init() {
		es = Executors.newFixedThreadPool(WebAnalyser.MAX_THREAD_NUM);
		int num = 0;
		while (num++ < WebAnalyser.MAX_THREAD_NUM) {
			es.execute(new WebAnalyser());
		}
	}

	/* (non-Javadoc)
	 * @see Q23.WebStatistics#find(java.lang.String, java.lang.String)
	 */
	@Override
	public void find(String word, String URL) throws InterruptedException {
		t = new Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				timerFlag = false;
			}
		}, TIME_OUT);
		Q = new LinkedBlockingQueue<String>();//  ‰»Î«Î«Ûurl
		visited = new Vector<String>();//
		results = new Hashtable<String, Integer>();
		TOTAL_WORDS_COUNT = 0;
		WebAnalyser.keyword = word;
		addUrl(URL);
		if (es == null)
			init();
	}

	/**
	 * handler request url
	 */
	public synchronized void deQueue() {
		while (timerFlag && visited.size() < MAX_PAGE_COUNT && TOTAL_WORDS_COUNT < MAX_WORDS_COUNT) {
			String websiteURL = getQ();
			if (websiteURL == null) {
			} else {
				addVisited(websiteURL);
				String content = Helper.getContentFromURL(websiteURL);
				if (!content.equals("")) {
					int counter = Helper.countNumberOfOccurrences(keyword, content);
					if (counter != 0) {
						TOTAL_WORDS_COUNT += counter;
						System.out.println(Thread.currentThread().getName() + "---The word '" + keyword + "' appears " + counter + " time(s) on " + websiteURL);
						addResult(websiteURL, counter);
						ArrayList<String> urls = Helper.getHyperlinksFromContent(websiteURL, content);
						for (String link : urls) {
							addUrl(link);
						}
					}
				}
			}
		}
	}

	/* 
	 * @see Q23.WebStatistics#printStatistics()
	 */
	@Override
	public void printStatistics() throws KeywordNotProvidedException, IllegalURLException {
		System.out.println("thread complete[" + MAX_THREAD_NUM_COPY + "]");
		if (MAX_THREAD_NUM_COPY > 1) {
			MAX_THREAD_NUM_COPY--;
			return;
		}
		int sum = 0;
		System.out.println("Word: '" + keyword + "'");
		for (Map.Entry<String, Integer> entry : results.entrySet()) {
			sum += entry.getValue();
			System.out.println(entry.getValue() + " time(s) on " + entry.getKey());
		}
		System.out.println("Total: " + results.size() + " pages");
		System.out.println("'" + keyword + "' appears " + sum + " time(s) ");
		es.shutdownNow();
		t.cancel();
	}

	@Override
	public void run() {
		System.out.println("Thread Name--->" + Thread.currentThread().getName());
		deQueue();
		try {
			printStatistics();
		} catch (KeywordNotProvidedException e) {
			e.printStackTrace();
		} catch (IllegalURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * fetch the head of the request quque
	 * @return head requestUrl
	 */
	public synchronized static String getQ() {
		return Q.poll();
	}

	/**
	 * add the requestUrl to the tail of the request quque
	 * @param URL requestUrl
	 */
	public synchronized static void addUrl(String URL) {
		if (Q.contains(URL)) {
		} else {
			System.out.println("add url--->" + URL);
			Q.add(URL);
		}
	}

	/**
	 * get resultset
	 * @return resultset
	 */
	public static Hashtable<String, Integer> getResults() {
		return results;
	}

	/**
	 * @param url the visited url
	 * @param counter 
	 */
	public synchronized static void addResult(String url, int counter) {
		WebAnalyser.results.put(url, counter);
	}

	public synchronized static void addVisited(String url) {
		WebAnalyser.visited.add(url);
	}

}
