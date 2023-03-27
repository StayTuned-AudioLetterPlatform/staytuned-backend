package aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.staytuned.staytuned.StayTunedApplication;
import com.staytuned.staytuned.aws.S3UploadComponent;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Import(S3MockConfig.class)
@SpringBootTest(classes = StayTunedApplication.class)
public class S3UploaderTest {
    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    @Autowired
    private S3Mock s3Mock;

    @Autowired
    private S3UploadComponent s3UploadComponent;


    @AfterEach
    public void tearDown() {
        s3Mock.stop();
    }

    @Test
    String  upload() throws IOException {
        // given
        String path = "test_image.png";
        String contentType = "image/png";
        String fileName = "delete";

        MockMultipartFile file = new MockMultipartFile("test", fileName, contentType, "test".getBytes());

        // when
        String urlPath = s3UploadComponent.upload(file, fileName);

        // then
        assertThat(urlPath).contains(fileName);

        return urlPath;
    }

    @Test
    void delete() throws IOException {
        //given
        String fileURl = upload();
        System.out.println(fileURl);
        String[] URL = fileURl.split("/");
        String uploadFileName = URL[URL.length-1];

        //when
        String uploadFileURl = s3UploadComponent.getUrl(uploadFileName).toString();
        s3UploadComponent.delete(uploadFileName);

        //then
        assertThat(fileURl).isEqualTo(uploadFileURl);
        assertThrows(AmazonS3Exception.class, () -> s3UploadComponent.getObject(uploadFileName));

    }
}
