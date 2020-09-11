package com.onei.ebirdus;

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
import java.time.Period;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class EbirdClient {
    @SneakyThrows
    public EbirdClient() {
    }

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static String rootPath = EbirdClient.class.getClassLoader().getResource("").getPath();
    private static String appConfigPath = rootPath + "counties.properties";
    private static StringBuilder stringBuilder = new StringBuilder();
    private static Properties appProps = new Properties();
    private static String IRELAND_CODE = "IE";


    public static List<Model> doRequest(URL url) {

        List<Model> models = null;
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("X-eBirdApiToken", "d4adr470eh9u");
            con.setRequestMethod("GET");

            models = objectMapper.readValue(new InputStreamReader(con.getInputStream()), new TypeReference<List<Model>>() {
            });

            con.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (models != null) {
            return models;
        }
        throw new NullPointerException();
    }

    @SneakyThrows
    public static URL getRequestURL(LocalDate date, Boolean hotspot, String locationCode) {
        String uri = "https://api.ebird.org/v2/data/obs/" + locationCode + "/recent/";
        int dateOffset = Period.between(LocalDate.now(),date).getDays()*-1;
        if(dateOffset == 0){
            throw new IllegalArgumentException("Date must be in  the past 1 - 30 days.");
        }
        uri= uri + "?back="+dateOffset;
        uri= uri + "&hotspot="+hotspot;

        try {
            log.info("Request {}", uri);
            return new URL(uri);
        } catch (MalformedURLException e) {
            System.out.println("Uri ");
            e.printStackTrace();
        }
        throw new IOException("Uri Error");
    }

    @SneakyThrows
    public static URL getNotableSightings(LocalDate date, String locationCode) {
        URL request = getRequestURL(date, false, locationCode);

        String[] split = request.toString().split("recent/");
        try {
            URL url = new URL(split[0] + "recent/notable" + split[1]);
            log.info("Request {}", url);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        throw new IOException("URI error");
    }

    public static URL getRequestForLocation(LocalDate last_week, String location) {

        String locationCode = getLocationCode(location);
        return getRequestURL(last_week, true, locationCode);
    }

    private static String getLocationCode(String location) {
        try {
            appProps.load(new FileInputStream(appConfigPath));
        } catch (IOException e) {
            log.error("Config file load error");
            e.printStackTrace();
        }
        String locationCode = appProps.getProperty(location.toLowerCase());

        if (locationCode == null || locationCode.isEmpty()){
            return IRELAND_CODE;
        }
        return locationCode;
    }


    public static String getResults(LocalDate date) {

        List<Model> models = doRequest(getRequestURL(date, true, IRELAND_CODE));
        List<Model> distinct = models.stream().distinct().collect(Collectors.toList());
        stringBuilder.append("Yesterday had " + distinct.size() + " sightings. ");

        for (Model model : distinct) {
            stringBuilder.append(model.getCommonName() + ", ");
        }
        return stringBuilder.toString();
    }

    public static List<Model> getDefaultRequest() {
        return doRequest(getRequestURL(LocalDate.now().minusDays(7), true, IRELAND_CODE));
    }


}