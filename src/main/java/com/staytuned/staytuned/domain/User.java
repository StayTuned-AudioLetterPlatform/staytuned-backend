package com.staytuned.staytuned.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "USER_TB")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Builder
    public User(String name, String email, String picture){
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public User update(String name, String picture){
        this.name = name;
        this.picture = picture;

        return this;
    }

}
