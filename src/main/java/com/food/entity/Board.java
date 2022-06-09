package com.food.entity;

import com.food.dto.BoardFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "board")
@Getter @Setter
@ToString
public class Board {

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String content;

    private String username;

   private LocalDateTime regTime;


    public static Board createBoard(BoardFormDto boardFormDto) {
        Board board = new Board();
        board.setTitle(boardFormDto.getTitle());
        board.setContent(boardFormDto.getContent());
        board.setUsername(boardFormDto.getUsername());
        board.setRegTime(LocalDateTime.now());
        return board;
    }



}
