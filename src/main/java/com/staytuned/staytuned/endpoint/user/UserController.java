package com.staytuned.staytuned.endpoint.user;

import com.staytuned.staytuned.security.jwt.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;


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
