package com.staytuned.staytuned.endpoint.voicemail;

import com.staytuned.staytuned.aws.S3UploadComponent;
import com.staytuned.staytuned.domain.User;
import com.staytuned.staytuned.domain.VoiceMailEntity;
import com.staytuned.staytuned.domain.VoiceMailRepository;
import com.staytuned.staytuned.endpoint.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class VoicemailService {
    private final S3UploadComponent s3UploadComponent;
    private final UserService userService;
    private final VoiceMailRepository voiceMailRepository;


    @Transactional
    public Long save(VoicemailRequestDto requestDto){
        log.info("voice mail save service");
        VoiceMailEntity voiceMail =  VoiceMailEntity.builder()
                .fileUrl(requestDto.getFileUrl())
                .iconType(requestDto.getIconType())
                .writer(requestDto.getWriter())
                .targetUserFK(userService.fineEntity(requestDto.getTargetUserCd()))
                .build();

        return voiceMailRepository.save(voiceMail).getCode();
    }

    public VoicemailResponseDto getListObjet(Long code, boolean isUser){
        User user = userService.fineEntity(code);
        List<VoicemailDto> voicemailList = voiceMailRepository.findByTargetUserFK(user).stream()
                .map(VoicemailDto:: new)
                .collect(Collectors.toList());

        return VoicemailResponseDto.builder()
                .voicemailList(voicemailList)
                .userName(user.getName())
                .userCode(user.getCode())
                .isUser(isUser)
                .build();
    }

    public void delete(Long code){
         voiceMailRepository.deleteById(code);
    }

}
