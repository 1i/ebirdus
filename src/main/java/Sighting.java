import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
//A bird sighting
public class Sighting {

    //reference is a unique increasing ID for every sighting e.g IB129006
    //it is NOT used in equals/compareTo
    private String reference;
    private String date;
    private String commonName;
    private String scientificName;
    private String count;
    private String location;
    private String county;
    private String photo;   // Boolean string "Yes" or ""

    // A sighting is the the same if the birdName, date and county are the same.
    // Even
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sighting sighting = (Sighting) o;

        if (date != null ? !date.equals(sighting.date) : sighting.date != null) return false;
        if (commonName != null ? !commonName.equals(sighting.commonName) : sighting.commonName != null) return false;
        return county != null ? county.equals(sighting.county) : sighting.county == null;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (commonName != null ? commonName.hashCode() : 0);
        result = 31 * result + (county != null ? county.hashCode() : 0);
        return result;
    }
}