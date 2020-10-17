import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambdaAsync;
import com.amazonaws.services.lambda.AWSLambdaAsyncClientBuilder;
import com.amazonaws.services.lambda.model.InvocationType;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onei.ebirdus.EbirdClient;
import com.onei.ebirdus.Model;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Properties;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
public class Tests {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private File EBIRD_FILE = new File(getClass().getClassLoader().getResource("ebird.json").getFile());
    private final LocalDate YESTERDAY = LocalDate.now().minusDays(1);
    private final LocalDate TWODAYSAGO = LocalDate.now().minusDays(2);
    private final LocalDate LAST_WEEK = LocalDate.now().minusDays(7);
    private final String THE_KINGDOM = "kerry";
    private final String REAL_REPUBLIC = "CORK";
    private final String IRELAND_CODE = "IE";
    private final String IRELAND = "IRELAND";
    AWSLambdaAsync awsLambdaAsync = AWSLambdaAsyncClientBuilder.standard()
            .withCredentials(new DefaultAWSCredentialsProviderChain())
            .withRegion(Regions.EU_WEST_1)
            .build();
    String functionName = "arn:aws:lambda:eu-west-1:585889682825:function:ebirdus";


    @Test
    void getLocalFile() throws Exception {
        List<Model> models = objectMapper.readValue(EBIRD_FILE, new TypeReference<List<Model>>() {
        });
        assertEquals(162, models.size());
    }

    @Test
    void transformerTest() throws Exception {

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
        assertEquals(162, models.size());
    }

    @Test
    void dateOffsetTest() {
        assertEquals(1, Period.between(YESTERDAY, LocalDate.now()).getDays());
        assertEquals(7, Period.between(LAST_WEEK, LocalDate.now()).getDays());
        assertEquals(10, Period.between(LocalDate.now().minusDays(10), LocalDate.now()).getDays());
    }

    @Test
    void getRequestYesterdayHotSpotTest() {
        URL request = EbirdClient.getRequestURL(YESTERDAY, true, THE_KINGDOM);
        assertTrue(request.toString().contains("hotspot=true"));
    }

    @Test
    void propertiesTest() throws Exception {
        String rootPath = EbirdClient.class.getClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "counties.properties";
        Properties appProps = new Properties();
        appProps.load(new FileInputStream(appConfigPath));
        assertEquals("IE-C-MO", appProps.get("mayo"));
    }


    void spacesInNamepropertiesTest() throws Exception {
        String rootPath = EbirdClient.class.getClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "counties.properties";
        Properties appProps = new Properties();
        appProps.load(new FileInputStream(appConfigPath));
        assertNotEquals("IE-L-WH", appProps.get("west meath"));
    }

    void spacesInCountyName() {
        Assertions.assertThrows(IOException.class, () -> {
            EbirdClient.getResults(YESTERDAY, "MADE UP");
        });
    }

    @Test
    void getRecentBirdsInKerry() {
        URL request = EbirdClient.getRequestForLocation(LAST_WEEK, THE_KINGDOM);
        assertTrue(request.toString().contains("KY"));
        assertTrue(request.toString().contains("back=7"));
    }

    @Test
    void getRecentBirds() {
        String recentSighting = EbirdClient.getResults(YESTERDAY, IRELAND);
        assertTrue(recentSighting.contains(IRELAND));
    }

    @Test
    void getLastWeeksBirds() {
        String recentSighting = EbirdClient.getResults(LAST_WEEK, IRELAND);
        assertTrue(recentSighting.contains(IRELAND));
    }

    @Test
    void getRecentBirdsInCork() {
        String recentSighting = EbirdClient.getResults(YESTERDAY, REAL_REPUBLIC);
        assertTrue(recentSighting.contains(REAL_REPUBLIC));
    }

    @Test
    void getLastWeeksBirdsInCork() {
        String recentSighting = EbirdClient.getResults(LAST_WEEK, REAL_REPUBLIC);
        assertTrue(recentSighting.contains(REAL_REPUBLIC));
    }

    @Test
    void getLastWeeksBirdsInMadeupLocation() {
        String recentSighting = EbirdClient.getResults(YESTERDAY, "Nowhere");
        assertTrue(recentSighting.contains("Sorry"));
    }

    @Test
    void getNotableUrl() {
        URL request = EbirdClient.getNotableURL(YESTERDAY, IRELAND_CODE);
        assertTrue(request.toString().contains(IRELAND_CODE));
        assertTrue(request.toString().contains("/recent/notable"));
    }

    @Test
    void invokeLambda() throws Exception {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("launchRequest.json");
        ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
        while (resourceAsStream.available() > 0) {
            byteBuffer.put((byte) resourceAsStream.read());
        }
        String inputJSON = "{\"test\":\"value\",\"key\": \"value\"}";
        URI launchRequest = this.getClass().getClassLoader().getResource("launchRequest.json").toURI();
        Object spayload = objectMapper.readValue(new File(launchRequest), Object.class);


        String ascii = new String(byteBuffer.array(), "UTF-8");
        InvokeRequest invokeRequest = new InvokeRequest()
                .withFunctionName(functionName)
                .withPayload(objectMapper.writeValueAsString(spayload));


        InvokeResult result = awsLambdaAsync.invoke(invokeRequest);

        ByteBuffer resultPayload = result.getPayload();
        String output = new String(resultPayload.array());
        log.info("output {}", output);
        String[] split = output.split("<speak>");
        assertTrue(split[1].contains("In Ireland"));
        log.info("Spoken {}", split[1]);

    }

    @Test
    void invokeRequest() throws Exception {

        String inputJSON = "{\"test\":\"value\",\"key\": \"value\"}";
        URI launchRequest = this.getClass().getClassLoader().getResource("TextMessageDefaultEvent.json").toURI();
        Object payload = objectMapper.readValue(new File(launchRequest), Object.class);
        InvokeRequest invokeRequest = new InvokeRequest()
                .withFunctionName(functionName)
                .withInvocationType(InvocationType.Event)
                .withPayload(objectMapper.writeValueAsString(payload));


        InvokeResult result = awsLambdaAsync.invoke(invokeRequest);
        ByteBuffer resultPayload = result.getPayload();
        String output = new String(resultPayload.array());
        log.info("output {}", output);
        String[] split = output.split("<speak>");
        // assertTrue(split[1].contains("In Ireland"));
        //log.info("Spoken {}", split[1]);
    }
}
