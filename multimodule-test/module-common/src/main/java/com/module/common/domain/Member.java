package com.module.common.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private String info;


    @Builder
    public Member(String password, String name, String info) {
        this.password = password;
        this.name = name;
        this.info = info;
    }
}
