package Stats;

import java.io.*;
import java.util.*;

/**
 * Created by Feng Hong on 16/1/20.
 */
public class Anagram {

    public List<String> makeDictionaryFromFile(File input) throws IOException {
        List<String> dictionary = new ArrayList<String>();
        Map<String,Integer> dictionaryMap= new HashMap<String,Integer>();
        BufferedReader bf = new BufferedReader(new FileReader(input));
        String line;
        while ((line = bf.readLine()) != null) {
            StringTokenizer tokenizer =new StringTokenizer(line,"://,//.//\n//\t//\r//\f// //'//\"//(//)//{//}//[//]//|//<//>" +
                    "//+//=//!//@//#//$//%//^//&//*//;//`//_");
            while(tokenizer.hasMoreTokens()){
                String str=tokenizer.nextToken().toLowerCase();
                if(!isNum(str)){
                    if(!dictionaryMap.containsKey(str)){
                        dictionaryMap.put(str,1);
                    }
                }
            }

        }

        Iterator it = dictionaryMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            dictionary.add(String.valueOf(pair.getKey()));
            it.remove(); // avoids a ConcurrentModificationException
        }

        return dictionary;
    }


    private boolean isNum(String str){
        for (char c : str.toCharArray()){
            if (!Character.isDigit(c)){
                return false;
            }
        }
        return true;

    }

    public void printDic(List<String> dic){
        for(int i=0;i<dic.size();i++){
            System.out.println(dic.get(i));
        }
    }


    public Map<String,List<String>> buildDataMap(List<String> dic){
        Map<String,List<String>> dataMap =new HashMap<String,List<String>>();

        for(int i=0;i<dic.size();i++){
            String str=dic.get(i);
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String sortedStr = new String(chars);

            if(dataMap.containsKey(sortedStr)) {
                List<String> temp = dataMap.get(sortedStr);
                temp.add(str);
                dataMap.put(sortedStr, temp);
            }
            else{
                List<String> temp =new ArrayList<String>();
                temp.add(str);
                dataMap.put(sortedStr, temp);
            }

        }

        return dataMap;
    }


    public void serializeDataMap(Map<String,List<String>> dataMap) throws IOException {
        FileOutputStream fos = new FileOutputStream("dataMap.ser");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(dataMap);
        oos.close();
    }


    public Map<String,List<String>>  deserializeDataMap() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("dataMap.ser");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Map dataMap = (Map) ois.readObject();
        ois.close();
        return dataMap;
    }


    public void printAnagramMap(Map<String,List<String>> dataMap){
        Iterator it = dataMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            List<String> anagramList = (List<String>) pair.getValue();
            System.out.print("[Token] : " + pair.getKey() + " [anagram] : " );

            for (int i = 0; i < anagramList.size(); i++) {
                System.out.print(anagramList.get(i)+' ');
            }
            System.out.println();
            it.remove(); // avoids a ConcurrentModificationException
        }
    }


    public Map<String,List<String>> detectAnagrams (List<String> tokens,Map<String,List<String>> dataMap ){
        Map<String,List<String>> anagramMap =new TreeMap<String,List<String>>();

        for(int i=0;i<tokens.size();i++){
            String token=tokens.get(i);
            char[] chars = token.toCharArray();
            Arrays.sort(chars);
            String sortedToken = new String(chars);
            if(dataMap.containsKey(sortedToken)){
                List<String> tokenAnagram = dataMap.get(sortedToken);
                anagramMap.put(token,tokenAnagram);
            }else{
                List<String> tokenAnagram = new ArrayList<String>();
                anagramMap.put(token,tokenAnagram);
            }
        }
        return anagramMap;
    }



}
