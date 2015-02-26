package test;

import Q23.IllegalURLException;
import Q23.KeywordNotProvidedException;
import Q23.WebAnalyser;

public class TestWebAnalyser {

	public static void main(String[] args) throws InterruptedException, KeywordNotProvidedException, IllegalURLException {
		WebAnalyser webanalyser= new WebAnalyser();
		//webanalyser.find("JAVA", "http://www.infoq.com/");
		webanalyser.find("Flex", "http://help.adobe.com/en_US/flashbuilder/using/index.html");
		webanalyser.printStatistics();
	}

}
