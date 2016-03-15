    package Stats;

    import com.google.gson.Gson;
    import com.google.gson.reflect.TypeToken;
    import org.json.simple.JSONArray;
    import org.json.simple.JSONObject;
    import org.json.simple.parser.JSONParser;

    import java.io.*;
    import java.lang.reflect.Type;
    import java.util.ArrayList;
    import java.util.Iterator;
    import java.util.List;
    import java.util.Map;

    /**
     * Created by Sam on 16/3/12.
     */
    public class NDCGProcessor {
        private List<List<String>> googleResult=new ArrayList<>();
        // 0-> mondego";
        // 1-> "machine learning";
        // 2-> "software engineering";
        // 3-> "security";
        // 4-> "student affairs";
        // 5-> "graduate courses";
        // 6-> "Crista Lopes";
        // 7-> "REST";
        // 8-> "computer games";
        // 9-> "information retrieval";

        public  NDCGProcessor() {
            List<String> modegoResult = new ArrayList<>();

           for (int i = 0; i < 10; i++){
               List<String> results = new ArrayList<>();
               JSONParser parser = new JSONParser();
                try {
                    Object obj = parser.parse(new FileReader(
                        "/Users/Sam/Desktop/IR_P2/GoogleResult/"+i+".txt"));
                    JSONObject jsonObject = (JSONObject) obj;
                    JSONObject obj1 = (JSONObject) jsonObject.get("responseData");
                    JSONArray arr = (JSONArray) obj1.get("results");
                    for(int j=0;j<5;j++) {
                        JSONObject obj2 = (JSONObject) arr.get(j);
                        String url = (String) obj2.get("url");
                        results.add(url);
                        System.out.println(url);
                    }
                    System.out.println("  ");

                    googleResult.add(results);

                } catch (Exception e) {
                e.printStackTrace();
            }
        }

        }


    }
