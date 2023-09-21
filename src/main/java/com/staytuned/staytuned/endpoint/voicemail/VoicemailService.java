package com.staytuned.staytuned.endpoint.voicemail;

import com.staytuned.staytuned.aws.S3UploadComponent;
import com.staytuned.staytuned.domain.User;
import com.staytuned.staytuned.domain.UserRepository;
import com.staytuned.staytuned.domain.VoiceMailEntity;
import com.staytuned.staytuned.domain.VoiceMailRepository;
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
    private final UserRepository userRepository;
    private final VoiceMailRepository voiceMailRepository; // chl

    @Transactional
    public Long save(VoicemailRequestDto requestDto){
        User targetUser = userRepository.findByCode(requestDto.getTargetUserCd())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. cd = " + requestDto.getTargetUserCd()));

        VoiceMailEntity voiceMail =  VoiceMailEntity.builder()
                .fileUrl(requestDto.getFileUrl())
                .iconType(requestDto.getIconType())
                .writer(requestDto.getWriter())
                .targetUserFK(targetUser)
                .build();

        return voiceMailRepository.save(voiceMail).getCode();
    }

    @Transactional(readOnly = true)
    public VoicemailResponseDto getListObjet(Long userCd, boolean isUser){
        User user =  userRepository.findByCode(userCd)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. cd = " + userCd));

        List<VoicemailDto> voicemailList = voiceMailRepository.findByTargetUserFK(user)
                .stream()
                .map(entity -> new VoicemailDto(entity, isUser))
                .collect(Collectors.toList());

        return VoicemailResponseDto.builder()
                .voicemailList(voicemailList)
                .userName(user.getName())
                .userCode(user.getCode())
                .isUser(isUser)
                .build();
    }

    public void delete(Long userCd){
         voiceMailRepository.deleteById(userCd);
    }
}
