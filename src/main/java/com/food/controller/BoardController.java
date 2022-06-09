package com.food.controller;

import com.food.dto.BoardFormDto;
import com.food.entity.Board;
import com.food.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시판 목록화면 출력
     **/
    @GetMapping("/list")
    public String list(Model model){
        List<Board> boards = boardService.findAll();
        model.addAttribute("boards",boards);
        return "board/boardList";
    }


    /**
     * 게시판 등록화면
     **/
    @GetMapping("/write")
    public String write(Model model){
        model.addAttribute("boardFormDto", new BoardFormDto());
        return "board/boardWrite";
    }



    /**
     * 게시판 등록
     **/
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

    /**
     * 게시판 상세화면 출력
     **/
    @GetMapping(value = "/detail")
    public String boardDtl(Model model, @RequestParam Long id){
        Board board = boardService.getBoardDtl(id);
        boardService.updateView(id);
        model.addAttribute("boardDetail",board);
        log.info("******** 상세화면 접속완료 *******");
        return "board/boardDetail";
    }


}
