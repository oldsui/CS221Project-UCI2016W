package startCrawler;

public class Params {

	// database parameters:
	public static String DBPath = "./data/DB/myDB";
	public static String DBPath2 = "./data/DB2/myDB";


	// config parameters:
	public static int polite = 300;							// politenessDelay: ms
	public static int maxDepth = -1; 						// -1: no limit
	public static int maxPage = -1;							// -1: no limit
	public static String tmp_data_folder = "./data/temp";
	public static String userAgentName = "testing";
	public static boolean resume = true;
    
        
    // controller parameters:
	public static int numberOfCrawlers = 10;
        		
	// other parameters:
	public static String crawlDir = "./data/crawled/";
		
}
