package com.food.repository;

import com.food.dto.BoardSearchDto;
import com.food.entity.Board;
import com.food.entity.QBoard;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{
    private JPAQueryFactory queryFactory;

    public BoardRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){

        if(StringUtils.equals("T",searchBy)){
            return QBoard.board.title.like("%" + searchQuery + "%");
        }else if(StringUtils.equals("C",searchBy)){
            return QBoard.board.content.like("%" + searchQuery + "%");
        }else if(StringUtils.equals("TC",searchBy)){
            return QBoard.board.title.like("%" + searchQuery + "%")
                    .or(QBoard.board.content.like("%" + searchQuery + "%"));
        }
        return null;
    }

    @Override
    public Page<Board> findAll(Pageable pageable, BoardSearchDto boardSearchDto) {
        QueryResults<Board> results = queryFactory
                .selectFrom(QBoard.board)
                .where(
                        searchByLike(boardSearchDto.getSearchBy(),
                                boardSearchDto.getSearchQuery())
                )
                .orderBy(QBoard.board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Board> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
}
