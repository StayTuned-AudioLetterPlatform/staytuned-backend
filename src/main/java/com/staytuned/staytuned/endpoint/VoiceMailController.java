package com.staytuned.staytuned.endpoint;

import com.staytuned.staytuned.aws.S3UploadComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class VoiceMailController {
    private final S3UploadComponent s3UploadComponent;
    private final VoiceMailService voiceMailService;

    @CrossOrigin
    @PostMapping("/v1/api/upload/{name}")
    public String addMedia(@PathVariable String name, @RequestParam("data") MultipartFile mediaFile) throws IOException, IOException {
        String url = s3UploadComponent.upload(mediaFile, name);
        log.info(mediaFile.getName());
        log.info(name);
        return  url;
    }

    @CrossOrigin
    @PostMapping("/v1/api/save")
    public void save(@RequestParam("data") String  mediaFile){
        log.info("controller save");
        voiceMailService.save(mediaFile);
    }

    @CrossOrigin
    @GetMapping("/v1/api/get")
    public String get(){
        log.info("controller save");
        return voiceMailService.getBlob();
    }
}
