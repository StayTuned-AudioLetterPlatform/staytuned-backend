package com.staytuned.staytuned.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoiceMailRepository extends JpaRepository<VoiceMailEntity, Long> {
   VoiceMailEntity findByCode(Long code);
   List<VoiceMailEntity> findByTargetUserFK(User user);
}
