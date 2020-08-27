import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class EbirdClient {

    public EbirdClient() {
    }
    ObjectMapper objectMapper = new ObjectMapper();

    void getSightsing() throws MalformedURLException {

        System.out.println("REquest");
        URL url = new URL("https://api.ebird.org/v2/data/obs/IE/recent");
        try {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("X-eBirdApiToken", "d4adr470eh9u");
            con.setRequestMethod("GET");

            System.out.println(con.getResponseCode());
            List<Model> model = objectMapper.readValue(new InputStreamReader(con.getInputStream()), new TypeReference<List<Model>>(){});
            System.out.println(model.size());
            System.out.println(model.get(0));

            con.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
