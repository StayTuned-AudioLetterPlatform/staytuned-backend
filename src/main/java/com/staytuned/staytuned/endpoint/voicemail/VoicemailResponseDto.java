package com.staytuned.staytuned.endpoint.voicemail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class VoicemailResponseDto {
    private List<VoicemailDto> voicemailList;
    private String userName;
    private Long userCode;
    private boolean isUser;

    @JsonProperty("isUser")
    public boolean getIsUser() {
        return isUser;
    }

    @Builder
    public VoicemailResponseDto(List<VoicemailDto> voicemailList, String userName, Long userCode,final boolean isUser){
        this.voicemailList = voicemailList;
        this.userCode = userCode;
        this.isUser = isUser;
        this.userName = userName;
    }
}
