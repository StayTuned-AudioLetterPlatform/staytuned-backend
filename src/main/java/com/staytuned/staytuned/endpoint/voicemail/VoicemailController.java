package com.staytuned.staytuned.endpoint.voicemail;

import com.staytuned.staytuned.aws.S3UploadComponent;
import com.staytuned.staytuned.domain.User;
import com.staytuned.staytuned.endpoint.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class VoicemailController {

    private final VoicemailService voicemailService;

    @PostMapping("/v1/api/save")
    public Long  save(@RequestParam("data") MultipartFile  mediaFile, @RequestBody VoicemailRequestDto requestDto) throws IOException {
        return voicemailService.save(mediaFile, requestDto);
    }

    @PostMapping("/v1/api/delete")
    public Long  delete(@RequestParam("data") MultipartFile  mediaFile, @RequestBody VoicemailRequestDto requestDto) throws IOException {
        return voicemailService.save(mediaFile, requestDto);
    }

}
