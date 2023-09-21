package com.staytuned.staytuned.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3UploadComponent {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}") //해당 값은 application.yml 에서 작성한 cloud.aws.credentials.accessKey 값을 가져옵니다.
    private String bucket;

    public String getObject(String objectName) throws AmazonClientException {
        return amazonS3Client.getObject(bucket, objectName).toString();
    }

    public String upload(MultipartFile multipartFile, Long userCd) throws IOException {//MultipartFile 을 전달 받고
        ObjectMetadata objectMetadata = getMetadata(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> objectMetadata 전환이 실패했습니다."));

        return upload(multipartFile, multipartFile.getName(), objectMetadata, userCd); //업로드된 파일의 S3 URL 주소를 반환
    }

    public void delete(String fileName){
        amazonS3Client.deleteObject(bucket, fileName);
    }

    public void deleteFolder(String folderName){
        ObjectListing objectListing = amazonS3Client.listObjects(bucket, folderName);
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            // 각 객체를 삭제
            amazonS3Client.deleteObject(bucket, objectSummary.getKey());
        }
        amazonS3Client.deleteObject(bucket, folderName);
    }

    public InputStreamResource downloadFile(String fileUrl) throws IOException {
        try (S3Object s3Object = amazonS3Client.getObject(bucket, fileUrl);
             InputStream inputStream = s3Object.getObjectContent()) {

            return new InputStreamResource(inputStream);
        }
    }

    public String getUrl(String objectName) throws AmazonClientException { //
        return amazonS3Client.getUrl(bucket, objectName).toString();
    }

    private String upload(MultipartFile uploadFile, String originFileName, ObjectMetadata objectMetadata, Long userCd) throws IOException {
        String contentType = Objects.requireNonNull(uploadFile.getContentType()).split("/")[1];
        String fileName =  "user_" + userCd + "/"+ originFileName + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+  "." + contentType;
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
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        }
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }
}
