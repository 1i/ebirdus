import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

public class Tests {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getLocalFile() throws Exception{
        File file = new File(getClass().getClassLoader().getResource("ebird.json").getFile());

        List<EbirdModel> models = objectMapper.readValue(file, new TypeReference<List<EbirdModel>>(){});
        System.out.println(models.size());
        System.out.println(models.get(0));
    }
}
