package com.staytuned.staytuned.endpoint.voicemail;

import com.staytuned.staytuned.domain.VoiceMailEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
public class VoicemailDto {
    private Long code;
    private String writer;
    private String fileUrl;
    private String iconType;
    private LocalDateTime date;

    public VoicemailDto(VoiceMailEntity entity, boolean isUser){
        this.code = entity.getCode();
        this.iconType= entity.getIconType();
        if(isUser) this.fileUrl = entity.getFileUrl();
        this.date = entity.getCreatedDate();
        this.writer = entity.getWriter();
    }
}

