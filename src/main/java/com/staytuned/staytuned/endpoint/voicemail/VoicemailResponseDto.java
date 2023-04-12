package com.staytuned.staytuned.endpoint.voicemail;

import com.staytuned.staytuned.domain.VoiceMailEntity;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
public class VoicemailResponseDto {
    private Long code;
    private String writer;
    private String voiceFileUrl;
    private String iconType;
    private LocalDateTime date;

    public VoicemailResponseDto (VoiceMailEntity entity){
        this.code = entity.getCode();
        this.iconType= entity.getIconType();
        this.voiceFileUrl = entity.getVoiceUrl();
        this.date = entity.getCreatedDate();
        this.writer = entity.getWriter();
    }
}

