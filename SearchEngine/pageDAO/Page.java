package pageDAO;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import search.Tokenizer;



/**
 * Page entity. @author MyEclipse Persistence Tools
 */

public class Page  implements java.io.Serializable {
	
	public static double[] DGW = 		{1, 		1, 		1.59, 		2, 		2.32};
	public static double[] idealDG = 	{5/1.0, 	4/1.0, 	3/1.59, 	2/2, 	1/2.32};
	public static double[] idealDCG = 	{5, 	  	9, 	 	10.89, 		11.89, 	12.32};

	
	public static String[] googleQueries = {
												"mondego",
												"machine learning",
												"software engineering",
												"security",
												"student affairs",
												"graduate courses",
												"crista lopes",
												"rest",
												"computer games",
												"information retrieval",		
											};
	public static HashMap<String, List<String>> idealUrlList = loadIdealUrlMap();				// query -> List<url>, descending order: 5,4,3,2,1
	
	// preload the ideal urls of the 10 queries (returned from google), relevance: 5,4,3,2,1
	public static HashMap<String, List<String>> loadIdealUrlMap(){
		System.out.println("Loading google's urls");
		HashMap<String, List<String>> res = new HashMap<> ();
		for (int i = 0; i < 10; i++){
			
            JSONParser parser = new JSONParser();
             try {
                 Object obj = parser.parse(new FileReader(
                     "C:/Users/oldsui/Desktop/SearchEngine_bak/SearchEngine/idealUrls/"+i+".txt"));
                 JSONObject jsonObject = (JSONObject) obj;
                 JSONObject obj1 = (JSONObject) jsonObject.get("responseData");
                 JSONArray arr = (JSONArray) obj1.get("results");
                 System.out.println("Loading query: " + googleQueries[i]+" ...");
                 res.put(googleQueries[i], new ArrayList<String> ());
                 for(int j=0;j<5;j++) {
                     JSONObject obj2 = (JSONObject) arr.get(j);
                     String url = (String) obj2.get("url");     
                     res.get(googleQueries[i]).add(url);
                     System.out.println(url+", relevance: "+(5-j));
                 }
                 System.out.println();
             } catch (Exception e) {
            	 e.printStackTrace();
             }
		}
		return res;
	}


    // Fields    
     private Integer urlId;
     private String url;
     private String title;
     private double tfidf;
     private double pr;
     private double titleScore;
     private double totalScore;
     
     // snipppets
     private List<String> snippets;

    // Constructors
     /** default constructor */
     public Page() {
    	 
     }

     
     /** full constructor */
     public Page(int urlId, String url, String title, double tfidf, double pr) {
         this.urlId = urlId;
         this.url = url;
         this.title = title;
         this.tfidf = tfidf;
         this.pr = Math.min(pr, 10);
         this.titleScore = 0.0;
         this.getTotalScore();
         snippets = new ArrayList<String> ();
     }


    public double computeTotalScore(){
    	this.totalScore = this.tfidf; 								// before improvement
    	//this.totalScore = this.tfidf + 20*Math.pow(10*this.pr, 0.1+10*this.titleScore); 	// after improvement

    	this.totalScore = this.tfidf + Math.min(20, 10*this.pr)*(0.1+10*this.titleScore);//+10*this.titleScore;
    	return this.totalScore;
    }
    public double getTotalScore() { 	
    		
		return totalScore;
	}


	public void setTotalScore(double totalScore) {
		this.totalScore = totalScore;
	}



	public Integer getUrlId() {
		return urlId;
	}


	public void setUrlId(Integer urlId) {
		this.urlId = urlId;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public double getTfidf() {
		return tfidf;
	}


	public void setTfidf(double tfidf) {
		this.tfidf = tfidf;
	}


	public double getPr() {
		return pr;
	}


	public void setPr(double pr) {
		this.pr = pr;
	}


	public double getTitleScore() {
		return this.titleScore;
	}


	public void setTitleScore(double titleScore) {
		this.titleScore = titleScore;
	}

	public double computeTitleScore(String query){
		if(query == null || query.equals("")){
			System.out.println("Query empty !");
			this.titleScore = 0.0;
			return 0.0;
		}
		if(this.title == null || this.title.equals("")){
			System.out.println("Title empty !");
			this.titleScore = 0.0;
			return 0.0;
		}
		Tokenizer myTokenizer = new Tokenizer();
		
		List<String> titleTerms = myTokenizer.tokenizeSingleText(this.title);
		List<String> queryTerms = myTokenizer.tokenizeSingleText(query);
		if(queryTerms.size() == 0 || titleTerms.size() == 0){
			this.titleScore = 0.0;
		}
		else{
			int intersect = 0;
			Set<String> collection = new HashSet<> (titleTerms);			// collection contains title terms only
			for(String cur: queryTerms){									// find the intersection
				if(collection.contains(cur)){
					intersect++;
				}				
			}
			collection.addAll(queryTerms);									// union both sets
			this.titleScore = (double)intersect/(double)collection.size();
			//System.out.println("Title score of "+this.title+"is: "+this.titleScore);
		}
		
		
		return this.titleScore;
	}

	

	public void constructSnippets(String query) throws Exception{
		String path = "C:/Users/oldsui/Desktop/SearchEngine_bak/SearchEngine/page/";
		
		JsonFactory jsonFac = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper(jsonFac);
		File cur = new File(path+this.urlId+".txt");
		System.out.println("Openning :"+path+this.urlId+".txt");
		TypeReference<HashMap<String, Object>> mapTypeRef = new TypeReference<HashMap<String, Object>>(){};
		

		HashMap<String, Object> pageMap = mapper.readValue(cur, mapTypeRef);
		String text = (String) pageMap.get("text");
		
		Tokenizer myTokenizer = new Tokenizer();
		List<String> textTokens = myTokenizer.tokenizeSingleTextNoStopWords(text);
		List<String> queryTokens = myTokenizer.tokenizeSingleTextNoStopWords(query);
		int cnt = 0; 
		if(queryTokens.size() == 1){									// querying only one word
			for(int i = 0; i < textTokens.size() && cnt < 5; i++){
				if(textTokens.get(i).equals(query)){
					cnt++;
					String tmpStr = "";
					List<String> tmpList = textTokens.subList(Math.max(0, i-3), Math.min(i+4, textTokens.size()));
					for(String str: tmpList){
						tmpStr += str+" ";
					}
					this.snippets.add(tmpStr);	// a snippet contains only previous 5 and later 5 words
				}
			}
		}
		else{														   // querying more than one word
			for(int i = 0; i < textTokens.size()-1 && cnt < 5; i++){
				if(textTokens.get(i).equals(queryTokens.get(0)) && textTokens.get(i+1).equals(queryTokens.get(1))){
					cnt++;
					String tmpStr = "";
					List<String> tmpList = textTokens.subList(Math.max(0, i-3), Math.min(i+4, textTokens.size()));
					for(String str: tmpList){
						tmpStr += str+" ";
					}
					this.snippets.add("..."+tmpStr+"");	// a snippet contains only previous 5 and later 5 words
				}
			}
		}
		
				
	}


	public List<String> getSnippets() {
		return snippets;
	}


	public void setSnippets(List<String> snippets) {
		this.snippets = snippets;
	}
		

}