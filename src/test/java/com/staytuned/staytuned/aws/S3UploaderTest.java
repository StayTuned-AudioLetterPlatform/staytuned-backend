package com.staytuned.staytuned.aws;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.staytuned.staytuned.endpoint.user.UserService;
import com.staytuned.staytuned.endpoint.voicemail.VoicemailController;
import com.staytuned.staytuned.endpoint.voicemail.VoicemailService;
import com.staytuned.staytuned.security.config.SecurityConfig;
import com.staytuned.staytuned.security.config.WebConfig;
import com.staytuned.staytuned.security.jwt.JwtAuthorizationArgumentResolver;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Import({S3MockConfig.class, S3UploadComponent.class})
@WebMvcTest(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthorizationArgumentResolver.class)
})
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

    @MockBean
    private UserService userService;

    @MockBean
    private VoicemailService voicemailService;

    @MockBean
    private VoicemailController voicemailController;

    @AfterEach
    public void tearDown() {
        s3Mock.stop();
    }

    String path = "test_image.png";
    String contentType = "image/png";
    String fileName = "test";


    @Test
    void upload() throws IOException {
        // given
        MockMultipartFile file = new MockMultipartFile(fileName, fileName, contentType, fileName.getBytes());

        // when
        String fileUrl  = s3UploadComponent.upload(file, 1L);
        System.out.println(fileUrl);

        // then
        assertThat(fileUrl).contains(fileName);
    }

    @Test
    void delete() throws IOException {
        //given
        MockMultipartFile file = new MockMultipartFile(fileName, fileName, contentType, fileName.getBytes());
        String fileUrl  = s3UploadComponent.upload(file, 1L);

        String[] URL = fileUrl.split("/");
        String uploadFileName = URL[URL.length-2] + "/" + URL[URL.length-1];

        //when
        String uploadFileURl = s3UploadComponent.getUrl(uploadFileName).toString();
        s3UploadComponent.delete(uploadFileName);

        //then
        assertThat(fileUrl).isEqualTo(uploadFileURl);
        assertThrows(AmazonS3Exception.class, () -> s3UploadComponent.getObject(uploadFileName));

    }
}