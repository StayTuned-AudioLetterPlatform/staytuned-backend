package com.staytuned.staytuned.endpoint.voicemail;

import com.staytuned.staytuned.domain.User;
import com.staytuned.staytuned.domain.VoiceMailEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class VoicemailRequestDto {
    private String writer;
    private Long targetUserCd;
    private String iconType;
    private String fileUrl;

    @Builder
    public  VoicemailRequestDto(String writer,String iconType, Long targetUserCd, String fileUrl ){
        this.iconType =iconType;
        this.writer = writer;
        this.targetUserCd =targetUserCd;
        this.fileUrl = fileUrl;
    }


    public VoiceMailEntity toEntity(User user){
        return VoiceMailEntity.builder()
                .iconType(iconType)
                .writer(writer)
                .fileUrl(fileUrl)
                .build();
    }
}
