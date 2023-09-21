package com.staytuned.staytuned.endpoint.voicemail;

import com.staytuned.staytuned.aws.S3UploadComponent;
import com.staytuned.staytuned.endpoint.user.UserController;
import com.staytuned.staytuned.endpoint.user.UserService;
import com.staytuned.staytuned.security.jwt.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;



@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/voicemail")
public class VoicemailController {

    private final VoicemailService voicemailService;

    private final UserService userService;

    private final S3UploadComponent s3UploadComponent;

    private final AES256 aes256;

    @PostMapping("/save")
    public Long save(@RequestBody VoicemailRequestDto requestDto) {
        return voicemailService.save(requestDto);
    }

    @PostMapping("/file/upload/{userCd}")
    public String upload(@RequestParam("data") MultipartFile file, @PathVariable Long userCd) throws IOException {
        return s3UploadComponent.upload(file, userCd);
    }

    @PostMapping("/file/download/{fileUrl}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileUrl, @LoginUser String email) throws IOException {
        InputStreamResource resource =  s3UploadComponent.downloadFile(fileUrl);
        String fileName = email + "/" + resource.getFilename();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @GetMapping("/my")
    public VoicemailResponseDto getUserList(@LoginUser Long email) {
        VoicemailResponseDto temp = voicemailService.getListObjet(email, true);
        log.info(String.valueOf(temp.getIsUser()));
        return temp;
    }

    @GetMapping("/user")
    public VoicemailResponseDto getTargetList(@RequestParam("userID") String value) throws Exception {
        Long id = Long.parseLong(aes256.decoder(value));
        return voicemailService.getListObjet(id, false);
    }

    @DeleteMapping("/")
    public void delete(Long code) throws IOException {
        voicemailService.delete(code);
    }

}
