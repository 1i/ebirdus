import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

public class EbirdClient {

    public EbirdClient() {
    }
    ObjectMapper objectMapper = new ObjectMapper();

    public List<Sighting> getRecentSighting(LocalDate date) {

        URL url = getRequest(date,true);
        List<EbirdModel> ebirdModels = null;
        try {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("X-eBirdApiToken", "d4adr470eh9u");
            con.setRequestMethod("GET");

            ebirdModels = objectMapper.readValue(new InputStreamReader(con.getInputStream()), new TypeReference<List<EbirdModel>>(){});

            con.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(ebirdModels != null){
            return ModelTransformer.transformList(ebirdModels);
        }
        throw new NullPointerException();
    }

    @SneakyThrows
    public URL getRequest(LocalDate date, Boolean hotspot) {
        String uri = "https://api.ebird.org/v2/data/obs/IE/recent";
        int dateOffset = LocalDate.now().compareTo(date);
        if(dateOffset == 0){
            throw new IllegalArgumentException("Date must be in  the past 1 - 30 days.");
        }
        uri= uri + "?back="+dateOffset;
        uri= uri + "&hotspot="+hotspot;

        try {
            return new URL(uri);
        } catch (MalformedURLException e) {
            System.out.println("Uri ");
            e.printStackTrace();
        }
        throw new IOException("Uri Error");
    }
}
