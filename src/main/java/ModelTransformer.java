import java.util.ArrayList;
import java.util.List;

public class ModelTransformer {

    public static Sighting transform(EbirdModel ebirdModel){

        Sighting sighting = new Sighting();

        sighting.setCommonName(ebirdModel.getCommonName());
        sighting.setLocation(ebirdModel.getLocation());
        sighting.setDate(ebirdModel.getDate());
        sighting.setScientificName(ebirdModel.getScientificName());
        sighting.setCount(ebirdModel.getCount());
        sighting.setReference("eb-"+ebirdModel.getSpeciesCode()+"-"+ebirdModel.getSubId());

        return sighting;
    }

    public static List<Sighting> transformList(List<EbirdModel>ebirdModels){

        List<Sighting> sightings = new ArrayList<>();
        for (EbirdModel ebirdModel : ebirdModels){
            sightings.add(transform(ebirdModel));
        }

        return sightings;
    }
}
