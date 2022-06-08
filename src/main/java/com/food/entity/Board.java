package com.food.entity;

import com.food.constant.Role;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "board")
@Getter
@Setter
@ToString
public class Board {

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    @NotNull
    @Size(min=2,max = 30, message = "제목은 2자이상 30자 이하입니다.")
    private String title;
    private String content;

    private LocalDate regTime;

    private int hit;

    private String searchBy;    //검색조건

    private String searchQuery = "";  //검색어 입력

    @Enumerated(EnumType.STRING)
    private Role role;


}
