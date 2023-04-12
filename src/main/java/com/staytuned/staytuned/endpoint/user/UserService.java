package com.staytuned.staytuned.endpoint.user;

import com.staytuned.staytuned.domain.User;
import com.staytuned.staytuned.domain.UserRepository;
import com.staytuned.staytuned.domain.VoiceMailEntity;
import com.staytuned.staytuned.domain.VoiceMailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public User fineEntity(Long userCd){
        return userRepository.findByCode(userCd).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. cd = " + userCd));
    }

}
