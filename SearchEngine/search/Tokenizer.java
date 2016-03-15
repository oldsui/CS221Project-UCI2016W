package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

public class Tokenizer {
    private static Set<String> stopMap = loadStopWordList(new File("C:/Users/oldsui/Desktop/SearchEngine_bak/SearchEngine/longStopWordList"));
    
    
    public Tokenizer(){
    }
    
    
    public List<String> tokenizeSingleText(String str){
    	List<String> tokenList = new ArrayList<String> ();
        StringTokenizer tokenizer = new StringTokenizer(str,"://,//.//\n//\t//\r//\f// //'//\"//(//)//{//}//[//]//|//<//>" +
                "//+//-//=//!//@//#//$//%//^//&//*//_//?//`//~//;//▸//»// //”//«//©//");
        while(tokenizer.hasMoreTokens()){
            String token= tokenizer.nextToken().toLowerCase();
            if(!this.stopMap.contains(token) && !isNumeric(token) ) {
                tokenList.add(token);
            }
        }
        return tokenList;
    }
    
    public List<String> tokenizeSingleTextNoStopWords(String str){
    	List<String> tokenList = new ArrayList<String> ();
        StringTokenizer tokenizer = new StringTokenizer(str,"://,//.//\n//\t//\r//\f// //'//\"//(//)//{//}//[//]//|//<//>" +
                "//+//-//=//!//@//#//$//%//^//&//*//_//?//`//~//;//▸//»// //”//«//©//");
        while(tokenizer.hasMoreTokens()){
            String token= tokenizer.nextToken().toLowerCase();
            tokenList.add(token);
        }
        return tokenList;
    }
    
       
    
    public static HashSet<String> loadStopWordList(File input){
    	HashSet<String> res = new HashSet<String> ();
        try (Scanner sc = new Scanner(input, "UTF-8")) {
            while (sc.hasNextLine()) {
                String token = sc.nextLine();
                // System.out.println(line);
                    res.add(token);
                }
            return res;
            } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
    
    
}
