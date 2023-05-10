package com.staytuned.staytuned.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "USER_TB")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;
    @Enumerated(EnumType.STRING)
    @Column

    private Role role;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "targetUserFK", cascade = CascadeType.ALL)
    private List<VoiceMailEntity> voiceMails = new ArrayList<>();


    @Builder
    public User(String name, String email, String picture, Role role){
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public User update(String name, String picture){
        this.name = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey(){
        return  this.role.getKey();
    }

}
