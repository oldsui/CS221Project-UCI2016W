package startCrawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {

    public void startCawler() throws Exception {
    	    	    	                
        /*
         *  CrawlConfig settings
         */
        CrawlConfig config = new CrawlConfig();
        config.setPolitenessDelay(Params.polite);								
        config.setMaxDepthOfCrawling(Params.maxDepth);
        config.setMaxPagesToFetch(Params.maxPage);
        config.setCrawlStorageFolder(Params.tmp_data_folder);
        config.setUserAgentString(Params.userAgentName); 
        config.setResumableCrawling(Params.resume);

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        robotstxtConfig.setUserAgentName(Params.userAgentName);
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        
        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        controller.addSeed("http://www.ics.uci.edu");
        //controller.addSeed("http://www.ics.uci.edu/~lopes/");
        //controller.addSeed("http://www.ics.uci.edu/~welling/");


        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(MyCrawler.class, Params.numberOfCrawlers);
               
        
        if(controller.isFinished()){
        	System.out.println("Crawling Complete !");
        }
    }
}