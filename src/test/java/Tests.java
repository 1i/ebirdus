import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

public class Tests {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getLocalFile() throws Exception{
        File file = new File(getClass().getClassLoader().getResource("ebird.json").getFile());

        List<Model> models = objectMapper.readValue(file, new TypeReference<List<Model>>(){});
        System.out.println(models.size());
        System.out.println(models.get(0));
    }
}
