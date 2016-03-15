import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sam on 16/3/12.
 */
public class GoogleResult {


    public static void main(String[] args) throws Exception {

        List<String>  queres =new ArrayList<>();


        String key0="mondego";
        String key1="machine%20learning";
        String key2="software%20engineering";
        String key3="security";
        String key4="student%20affairs";
        String key5="graduate%20courses";
        String key6="Crista%20Lopes";
        String key7="REST";
        String key8="computer%20games";
        String key9="information%20retrieval";


        queres.add(key0);
        queres.add(key1);
        queres.add(key2);
        queres.add(key3);
        queres.add(key4);
        queres.add(key5);
        queres.add(key6);
        queres.add(key7);
        queres.add(key8);
        queres.add(key9);

        PrintStream stdout = System.out;


        PrintStream out = new PrintStream(new FileOutputStream("GoogleResult1.txt"));
        System.setOut(out);
        for(String query : queres) {
            URL url = new URL("http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=" + query + "++site:ics.uci.edu+&rsz=5");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
           // System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            conn.disconnect();
        }
        System.setOut(stdout);

    }

}
