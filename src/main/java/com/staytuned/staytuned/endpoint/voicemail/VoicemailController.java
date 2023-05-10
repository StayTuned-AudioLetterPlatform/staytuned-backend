package com.staytuned.staytuned.endpoint.voicemail;

import com.staytuned.staytuned.aws.S3UploadComponent;
import com.staytuned.staytuned.domain.User;
import com.staytuned.staytuned.endpoint.user.UserService;
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

    @PostMapping("/save")
    public Long  save(@RequestBody VoicemailRequestDto requestDto)  {
        return voicemailService.save(requestDto);

    }

    @PostMapping("/file/upload")
    public String upload(@RequestParam("data") MultipartFile  file) throws  IOException{
        return s3UploadComponent.upload(file);
    }

    @GetMapping("List/{string}")
    public List<VoicemailResponseDto> getList(@PathVariable("string") String randomString){
        Long id =  Long.parseLong(randomString); // decoding 로직만들기
        return voicemailService.getListObjet(id);
    }

    @PostMapping("/delete")
    public void  delete(Long code) throws IOException {
         voicemailService.delete(code);
    }

}
