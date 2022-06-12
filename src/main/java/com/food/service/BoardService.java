package com.food.service;

import com.food.dto.BoardSearchDto;
import com.food.entity.Board;
import com.food.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }

    public Page<Board> findAll(Pageable pageable, BoardSearchDto boardSearchDto) {
        return boardRepository.findAll(pageable,boardSearchDto);
    }

    public Board getBoardDtl(Long id) {
        return boardRepository.getBoardDtl(id);
    }

    @Transactional
    public int updateView(Long id) {
        return boardRepository.updateView(id);
    }

    public void deleteBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        boardRepository.delete(board);
    }
}
