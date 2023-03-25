package aws;

import com.staytuned.staytuned.StayTunedApplication;
import com.staytuned.staytuned.aws.S3UploadComponent;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@Import(S3MockConfig.class)
@SpringBootTest(classes = StayTunedApplication.class)
public class S3UploaderTest {
    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    @Autowired
    private S3Mock s3Mock;

    @Autowired
    private S3UploadComponent s3UploadComponent;

    @AfterEach
    public void tearDown() {
        s3Mock.stop();
    }

    @Test
    void upload() throws IOException {
        // given
        String path = "test_image.png";
        String contentType = "image/png";
        String fileName = "test";

        MockMultipartFile file = new MockMultipartFile("test", fileName, contentType, "test".getBytes());

        // when
        String urlPath = s3UploadComponent.upload(file, fileName);

        // then
        assertThat(urlPath).contains(fileName);
    }
}
