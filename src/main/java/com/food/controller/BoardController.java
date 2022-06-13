package com.food.controller;

import com.food.dto.BoardFormDto;
import com.food.dto.BoardSearchDto;
import com.food.entity.Board;
import com.food.entity.Member;
import com.food.repository.MemberRepository;
import com.food.service.BoardService;
import com.food.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final MemberRepository memberRepository;

    /**
     * 게시글 목록화면 출력
     **/
    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 2) Pageable pageable
                        ,BoardSearchDto boardSearchDto,
                       @RequestParam(required = false,defaultValue = "") String searchText){
        //List<Board> boards = boardService.findAll();
        Page<Board> boards = boardService.findAll(pageable,boardSearchDto);
        int startPage = Math.max(1,boards.getPageable().getPageNumber() - 4);
        int endPage = Math.min(boards.getTotalPages(),boards.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        model.addAttribute("boards",boards);
        return "board/boardList";
    }

    /**
     * 게시글 등록화면
     **/
    @GetMapping("/write")
    public String write(Model model) throws Exception{

        List<Member> members = memberRepository.findAll();

        model.addAttribute("members",members);
        model.addAttribute("boardFormDto", new BoardFormDto());

        return "board/boardWrite";
    }

    /**
     * 게시글 등록
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
     * 게시글 상세화면 출력
     **/
    @GetMapping(value = "/detail")
    public String boardDtl(Model model, @RequestParam Long id){
        Board board = boardService.getBoardDtl(id);
        boardService.updateView(id);
        model.addAttribute("boardDetail",board);
        log.info("******** 상세화면 접속완료 *******");
        return "board/boardDetail";
    }

    /**
     * 게시글 수정
     **/
    @PostMapping("/update")
    public String updateBoard(@ModelAttribute("boardDetail") BoardFormDto boardFormDto){
        Board board = new Board();
        board.setId(boardFormDto.getId());
        board.setTitle(boardFormDto.getTitle());
        board.setContent(boardFormDto.getContent());
        board.setMenu(boardFormDto.getMenu());
        board.setUpdateTime(LocalDateTime.now());

        boardService.saveBoard(board);

        return "redirect:/board/list";
    }

    /**
     * 게시글 삭제
     **/
    @GetMapping("/delete")
    public String deleteBoard(@RequestParam Long id){

        boardService.deleteBoard(id);
        return "redirect:/board/list";
    }
}
