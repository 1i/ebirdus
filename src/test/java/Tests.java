import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class Tests {

    ObjectMapper objectMapper = new ObjectMapper();
    File EBIRD_FILE = new File(getClass().getClassLoader().getResource("ebird.json").getFile());

    @Test
    void getLocalFile() throws Exception{
        List<EbirdModel> models = objectMapper.readValue(EBIRD_FILE, new TypeReference<List<EbirdModel>>(){});
        assertEquals(162,models.size());
    }

    @Test
    void transformerTest()throws Exception{

        List<EbirdModel> models = objectMapper.readValue(EBIRD_FILE, new TypeReference<List<EbirdModel>>(){});
        Sighting sighting = ModelTransformer.transform(models.get(0));

        assertEquals("Willow Warbler", sighting.getCommonName());
        assertEquals("Phylloscopus trochilus", sighting.getScientificName());
        assertEquals("Tolka Valley Park", sighting.getLocation());
        assertEquals("2020-08-26", sighting.getDate());
        assertEquals("5", sighting.getCount());
        assertEquals(null, sighting.getPhoto());
        assertEquals(null, sighting.getCounty());
        System.out.println(sighting);


        List<Sighting> sightings = ModelTransformer.transformList(models);
        assertEquals(162,sightings.size());
        Sighting sightingFromList = sightings.get(0);
        assertEquals("Willow Warbler", sightingFromList.getCommonName());
        assertEquals("Phylloscopus trochilus", sightingFromList.getScientificName());
        assertEquals("Tolka Valley Park", sightingFromList.getLocation());
        assertEquals("2020-08-26", sightingFromList.getDate());
        assertEquals("5", sightingFromList.getCount());
        assertEquals(null, sightingFromList.getPhoto());
        assertEquals(null, sightingFromList.getCounty());

    }

    @Test
    void dateOffsetTest(){

        int dateOffset = LocalDate.now().minusDays(1).compareTo(LocalDate.now());
        assertEquals(1,dateOffset*-1);
    }

    @Test
    void getLastWeeksRequestyTest() throws Exception {
        EbirdClient ebirdClient = new EbirdClient();
        URL request = ebirdClient.getRequest(LocalDate.now().minusDays(7), false);
        System.out.println(request);
    }

    @Test
    void getYesterdaysRequestTest()throws Exception {
        EbirdClient ebirdClient = new EbirdClient();
        URL request = ebirdClient.getRequest(LocalDate.now().minusDays(1), false);
        System.out.println(request);
    }

    @Test
    void getHotSpotRequestTest()throws Exception {
        EbirdClient ebirdClient = new EbirdClient();
        URL request = ebirdClient.getRequest(LocalDate.now().minusDays(1), true);
        System.out.println(request);
    }

    @Test
    void getRequestYesterdayHotSpotTest()throws Exception {
        EbirdClient ebirdClient = new EbirdClient();
        URL request = ebirdClient.getRequest(LocalDate.now().minusDays(1), true);
        System.out.println(request);
    }

    @Test
    void lambdaTest(){
        Lambda lambda = new Lambda();
        lambda.handler(Mockito.mock(Context.class));
    }
}
