package com.staytuned.staytuned.endpoint.user;

import com.staytuned.staytuned.aws.S3UploadComponent;
import com.staytuned.staytuned.domain.User;
import com.staytuned.staytuned.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final S3UploadComponent s3UploadComponent;

    private User fineEntity(Long userCd){
        return userRepository.findByCode(userCd).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. cd = " + userCd));
    }

    @Transactional
    public void deleteUser(String email){
        Optional<User> user = userRepository.findByEmail(email);
        s3UploadComponent.deleteFolder(email);
        userRepository.deleteByEmail(email);
    }

}
