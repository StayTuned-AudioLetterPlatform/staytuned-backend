package com.staytuned.staytuned.endpoint.voicemail;

import com.staytuned.staytuned.aws.S3UploadComponent;
import com.staytuned.staytuned.security.jwt.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/voicemail")
public class VoicemailController {

    private final VoicemailService voicemailService;
    private final S3UploadComponent s3UploadComponent;
    private final UserStringDecoder userStringDecoder;

    @PostMapping("/save")
    public Long  save(@RequestBody VoicemailRequestDto requestDto)  {
        return voicemailService.save(requestDto);
    }

    @PostMapping("/file/upload")
    public String upload(@RequestParam("data") MultipartFile  file) throws  IOException{
        return s3UploadComponent.upload(file);
    }

    @GetMapping("/getList")
    public VoicemailResponseDto getList(@LoginUser(required = false) Long id) { // @LoginUser 로직 만들기.
        return voicemailService.getListObjet(id, true);
    }

    @GetMapping("List/{value}")
    public VoicemailResponseDto getList(@PathVariable("value") String value) throws Exception {
        Long id =  Long.parseLong(userStringDecoder.decoder(value));
        return voicemailService.getListObjet(id, false);
    }

    @PostMapping("/delete")
    public void  delete(Long code) throws IOException {
         voicemailService.delete(code);
    }

    //파일 다운로드 로직 작성하기

}
