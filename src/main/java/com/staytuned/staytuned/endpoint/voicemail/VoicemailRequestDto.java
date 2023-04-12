package com.staytuned.staytuned.endpoint.voicemail;

import com.staytuned.staytuned.domain.User;
import com.staytuned.staytuned.domain.VoiceMailEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VoicemailRequestDto {
    private String writer;
    private Long targetUserCd;
    private String iconType;

    public VoiceMailEntity toEntity(User user, String voiceUrl){
        return VoiceMailEntity.builder()
                .iconType(iconType)
                .writer(writer)
                .voiceUrl(voiceUrl)
                .build();
    }
}
