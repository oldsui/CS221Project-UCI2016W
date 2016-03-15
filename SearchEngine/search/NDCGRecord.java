package search;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class NDCGRecord {
	public static  HashMap<String, HashMap<String, Integer>> idealUrls = new HashMap<> ();
	
	public double[] DCG = new double[5];
	public double[] NDCG = new double[5];
	
	public NDCGRecord(){
		
	}

}
