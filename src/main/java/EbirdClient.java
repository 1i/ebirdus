import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

@Slf4j
public class EbirdClient {

    @SneakyThrows
    public EbirdClient() {
        appProps.load(new FileInputStream(appConfigPath));
    }
    private static ObjectMapper objectMapper = new ObjectMapper();
    String rootPath = EbirdClient.class.getClassLoader().getResource("").getPath();
    String appConfigPath = rootPath + "counties.properties";

    static Properties appProps = new Properties();


    public static List<EbirdModel> getRecentSighting(LocalDate date) {

        URL url = getRequest(date,true, null);
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
            return ebirdModels;
        }
        throw new NullPointerException();
    }

    @SneakyThrows
    public static URL getRequest(LocalDate date, Boolean hotspot, String location) {
        if(location != null || location.isEmpty()){
            location = "IE";
        }
        String uri = "https://api.ebird.org/v2/data/obs/"+location+"/recent";
        log.info("Request {}", uri);
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

    public static void getRequestForLocation(LocalDate last_week, String location) {

        String locationCode = appProps.getProperty(location);
        getRequest(last_week,true,locationCode);
    }
}
