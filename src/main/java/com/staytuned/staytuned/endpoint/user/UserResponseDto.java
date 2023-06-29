package com.staytuned.staytuned.endpoint.user;

import com.staytuned.staytuned.endpoint.voicemail.VoicemailDto;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class UserResponseDto {
    private Long code;
    private String name;
    private List<VoicemailDto> voicemails;
}
