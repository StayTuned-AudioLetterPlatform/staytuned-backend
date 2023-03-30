package com.staytuned.staytuned.endpoint;

import com.staytuned.staytuned.domain.VoiceMailEntity;
import com.staytuned.staytuned.domain.VoiceMailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class VoiceMailService {

    private final VoiceMailRepository voiceMailRepository;

    public String getBlob(){
        log.info("service get}");
        VoiceMailEntity voiceMail = voiceMailRepository.findByCode(0L);
        return voiceMail.getBlobValue();
    }
    public void save(String blob){
        log.info("service save}");
        VoiceMailEntity voiceMail = new VoiceMailEntity(blob);
        voiceMailRepository.save(voiceMail);
    }

}
