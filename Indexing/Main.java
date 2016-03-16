import Indexing.IndexBuilder;
import Stats.NDCGProcessor;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sam on 16/2/3.
 */
public class Main {


    public static void main(String[] args) throws Exception {

        // NDCGProcessor ndcgProcessor= new NDCGProcessor();


        String path = "/Users/Sam/Desktop/IR_P2/txtData3";
        final File folder = new File(path);

        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(jsonFactory);

        IndexBuilder indexer = new IndexBuilder();



        for (final File file : folder.listFiles()) {
            if(file.getName() .equals( ".DS_Store")){
                continue;
            }
            TypeReference<HashMap<String,Object>> hashMapTypeRef = new TypeReference<HashMap<String,Object>>() {};
            HashMap<String,Object> pageMap = mapper.readValue(file, hashMapTypeRef);
            String url = (String) pageMap.get("url");
            int docid = (Integer) pageMap.get("docid");
            indexer.createURLIDMap(url,docid);
        }


        System.out.println(indexer.urlIDMap.size());


        //  System.out.println(gson.toJson(urlIDList, fooType));
        for (final File file : folder.listFiles())
        {
            if(file.getName() .equals( ".DS_Store")){
                continue;
            }
            TypeReference<HashMap<String,Object>> hashMapTypeRef = new TypeReference<HashMap<String,Object>>() {};
            HashMap<String,Object> pageMap = mapper.readValue(file, hashMapTypeRef);

            String text = (String) pageMap.get("text");
            String url = (String) pageMap.get("url");
            int docid = (Integer) pageMap.get("docid");
            String title = (String) pageMap.get("title");
            String outStrings = (String) pageMap.get("outgoingUrls");
            List<String> outGoingUrls=new ArrayList<>();
            for (String outUrl: outStrings.split(",", 0)) {
                outUrl = outUrl.replace(" ", "");
                outUrl = outUrl.replace("[", "");
                outUrl = outUrl.replace("]", "");
                outGoingUrls.add(outUrl);
            }

            indexer.processData(url,docid,text,title,outGoingUrls);
        }
        System.out.println(indexer.counter1);

        System.out.println(indexer.counter);





            /*
                   Cawler Part

            Controller cawlercontroller= new Controller();
            cawlercontroller.startCawler();

            */




    /*
           Data Process, Using JDBM

            Set<String> urls = new HashSet<>();


            DB db= new DB(Params.DBPath);
            PrimaryHashMap<String, String> dataTable=db.getMyTable();


            IndexBuilder builder= new IndexBuilder();

            for(String url: dataTable.keySet()){
                String text=dataTable.get(url);
                builder.buildInvertedIndex(url,text);
            }

            DB db2= new DB(Params.DBPath2);
            PrimaryHashMap<String, String> dataTable2=db2.getMyTable();
            for(String url: dataTable2.keySet()){
                String text=dataTable2.get(url);
                builder.buildInvertedIndex(url,text);
            }


    // Using tokenizer for text process (old version)

             TextToenizer tokenizer= new TextToenizer();

            tokenizer.loadStopWordList(new File("longStopWordList"));
            dataTable.remove("http://sli.ics.uci.edu/Classes/2013-iCamp?action=download&upname=yelp_validate.mat");

            tokenizer.loadDBTable(dataTable);
            PrimaryHashMap<String, String> dataTable2=db2.getMyTable();
            tokenizer.loadDBTable(dataTable2);

            System.out.println("Nubmer of subdomains: "+tokenizer.numOfSubdomain());		// print number of subDomains
            tokenizer.printSubdomain();

            System.out.println("Number of URL is "+ tokenizer.urlCount);
            System.out.println("Longest Page is "+ tokenizer.longestpage);

            List<String> tokens=tokenizer.getTokenList();

            FrequencyCounter fc = new FrequencyCounter();
            Map<String,Integer> freqMap= fc.sortMap(fc.CountFrequency(tokens));

            fc.printMapFirstK(freqMap,500);

            Map<String,Integer> trigramMap =fc.sortMap(fc.Compute3GramFrequency(tokens));
            fc.printMapFirstK(trigramMap,20);

    */


            //Bulding index

            //builder.printInvertedIndex();
            //indexer.printTFMap();

            //indexer.printDF();


          //  indexer.computeTFIDF();
           // indexer.outputTFIDF();


          //  indexer.printTFIDF();




            indexer.computePageRank(30);


            /* Testing pageRank
            for(int ID: indexer.pageRankMap.keySet()){
                System.out.println("The document[" + ID +"], page rank is: "+ indexer.pageRankMap.get(ID));
            }
            */


            indexer.outputPageRank();
            indexer.outputTitle();
            indexer.outputUrlIDMap();


            /* Old Version
            builder.outputTFIDF();
            builder.TFIDFtoJson();
            builder.IDListtoJson();

            builder.outputTFIDF();
            builder.outputUrlIDList();
            builder.TFMaptoJson();
            builder.readTFIDFJson();
            System.out.println("Num of URL is "+ builder.getNumURL());
            System.out.println("Num of word is "+ builder.getNumWord());
         */
    }


    // sort subDomain list alphabetically

}
