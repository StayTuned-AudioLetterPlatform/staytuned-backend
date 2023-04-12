package com.staytuned.staytuned.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoiceMailRepository extends JpaRepository<VoiceMailEntity, Long> {
   VoiceMailEntity findByCode(Long code);
}
