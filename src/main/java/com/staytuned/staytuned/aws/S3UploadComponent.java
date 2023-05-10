package com.staytuned.staytuned.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.staytuned.staytuned.endpoint.voicemail.VoicemailResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3UploadComponent {

    private final AmazonS3Client amazonS3Client;


    @Value("${cloud.aws.s3.bucket}") //해당 값은 application.yml 에서 작성한 cloud.aws.credentials.accessKey 값을 가져옵니다.
    private String bucket;

    public void createFolder(String folderName) {
        amazonS3Client.putObject(bucket, folderName + "/", new ByteArrayInputStream(new byte[0]), new ObjectMetadata());
    }

    public String getObject(String objectName) throws AmazonClientException {
        return amazonS3Client.getObject(bucket, objectName).toString();
    }

    public String upload(MultipartFile multipartFile) throws IOException {//MultipartFile 을 전달 받고
        ObjectMetadata objectMetadata = getMetadata(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> objectMetadata 전환이 실패했습니다."));

        return upload(multipartFile, multipartFile.getName(), objectMetadata); //업로드된 파일의 S3 URL 주소를 반환
    }

    public void delete(String fileName){
        amazonS3Client.deleteObject(bucket, fileName);
    }

    public String getUrl(String objectName) throws AmazonClientException {
        return amazonS3Client.getUrl(bucket, objectName).toString();
    }

    private String upload(MultipartFile uploadFile, String originFileName, ObjectMetadata objectMetadata) throws IOException {
        String contentType = Objects.requireNonNull(uploadFile.getContentType()).split("/")[1];
        String fileName =   originFileName + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+  "." + contentType;
        return putS3(uploadFile, fileName, objectMetadata);
    }

    private Optional<ObjectMetadata> getMetadata(MultipartFile multipartFile){
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());
        return Optional.of(objectMetadata);
    }

    private String putS3(MultipartFile uploadFile, String fileName, ObjectMetadata objectMetadata) throws IOException {
        try (InputStream inputStream = uploadFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead)); ////외부에 공개할 파일임, 해당 파일에 public read 권한을 추가
        }
        return amazonS3Client.getUrl(bucket, fileName).toString(); //업로드를 한 후, 해당 URL을 DB에 저장할 수 있도록 컨트롤러로 URL 을 반환
    }


}
