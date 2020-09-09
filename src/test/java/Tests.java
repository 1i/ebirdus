import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onei.ebirdus.EbirdClient;
import com.onei.ebirdus.Model;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Properties;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@Slf4j
public class Tests {

    private ObjectMapper objectMapper = new ObjectMapper();
    private File EBIRD_FILE = new File(getClass().getClassLoader().getResource("ebird.json").getFile());
    private LocalDate YESTERDAY = LocalDate.now().minusDays(1);
    private LocalDate TWODAYSAGO = LocalDate.now().minusDays(2);
    private LocalDate LAST_WEEK = LocalDate.now().minusDays(7);
    private String location = "kerry";
    private String IRELAND_CODE = "IE";

    @Test
    void getLocalFile() throws Exception{
        List<Model> models = objectMapper.readValue(EBIRD_FILE, new TypeReference<List<Model>>() {
        });
        assertEquals(162,models.size());
    }

    @Test
    void transformerTest()throws Exception{

        List<Model> models = objectMapper.readValue(EBIRD_FILE, new TypeReference<List<Model>>() {
        });
        Model sighting = models.get(0);

        assertEquals("Willow Warbler", sighting.getCommonName());
        assertEquals("Phylloscopus trochilus", sighting.getScientificName());
        assertEquals("Tolka Valley Park", sighting.getLocation());
        assertEquals("2020-08-26", sighting.getDate());
        assertEquals("5", sighting.getCount());
        assertTrue(sighting.getLat().contains("53"));
        assertTrue(sighting.getLng().contains("-6"));
        assertEquals(162,models.size());
    }

    @Test
    void dateOffsetTest(){
        assertEquals(1,Period.between(YESTERDAY,LocalDate.now()).getDays());
        assertEquals(7,Period.between(LAST_WEEK,LocalDate.now()).getDays());
        assertEquals(10, Period.between(LocalDate.now().minusDays(10),LocalDate.now()).getDays());
    }

    @Test
    void getLastWeeksRequestyTest() throws Exception {
        URL request = EbirdClient.getRequestURL(LAST_WEEK, false, location);
        System.out.println(request);
    }

    @Test
    void getYesterdaysRequestTest()throws Exception {
        URL request = EbirdClient.getRequestURL(YESTERDAY, false, location);
        System.out.println(request);
    }

    @Test
    void getHotSpotRequestTest()throws Exception {
        URL request = EbirdClient.getRequestURL(YESTERDAY, true, location);
        System.out.println(request);
    }

    @Test
    void getRequestYesterdayHotSpotTest()throws Exception {
        URL request = EbirdClient.getRequestURL(YESTERDAY, true, location);
        System.out.println(request);
    }


    @Test
    void propertiesTest() throws Exception{
        String rootPath = EbirdClient.class.getClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "counties.properties";

        Properties appProps = new Properties();
        appProps.load(new FileInputStream(appConfigPath));
        assertEquals("IE-C-MO",appProps.get("Mayo"));
    }

    @Test
    void getRecentBirdsInKerry(){
        URL request = EbirdClient.getRequestForLocation(LAST_WEEK, "Kerry");
        assertTrue(request.toString().contains("KY"));
        assertTrue(request.toString().contains("back=7"));
    }

    @Test
    void getRecentBirds() {
        String recentSighting = EbirdClient.getResults(LocalDate.now().minusDays(1));
        System.out.println(recentSighting);
    }

    @Test
    void getNotableUrl() {
        URL request = EbirdClient.getNotableSightings(YESTERDAY, IRELAND_CODE);
        assertTrue(request.toString().contains(IRELAND_CODE));
        assertTrue(request.toString().contains("/recent/notable"));

    }
}
