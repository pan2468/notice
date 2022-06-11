package com.food.entity;

import com.food.dto.BoardFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "board")
@Getter @Setter
@ToString
public class Board {

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;                //No

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;           //제목

    private String content;         //내용

    private String username;        //작성자

   private LocalDateTime regTime;   //등록일자

   private int hit;                 // 조회수

    public static Board createBoard(BoardFormDto boardFormDto) {
        Board board = new Board();
        board.setTitle(boardFormDto.getTitle());
        board.setContent(boardFormDto.getContent());
        board.setUsername(boardFormDto.getUsername());
        board.setRegTime(LocalDateTime.now());
        return board;
    }

    public static Board updateBoard(BoardFormDto boardFormDto) {
        Board board = new Board();
        board.setTitle(boardFormDto.getTitle());
        board.setContent(boardFormDto.getContent());
        board.setUsername(boardFormDto.getUsername());
        board.setRegTime(LocalDateTime.now());
        return board;
    }





}
