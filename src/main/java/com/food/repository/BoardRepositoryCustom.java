package com.food.repository;

import com.food.dto.BoardSearchDto;
import com.food.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<Board> findAll(Pageable pageable, BoardSearchDto boardSearchDto);
}
