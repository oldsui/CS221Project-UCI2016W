package Stats;

import java.util.*;

/**
 * Created by Feng Hong on 16/1/19.
 */
public class FrequencyCounter {

    public  Map<String,Integer> CountFrequency(List<String> tokens){
        Map<String,Integer> freqMap= new HashMap<String,Integer>();
        for(int i=0; i<tokens.size();i++){
            String token=tokens.get(i);
            if(freqMap.containsKey(token)){
                int count=freqMap.get(token);
                count++;
                freqMap.remove(token);
                freqMap.put(token,count);
            }else{
                freqMap.put(token,1);
            }
        }

        return freqMap;
    }

    public  Map<String,Integer> sortMap(Map<String,Integer> map){
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return  (o2.getValue()).compareTo(o1.getValue());
            }
        });
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
            Map.Entry<String, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public  void printMap(Map<String, Integer> map) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println("[Key] : " + entry.getKey() + " [Value] : " + entry.getValue());
        }
    }



    public  void printMapFirstK(Map<String, Integer> map, Integer k) {
        int count=0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println("[Key] : " + entry.getKey() + " [Value] : " + entry.getValue());
            count ++;
            if (count >=k) break;
        }
    }

    public  void printMapMoreThanK(Map<String, Integer> map,Integer k) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {

            if(entry.getValue()>k) {
                System.out.println("[Key] : " + entry.getKey() + " [Value] : " + entry.getValue());
            }
        }
    }



    public  Map<String,Integer> Compute3GramFrequency(List<String> tokens){
        Map<String,Integer> threeGramFreqMap= new HashMap<String,Integer>();
        for(int i=0; i<tokens.size()-3;i++){
            StringBuilder sb = new StringBuilder();
            sb.append(tokens.get(i));
            sb.append(' ');
            sb.append(tokens.get(i+1));
            sb.append(' ');
            sb.append(tokens.get(i+2));
            String threeGramToken=sb.toString();

            if(threeGramFreqMap.containsKey(threeGramToken)){
                int count=threeGramFreqMap.get(threeGramToken);
                count++;
                threeGramFreqMap.remove(threeGramToken);
                threeGramFreqMap.put(threeGramToken,count);
            }else{
                threeGramFreqMap.put(threeGramToken,1);
            }

        }
        return threeGramFreqMap;

    }

}
