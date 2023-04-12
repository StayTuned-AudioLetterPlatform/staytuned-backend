package com.staytuned.staytuned.endpoint.user;

import com.staytuned.staytuned.endpoint.voicemail.VoicemailResponseDto;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class UserResponseDto {
    private Long code;
    private String name;
    private List<VoicemailResponseDto> voicemails;
}
