
import com.amazonaws.services.lambda.runtime.Context;
import java.time.LocalDate;
import java.util.List;

public class Lambda {

    public void handler(Context context) {
        System.out.println("Starting");

        EbirdClient ebirdClient = new EbirdClient();

        List<Sighting> results = ebirdClient.getRecentSighting(LocalDate.now().minusDays(1));


        if(results.size()>0) {
            S3Client s3Client = new S3Client();
            s3Client.upload(results);
            System.out.println("saved to s3 "+ results.size());
        }
        System.out.println("Finished");
    }
}
