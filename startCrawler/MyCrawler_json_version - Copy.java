package startCrawler;


import java.io.IOException;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;


public class MyCrawler extends WebCrawler {
		
	private static JsonFactory jsonFactory;
    private static String path = "/Users/Sam/Desktop/IR_P2/txtData3";
    private static int docid=0;
	private static final Logger logger = LoggerFactory.getLogger(MyCrawler.class);
	private final static Pattern FILTERS1 = Pattern.compile("^(https?)://(.*.)?ics.uci.edu/.*");
    private final static Pattern FILTERS2 = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|rar|gz|ico|pfm|c|h|o))$");
    private final static Pattern FILTERS_TRAP = 
    		Pattern.compile(".*calendar.ics.uci.edu.*"
    						//+"|.*archive.ics.uci.edu.*"
    						+"|.*drzaius.ics.uci.edu.*"
    						+"|.*fano.ics.uci.edu.*"
    						+"|.*djp3-pc2.ics.uci.edu.*"
    						+"|.*wics.ics.uci.edu.*"
    						);
    
    
    
    public MyCrawler(){
    	jsonFactory = new JsonFactory();
    }

    /**
     * This method receives two parameters. The first parameter is the page
     * in which we have discovered this new url and the second parameter is
     * the new url. You should implement this function to specify whether
     * the given url should be crawled or not (based on your crawling logic).
     * In this example, we are instructing the crawler to ignore urls that
     * have css, js, git, ...   
     */
     @Override
     public boolean shouldVisit(Page referringPage, WebURL url) {
         String href = url.getURL().toLowerCase();
         if(	!FILTERS1.matcher(url.getURL()).matches() 				// if not in the format of ...ics.uci.edu/...
        		|| FILTERS2.matcher(href).matches()						// if matches FILTER2 pattern
        		|| FILTERS_TRAP.matcher(href).matches()){					// if it's a trap	
        	 
        	return false;
        }         
                         
         return true;
     }

     /**
      * This function is called when a page is fetched and ready
      * to be processed by your program.
      */
    @Override
    public void visit(Page page) {
    	if (page.getParseData() instanceof HtmlParseData) {
    		HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();

            if (text.length() <= 2097152) {
            	File cur = new File(path+"\\"+docid+".txt");
                jsonObj.put("docid", docid);
                jsonObj.put("url", page.getWebURL().getURL());
                jsonObj.put("domain",  page.getWebURL().getDomain());
                jsonObj.put("subDomain",  page.getWebURL().getSubDomain());

                jsonObj.put("text", htmlParseData.getText());
                jsonObj.put("title", htmlParseData.getTitle());
                jsonObj.put("anchor",  page.getWebURL().getAnchor());
                jsonObj.put("outgoingUrls", htmlParseData.getOutgoingUrls().toString());
                

                docid++;
 				logger.info("Saved: {}", url);

 			}



        }
     }
     

         
     
}