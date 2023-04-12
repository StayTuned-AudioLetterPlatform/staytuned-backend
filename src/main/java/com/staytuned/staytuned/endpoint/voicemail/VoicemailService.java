package com.staytuned.staytuned.endpoint.voicemail;

import com.staytuned.staytuned.aws.S3UploadComponent;
import com.staytuned.staytuned.domain.User;
import com.staytuned.staytuned.domain.UserRepository;
import com.staytuned.staytuned.domain.VoiceMailEntity;
import com.staytuned.staytuned.domain.VoiceMailRepository;
import com.staytuned.staytuned.endpoint.user.UserResponseDto;
import com.staytuned.staytuned.endpoint.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VoicemailService {
    private final S3UploadComponent s3UploadComponent;
    private final UserService userService;
    private final VoiceMailRepository voiceMailRepository;


    public Long save(MultipartFile multipartFile, VoicemailRequestDto requestDto) throws IOException {
        VoiceMailEntity voiceMail =  VoiceMailEntity.builder()
                .voiceUrl(getVoiceUrl(multipartFile, requestDto.getWriter()))
                .iconType(requestDto.getIconType())
                .build();

        return voiceMailRepository.save(voiceMail).getCode();

    }
    private String getVoiceUrl(MultipartFile multipartFile, String name) throws IOException {
        return s3UploadComponent.upload(multipartFile, name);
    }
    public void delete(Long code){
         voiceMailRepository.deleteById(code);

    }

}
