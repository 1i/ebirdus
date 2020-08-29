import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EbirdModel {

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



}