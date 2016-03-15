package Stats;

import edu.uci.ics.crawler4j.url.WebURL;
import jdbm.PrimaryHashMap;

import java.io.*;
import java.util.*;

/**
 * Created by Feng Hong on 16/1/19.
 */


public class TextToenizer {

    private List<String> tokenList;
    private Map<String,Integer> stopMap;
    private int numOfSubdomain=0;
    private Set<String> urls= new HashSet<>();
    public int urlCount =0;
    public String longestpage="";
    private int maxpagelength=0;
    private  TreeMap<String, Integer> subMap;


    public TextToenizer(){
        tokenList=new ArrayList<String>();
        stopMap=new HashMap<>();
    }

    private Integer tokenize(String str){
        int length=tokenList.size();
        StringTokenizer tokenizer = new StringTokenizer(str,"://,//.//\n//\t//\r//\f// //'//\"//(//)//{//}//[//]//|//<//>" +
                        "//+//-//=//!//@//#//$//%//^//&//*//_//?//`//~//;//▸//»// //”//«//©//");
        while(tokenizer.hasMoreTokens()){
            String token= tokenizer.nextToken().toLowerCase();
            if(!this.stopMap.containsKey(token) && !isNumeric(token) ) {
                this.tokenList.add(token);
            }
        }
        length=tokenList.size()-length;
        return length;
    }

    public List<String> tokenizeText(String str){
        List<String> tokenList = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(str,"://,//.//\n//\t//\r//\f// //'//\"//(//)//{//}//[//]//|//<//>" +
                "//+//-//=//!//@//#//$//%//^//&//*//_//?//`//~//;//▸//»// //”//«//©//");
        while(tokenizer.hasMoreTokens()){
            String token= tokenizer.nextToken().toLowerCase();
            if(!this.stopMap.containsKey(token) && !isNumeric(token) ) {
                tokenList.add(token);
            }
        }
        return tokenList;
    }



    public void loadDBTable(PrimaryHashMap<String, String> table) throws FileNotFoundException {
        /*   Output console Result for testing
        PrintStream stdout = System.out;
        File file = new File("Result.txt");
        FileOutputStream fis = new FileOutputStream(file);
        PrintStream out = new PrintStream(fis);
        System.setOut(out);
        */

        for(String url: table.keySet()){
            this.urls.add(url);
            String text=table.get(url);
            if(text.length() > maxpagelength){
                maxpagelength = text.length();
                longestpage=url;
            }
            int length=this.tokenize(text);
            urlCount++;
     //       System.out.println(url);

        }
    //    System.setOut(stdout);


    }

    public List<String> getTokenList (){
        return this.tokenList;
    }

    public void loadStopWordList(File input){
        try (Scanner sc = new Scanner(input, "UTF-8")) {
            while (sc.hasNextLine()) {
                String token = sc.nextLine();
                // System.out.println(line);
                    this.stopMap.put(token,1);

                }
            } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // note that Scanner suppresses exceptions
    }



    public int numOfSubdomain(){
        if(numOfSubdomain == 0) {
          this.subMap = getSubMap(urls);
            numOfSubdomain=subMap.size();
        }
        return numOfSubdomain;
    }

    public void printSubdomain(){
        for(String url : subMap.keySet()){
            System.out.println("http://"+ url + ".uci.edu, "+subMap.get(url));
        }
    }


    public static TreeMap<String, Integer> getSubMap(Set<String> urls){
        TreeMap<String, Integer>subMap = new TreeMap<String, Integer> ();
        for(String href: urls){
            WebURL url = new WebURL();
            url.setURL(href);
            String subDomain = url.getSubDomain();
            if(!subMap.containsKey(subDomain)){
                subMap.put(subDomain, 1);
            }
            else{
                subMap.put(subDomain, subMap.get(subDomain)+1);
            }
        }
        return subMap;
    }



    public static void printTokens(List<String> tokens){
        for (String token : tokens) {
            System.out.println(token);
        }
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
