package com.food.controller;

import com.food.dto.BoardFormDto;
import com.food.entity.Board;
import com.food.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public String list(Model model){
        List<Board> boards = boardService.findAll();
        model.addAttribute("boards",boards);
        return "board/boardList";
    }

    @GetMapping("/write")
    public String write(Model model){
        model.addAttribute("boardFormDto", new BoardFormDto());
        return "board/boardWrite";
    }


    @PostMapping("/write")
    public String reg(@Valid BoardFormDto boardFormDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "board/boardWrite";
        }

        try{
            Board board = Board.createBoard(boardFormDto);
            boardService.saveBoard(board);
        }catch (IllegalStateException e){
            model.addAttribute("errorMessage",e.getMessage());
            return "board/boardWrite";
        }
        return "redirect:/board/list";
    }


}
