package com.food.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter @Setter
public class BoardFormDto {

    @NotEmpty(message = "제목은 필수 입력 값입니다.")
    private String title;

    @NotEmpty(message = "내용은 필수 입력 값입니다.")
    private String content;

    @NotBlank(message = "작성자 필수 입력 값 입니다.")
    private String username;

    private LocalDateTime regTime;

    //private int hit;

    //private String searchBy;    //검색조건

    //private String searchQuery = "";  //검색어 입력

//    @Enumerated(EnumType.STRING)
//    private Role role;

}
