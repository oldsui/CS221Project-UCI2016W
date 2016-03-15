package search;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.*;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pageDAO.Page;


public class SearchServlet extends HttpServlet {
	private static String TFIDF_LOCATION = "C:/Users/oldsui/Desktop/SearchEngine_bak/SearchEngine/map/TFIDFhashmap.ser";
	private static String URL_LOCATION = "C:/Users/oldsui/Desktop/SearchEngine_bak/SearchEngine/map/UrlIdMap.ser";
	private static String PAGERANK_LOCATION = "C:/Users/oldsui/Desktop/SearchEngine_bak/SearchEngine/map/PageRankhashmap.ser";
	private static String TITLE_LOCATION = "C:/Users/oldsui/Desktop/SearchEngine_bak/SearchEngine/map/TitleHashMap.ser";
	
	
	
	private static Map<String, Map<Integer, Double>>tfidfMap = deserializeMap(TFIDF_LOCATION);
	private static Map<String, Integer> urlMap = deserializeMap(URL_LOCATION);
	private static Map<Integer, String> idUrlMap = new HashMap<> ();
	private static Map<Integer, Double> prMap = deserializeMap(PAGERANK_LOCATION);
	private static Map<Integer, String> titleMap = deserializeMap(TITLE_LOCATION);
	
	
	private static Tokenizer myTokenizer = new Tokenizer();
	
	public static Map deserializeMap(String mapLocation){
		try {
			FileInputStream fis = new FileInputStream(mapLocation);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Map map = (HashMap) ois.readObject();
			System.out.println("Map loaded!" + mapLocation);
			ois.close();
			fis.close();
			return map;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.err.print("Failed to read map");
		return null;	// error
	}

			
	public SearchServlet(){
		
		// construct idUrlMap
		for(String url: urlMap.keySet()){
			idUrlMap.put(urlMap.get(url), url);
		}
	
		
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//HttpSession session  = request.getSession();	
		//session.setAttribute("googleQueries", Page.googleQueries);							
		//response.sendRedirect("ndcgTest.jsp");
		
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {		
		String go = request.getParameter("GO");		
		String ndcgTest = request.getParameter("NDCG_TEST");
		
		// normal search mode if ndcgTest == null, which means it's not clicked
		if(go != null){		
			String query = request.getParameter("query");
			System.out.println("Get query: " + query);						// Console testing if query is received from index.jsp 			
			List<Page> pageList = this.getResultList(query, Integer.MAX_VALUE, true);			
			// pass parameters to display
			HttpSession session  = request.getSession();
			session.setAttribute("query", query);	
			session.setAttribute("pageCnt", pageList.size());
			session.setAttribute("pageList", pageList);					
			// redirect to display.jsp
			response.sendRedirect("display.jsp");
		}
		
		else if(ndcgTest != null){
			
			double[][] DG = new double[10][5];			
			double[][] DCG = new double[10][5];
			double[][] NDCG = new double[10][6];		// last element stores sum
			String[][] idealUrlsArr = new String[10][5];

			List<List<Page>> myResultUrls = new ArrayList<> ();
			for(int i = 0; i < 10; i++){
				String query = Page.googleQueries[i];
				List<Page> tmpPageList = this.getResultList(query, 5, false);
				myResultUrls.add(tmpPageList);
				
				// compute ndcg
				List<String> idealUrls = Page.idealUrlList.get(query);
				for(int j = 0; j < 5; j++){
					idealUrlsArr[i][j] = idealUrls.get(j);
					DG[i][j] = 0.0;
					String curUrl = tmpPageList.get(j).getUrl();					
					if(idealUrls.contains(curUrl)){
						DG[i][j] = (5-idealUrls.indexOf(curUrl))/Page.DGW[j];	// get the relevance of current url and divide by logi to obtain DG
					}
				}
				
				// compute DCG
				DCG[i][0] = DG[i][0]; 
				for(int j = 1; j < 5; j++){
					DCG[i][j] = DG[i][j] + DCG[i][j-1];
					
				}
				
				// compute NDCG and store NDCG sum to the last element
				NDCG[i][5] = 0;
				for(int j = 0; j < 5; j++){
					NDCG[i][j] = DCG[i][j]/Page.idealDCG[j];
					NDCG[i][5] += NDCG[i][j];
				}
				

			}
			
			
			
			// pass display parameters
			HttpSession session  = request.getSession();	
			session.setAttribute("googleQueries", Page.googleQueries);	
			session.setAttribute("idealUrlArr", idealUrlsArr);
			session.setAttribute("myResultUrls", myResultUrls);	
			session.setAttribute("NDCG", NDCG);
			
			// compute total NDCG of the 10 queries
			double totalNDCG = 0.0;
			for(int i = 0; i < 10; i++){
				totalNDCG += NDCG[i][5];
			}
			session.setAttribute("totalNDCG", totalNDCG);
			// redirect to ndcgTest.jsp
			response.sendRedirect("ndcgTest.jsp");
		}

	}
	
	
	public List<Page> getResultList(String query, int cntLimit, boolean buildSnippets){
		
		// result map: store urlId, Page. Each page contains the accumulated tfidf, pagerank, title score, and total score
		Map<Integer, Page> resultMap = new HashMap<Integer, Page> ();							
		// tokenize query into terms
		List<String> terms = myTokenizer.tokenizeSingleText(query.toLowerCase());
				
		// retrieval documents for each term in the query, accumulate TFIDF of each document
		for(String term: terms){
			//System.out.println("Tokenized term: " + term);
			// get the urls that contains the term: termTFIDFs =  Map<urlId, TFIDF>
			Map<Integer, Double> termTFIDFs = tfidfMap.get(term);
			if(termTFIDFs == null){								// if the term is not found, skip this term
					continue;
			}
			for(int urlId: termTFIDFs.keySet()){
				if(!resultMap.containsKey(urlId)){		// if this document is not in the result map, create a new entry
					Page tmpPage = new Page(	urlId, 
												idUrlMap.get(urlId), 
												titleMap.get(urlId), 
												termTFIDFs.get(urlId),
												prMap.get(urlId)
											);
					resultMap.put(urlId, tmpPage);
				}
				else{									// if this document is in the result map, update its tfidf
						double old_tfidf = resultMap.get(urlId).getTfidf();
						resultMap.get(urlId).setTfidf(old_tfidf + termTFIDFs.get(urlId));
					}				
				}		
			}
							
			List<Page> pageList = new ArrayList<Page> ();

			if(resultMap.size() != 0){
				
				int cnt = 0;
				for(Page tmpPage: resultMap.values()){
					if(cnt >= cntLimit){
						continue;
					}
					cnt++;
					if(buildSnippets){
						try {			
							tmpPage.constructSnippets(query);		// construct snippets based on only the first word in the query
						} catch (Exception e) {
							e.printStackTrace();
						}				
					}		
					tmpPage.computeTitleScore(query);			// compute title score of each page given the query						
					tmpPage.computeTotalScore();				// compute total score of each page
					pageList.add(tmpPage);						// add current page into the result list
				}
			}
								
			// sort pageList
			Collections.sort(pageList, new Comparator<Page>(){
											public int compare(Page p1, Page p2){
												double score1 = p1.getTotalScore();
												double score2 = p2.getTotalScore();
												return -1*Double.compare(score1, score2);
											}
										}				
				);
				
		return pageList;
	}
	
			
	
	public Map<String,Map<Integer,Double>> readTFIDFJson() throws FileNotFoundException {		// not used any more
		System.out.println("Read json into memory...");
        InputStream is = new FileInputStream(TFIDF_LOCATION);
        Reader reader = new InputStreamReader(is);
        Type fooType = new TypeToken<Map<String,Map<Integer,Double>>>() {}.getType();
        Gson gson=new Gson();
        System.out.println("Start reading Json");

        Map<String,Map<Integer,Double>> newMap=gson.fromJson(reader,fooType);
        System.out.println("Read json Done");
        //printReadedTFIDF(newMap);
        return newMap;

    }

    public void printReadTFIDF(Map<String,Map<Integer,Double>> newMap){

        Iterator it = newMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String word= (String) pair.getKey();
            Map<Integer,Double> TFIDFmap= (Map<Integer, Double>) pair.getValue();
            Iterator it1 = TFIDFmap.entrySet().iterator();
            while (it1.hasNext()) {
                Map.Entry tfidfPair = (Map.Entry)it1.next();
                Integer urlID= (Integer) tfidfPair.getKey();
                double tfidf= (Double) tfidfPair.getValue();
                System.out.println("The word["+word+"] in docID "+urlID+" TFIDF is "+tfidf);
            }
        }
    }
	
	
}
