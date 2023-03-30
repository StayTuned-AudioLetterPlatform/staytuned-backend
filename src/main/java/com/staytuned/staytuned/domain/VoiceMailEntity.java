package com.staytuned.staytuned.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "Voice_Mail_TB")
public class VoiceMailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    @Column(nullable = false)
    private String blobValue;

    @Builder
    public VoiceMailEntity(String blobValue){
        this.blobValue = blobValue;
    }
}
