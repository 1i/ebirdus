import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;

public class Model {

    private String subId;
    @JsonDeserialize(converter = DateSplitter.class)

    @JsonProperty("obsDt")
    private String date;
    @JsonProperty("comName")
    private String commonName;
    private String speciesCode;
    @JsonProperty("sciName")
    private String scientificName;
    @JsonProperty("howMany")
    private String count;
    @JsonProperty("locName")
    private String location;
    private String locId;
    private String obsValid;
    private String obsReviewed;
    private String locationPrivate;
    private String lat;
    private String lng;


    @JsonDeserialize(converter = DateSplitter.class)
    public void setDate(String date) {
        this.date = date;
    }

    public static class DateSplitter extends StdConverter<String,String>{

        @Override
        public String convert(String s) {
            return s.split(" ")[0];
        }
    }

    @Override
    public String toString() {
        return "Model{" +
                "subId='" + subId + '\'' +
                ", date='" + date + '\'' +
                ", commonName='" + commonName + '\'' +
                ", speciesCode='" + speciesCode + '\'' +
                ", scientificName='" + scientificName + '\'' +
                ", count='" + count + '\'' +
                ", location='" + location + '\'' +
                ", locId='" + locId + '\'' +
                ", obsValid='" + obsValid + '\'' +
                ", obsReviewed='" + obsReviewed + '\'' +
                ", locationPrivate='" + locationPrivate + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                '}';
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getSpeciesCode() {
        return speciesCode;
    }

    public void setSpeciesCode(String speciesCode) {
        this.speciesCode = speciesCode;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getObsValid() {
        return obsValid;
    }

    public void setObsValid(String obsValid) {
        this.obsValid = obsValid;
    }

    public String getObsReviewed() {
        return obsReviewed;
    }

    public void setObsReviewed(String obsReviewed) {
        this.obsReviewed = obsReviewed;
    }

    public String getLocationPrivate() {
        return locationPrivate;
    }

    public void setLocationPrivate(String locationPrivate) {
        this.locationPrivate = locationPrivate;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public Model() {
    }

}