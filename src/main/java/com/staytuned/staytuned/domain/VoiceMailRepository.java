package com.staytuned.staytuned.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VoiceMailRepository extends JpaRepository<VoiceMailEntity, Long> {
    VoiceMailEntity findByCode(Long code);
}
