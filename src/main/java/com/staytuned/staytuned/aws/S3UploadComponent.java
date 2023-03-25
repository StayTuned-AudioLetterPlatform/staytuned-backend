package com.staytuned.staytuned.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
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


    public String upload(MultipartFile multipartFile, String fileName) throws IOException {//MultipartFile 을 전달 받고
        ObjectMetadata objectMetadata = getMetadata(multipartFile)//S3에 전달할 수 있도록 MultiPartFile 을 File 로 전환
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> objectMetadata 전환이 실패했습니다."));
        //S3에 MultipartFile 타입은 전송이 안됨

        return upload(multipartFile, fileName, objectMetadata);
        //업로드된 파일의 S3 URL 주소를 반환
    }
    private String upload(MultipartFile uploadFile, String originFileName, ObjectMetadata objectMetadata) throws IOException {
        String fileName =  getFileName(originFileName);
        return putS3(uploadFile, fileName, objectMetadata);
    }

    private Optional<ObjectMetadata> getMetadata(MultipartFile multipartFile){
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());
        return Optional.of(objectMetadata);
    }

    private String getFileName(String fileName){
        return fileName + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }


    private String putS3(MultipartFile uploadFile, String fileName, ObjectMetadata objectMetadata) throws IOException {
        try (InputStream inputStream = uploadFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead)); ////외부에 공개할 이미지이므로, 해당 파일에 public read 권한을 추가
        }
        return amazonS3Client.getUrl(bucket, fileName).toString(); //업로드를 한 후, 해당 URL을 DB에 저장할 수 있도록 컨트롤러로 URL 을 반환
    }


}
