package Indexing;


import Stats.TextToenizer;

import java.io.*;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Sam on 16/2/21.
 */
public class IndexBuilder {


    //Reference
    public Map<String, Integer> urlIDMap = new HashMap<>();

    //TFIDF Data
    private Map<String,Map<Integer,List<Integer>>> TF= new HashMap<>();
    private Map<String,Integer> DF= new HashMap<>();
    private Map<String,Map<Integer,Double>> TFIDF= new HashMap<>();


    // maps for page rank
    public Map<Integer, Double> pageRankMap=new HashMap<>();;
    private Map<Integer, List<Integer>> inMap=new HashMap<>();;
    private Map<Integer,Integer> outCountMap=new HashMap<>();


    //Title
    private Map<Integer, String> titleMap = new HashMap<>();


    private int numURL=0;
    private int numWord=0;
    private TextToenizer tokenizer;


    public int counter=0;
    public int counter1=0;

    public IndexBuilder(){
        tokenizer = new TextToenizer();
        tokenizer.loadStopWordList(new File("longStopWordList"));
    }

    public void createURLIDMap(String url, Integer urlID){
        this.urlIDMap.put(url,urlID);
    }


    // compute
    public void processData(String url, Integer urlID,String text, String title,List<String> outUrls){
        buildInvertedIndex(urlID,text);
        outCountMap.put(urlID,outUrls.size());
        titleMap.put(urlID,title);
        for(String outUrl: outUrls){
            int outID=-1;
            if(urlIDMap.containsKey(outUrl)){
                outID=urlIDMap.get(outUrl);
                List<Integer> inGoingList=inMap.get(outID);
                if(inGoingList==null){
                    inGoingList=new ArrayList<>();
                }
                inGoingList.add(urlID);
                inMap.put(outID,inGoingList);
                counter1++;
            }
            else{
                counter++;
            }
        }

    }





    public void computePageRank(int n){
        //intialize pageRankMap
        for (String url : urlIDMap.keySet()) {
            int ID=urlIDMap.get(url);
            pageRankMap.put(ID,1.0);
        }

        for(int i=0;i<n;i++) {
            //One interation of PageRank
            for (Integer urlID : pageRankMap.keySet()) {
                double PR = 0;
                List<Integer> inList = inMap.get(urlID);
                if(inList == null){
                    continue;
                }
                for (int prID : inList) {
                    PR += pageRankMap.get(prID) / (double) outCountMap.get(prID);
                }
                PR = PR * 0.85 + 0.15;
                pageRankMap.put(urlID, PR);
            }
        }
    }





    private void buildInvertedIndex(Integer urlID,String text){
        Set<String> currentDocTokens = new HashSet<>();
        List<String> tokens=tokenizer.tokenizeText(text);

       for(int i=0; i< tokens.size(); i++) {

           String token=tokens.get(i);
           Map<Integer,List<Integer>> wordMap;
           //Add to Inverted Index Map
           if(TF.containsKey(token)) {
               wordMap = TF.get(token);
               if(wordMap.containsKey(urlID)){
                   List<Integer> posList=wordMap.get(urlID);
                   posList.add(i);
               }
               else{
                   List<Integer> posList= new ArrayList<>();
                   posList.add(i);
                   wordMap.put(urlID,posList);
               }

               int docCount=DF.get(token);
               docCount++;
               if(!currentDocTokens.contains(token)){
                   DF.put(token,docCount);
                   currentDocTokens.add(token);
               }
           }
           else{
               wordMap= new HashMap<>();
               List<Integer> posList= new ArrayList<>();
               posList.add(i);
               wordMap.put(urlID,posList);
               DF.put(token,1);
           }
           TF.put(token,wordMap);
       }
        numWord=TF.size();
        numURL++;
    }




    public void printDF(){
        Iterator it = DF.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String word= (String) pair.getKey();
            int docCount= (int) pair.getValue();
            System.out.println("The number of document that contain word ["+ word+"] is "+ docCount);
        }
    }


    public int getNumWord(){
        return numWord;
    }

    public int getNumURL(){
        return numURL;
    }

    public void computeTFIDF(){
        Iterator it = TF.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String word= (String) pair.getKey();
            Map<Integer,Integer> wordMap= (Map<Integer, Integer>) pair.getValue();
            Iterator it1 = wordMap.entrySet().iterator();
            Map<Integer,Double> TFIDFmap=new HashMap<>();

            while (it1.hasNext()) {
                Map.Entry countpair = (Map.Entry)it1.next();
                Integer urlID= (Integer) countpair.getKey();
                List<Integer> termList= (List<Integer>) countpair.getValue();
                int tf=termList.size();
                int df=DF.get(word);
                double tfidf=  Math.log10( (double)numWord/(double)df) * Math.log(1+(double)tf);
                TFIDFmap.put(urlID,tfidf);
            }
            TFIDF.put(word,TFIDFmap);
        }
    }

    public void printTFIDF(){
        Iterator it = TFIDF.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String word= (String) pair.getKey();
            Map<Integer,Double> TFIDFmap= (Map<Integer, Double>) pair.getValue();
            Iterator it1 = TFIDFmap.entrySet().iterator();
            while (it1.hasNext()) {
                Map.Entry tfidfPair = (Map.Entry)it1.next();
                Integer urlID= (Integer) tfidfPair.getKey();
                double tfidf= (double) tfidfPair.getValue();
                System.out.println("The word["+word+"] in docID "+urlID+" TFIDF is "+tfidf);
            }
        }
    }

    public void outputTFIDF() throws FileNotFoundException {
        try
        {
            FileOutputStream fos =
                    new FileOutputStream("TFIDFhashmap.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(TFIDF);
            oos.close();
            fos.close();
            System.out.printf("Serialized HashMap data is saved in TFIDFhashmap.ser");
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
        }

    }


    public void outputPageRank() throws FileNotFoundException {
        try
        {
            FileOutputStream fos =
                    new FileOutputStream("PageRankhashmap.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(pageRankMap);
            oos.close();
            fos.close();
            System.out.printf("Serialized HashMap data is saved in PageRankhashmap.ser");
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
        }

    }

    public void outputTitle() throws FileNotFoundException {
        try
        {
            FileOutputStream fos =
                    new FileOutputStream("TitleHashMap.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(titleMap);
            oos.close();
            fos.close();
            System.out.printf("Serialized HashMap data is saved in TitleHashMap.ser");
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
        }

    }


    public void outputUrlIDMap() throws FileNotFoundException {
        try
        {
            FileOutputStream fos =
                    new FileOutputStream("UrlIdMap.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(urlIDMap);
            oos.close();
            fos.close();
            System.out.printf("Serialized HashMap data is saved in UrlIdMap.ser");
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
        }

    }





    public void TFIDFtoJson() throws FileNotFoundException {
        PrintStream stdout = System.out;

        Type fooType = new TypeToken<Map<String,Map<Integer,Double>>>() {}.getType();
        Gson gson=new Gson();
        PrintStream out = new PrintStream(new FileOutputStream("TFIDFTest.txt"));
        System.setOut(out);
        System.out.println(gson.toJson(TFIDF, fooType));

        System.setOut(stdout);

    }

    public void TFMaptoJson() throws FileNotFoundException {
        PrintStream stdout = System.out;

        Type fooType = new TypeToken<Map<String,Map<Integer,List<Integer>>>>() {}.getType();
        Gson gson=new Gson();
        PrintStream out = new PrintStream(new FileOutputStream("TFMapTest.txt"));
        System.setOut(out);
        System.out.println(gson.toJson(TF, fooType));
        System.setOut(stdout);


    }

    public void IDListtoJson() throws FileNotFoundException {
        PrintStream stdout = System.out;
        Type fooType = new TypeToken<List<String>>() {}.getType();
        Gson gson=new Gson();
        PrintStream out = new PrintStream(new FileOutputStream("IDListTest.txt"));
        System.setOut(out);

      //  System.out.println(gson.toJson(urlIDList, fooType));
        System.setOut(stdout);


    }

    public void readTFIDFJson() throws FileNotFoundException {
        InputStream is = new FileInputStream("TFIDFTest.txt");
        Reader reader = new InputStreamReader(is);
        Type fooType = new TypeToken<Map<String,Map<Integer,Double>>>() {}.getType();
        Gson gson=new Gson();
        Map<String,Map<Integer,Double>> newMap=gson.fromJson(reader,fooType);
        System.out.println("Read json Done");
        printReadedTFIDF(newMap);
    }

/*
    public void outputUrlIDList(){
        try
        {
            FileOutputStream fos =
                    new FileOutputStream("URlList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(urlIDList);
            oos.close();
            fos.close();
            System.out.printf("ID-List is saved in hashmap.ser");
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

*/
    public void printReadedTFIDF(Map<String,Map<Integer,Double>> newMap){
        Iterator it = newMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String word= (String) pair.getKey();
            Map<Integer,Double> TFIDFmap= (Map<Integer, Double>) pair.getValue();
            Iterator it1 = TFIDFmap.entrySet().iterator();
            while (it1.hasNext()) {
                Map.Entry tfidfPair = (Map.Entry)it1.next();
                Integer urlID= (Integer) tfidfPair.getKey();
                double tfidf= (double) tfidfPair.getValue();
                System.out.println("The word["+word+"] in docID "+urlID+" TFIDF is "+tfidf);
            }
        }
    }

}




