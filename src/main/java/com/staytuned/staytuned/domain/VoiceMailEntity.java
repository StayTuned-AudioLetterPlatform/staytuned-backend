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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Target_User_FK")
    private User targetUserFK;

    @Column(nullable = false)
    private String fileUrl;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private String iconType;


    @Builder
    public VoiceMailEntity(String fileUrl, String writer, String iconType, User targetUserFK){
        this.fileUrl = fileUrl;
        this.iconType = iconType;
        this.writer = writer;
        this.targetUserFK = targetUserFK;
    }
}
