import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class S3Client {


    public void upload(List models) {
        String BUCKET = "ebirdus";
        String todaysDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yy-MM-dd"));
        String keyName = String.format("%s.json", todaysDate);

        ObjectMapper objectMapper = new ObjectMapper();
        String s = null;
        try {
            s = objectMapper.writeValueAsString(models);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        byte[] bytes = new byte[0];
        try {
            bytes = objectMapper.writeValueAsBytes(models);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        InputStream byteArrayInputStream = new ByteArrayInputStream(s.getBytes());

        System.out.println("Uploading to s3");
        try {
            AmazonS3 s3Client;
            s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(Regions.EU_WEST_1)
                    .build();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("application/json");
            metadata.addUserMetadata("x-amz-meta-title", "ebirdus");
            metadata.setContentLength(bytes.length);
            PutObjectRequest request = new PutObjectRequest(BUCKET, keyName, byteArrayInputStream, metadata);

            PutObjectResult putObjectResult = s3Client.putObject(request);

            System.out.println("Uploaded to s3 : " + putObjectResult.getETag());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}