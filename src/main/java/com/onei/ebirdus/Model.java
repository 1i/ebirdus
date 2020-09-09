package com.onei.ebirdus;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Model model = (Model) o;

        if (date != null ? !date.equals(model.date) : model.date != null) return false;
        return commonName != null ? commonName.equals(model.commonName) : model.commonName == null;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (commonName != null ? commonName.hashCode() : 0);
        return result;
    }
}