package com.staytuned.staytuned.endpoint.user;

import com.staytuned.staytuned.aws.S3UploadComponent;
import com.staytuned.staytuned.endpoint.voicemail.VoicemailService;
import com.staytuned.staytuned.security.jwt.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @DeleteMapping("/")
    public void delete(@LoginUser String email){
        userService.deleteUser(email);
    }

}
