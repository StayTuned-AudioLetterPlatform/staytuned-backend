package com.staytuned.staytuned.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@Entity
@Table(name = "Voice_Mail_TB")
public class VoiceMailEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    @Column(nullable = false)
    private String voiceUrl;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private String iconType;

    @Builder
    public VoiceMailEntity(String voiceUrl, String writer, String iconType){
        this.voiceUrl = voiceUrl;
        this.iconType = iconType;
        this.writer = writer;
    }
}
