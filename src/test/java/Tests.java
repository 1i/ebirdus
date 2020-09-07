import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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

    ObjectMapper objectMapper = new ObjectMapper();
    File EBIRD_FILE = new File(getClass().getClassLoader().getResource("ebird.json").getFile());

    private LocalDate YESTERDAY = LocalDate.now().minusDays(1);
    private LocalDate TWODAYSAGO = LocalDate.now().minusDays(2);
    private LocalDate LAST_WEEK = LocalDate.now().minusDays(7);
    private String location = "kerry";

    @Test
    void getLocalFile() throws Exception{
        List<EbirdModel> models = objectMapper.readValue(EBIRD_FILE, new TypeReference<List<EbirdModel>>(){});
        assertEquals(162,models.size());
    }

    @Test
    void transformerTest()throws Exception{

        List<EbirdModel> models = objectMapper.readValue(EBIRD_FILE, new TypeReference<List<EbirdModel>>(){});
        EbirdModel sighting = models.get(0);

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
        EbirdClient ebirdClient = new EbirdClient();
        URL request = ebirdClient.getRequest(LAST_WEEK, false, location);
        System.out.println(request);
    }

    @Test
    void getYesterdaysRequestTest()throws Exception {
        EbirdClient ebirdClient = new EbirdClient();
        URL request = ebirdClient.getRequest(YESTERDAY, false, location);
        System.out.println(request);
    }

    @Test
    void getHotSpotRequestTest()throws Exception {
        EbirdClient ebirdClient = new EbirdClient();
        URL request = ebirdClient.getRequest(YESTERDAY, true, location);
        System.out.println(request);
    }

    @Test
    void getRequestYesterdayHotSpotTest()throws Exception {
        EbirdClient ebirdClient = new EbirdClient();
        URL request = ebirdClient.getRequest(YESTERDAY, true, location);
        System.out.println(request);
    }

    @Test
    void lambdaTest(){
        Lambda lambda = new Lambda();
        lambda.handler(Mockito.mock(Context.class));
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
    void getNotable(){
        List<EbirdModel> recentSighting = EbirdClient.getRecentSighting(YESTERDAY);
        log.info(" ",recentSighting.get(0));
        log.info(recentSighting.get(0).toString());
    }

    @Test
    void getRecentBirdsInKerry(){
        assertEquals("IE-M-KY",EbirdClient.getLocationCode("Kerry"));
        assertEquals("GB-NIR-DOW",EbirdClient.getLocationCode("down"));

        URL request = EbirdClient.getRequestForLocation(LAST_WEEK, "Kerry");
        System.out.println(request);
        assertTrue(request.toString().contains("KY"));
    }
}
