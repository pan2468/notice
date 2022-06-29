package com.food.repository;


import com.food.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board,Long>, QuerydslPredicateExecutor<Board>,BoardRepositoryCustom {

    @Query("select b from Board b where b.id = :id")
    Board getBoardDtl(@Param("id") Long id);

    @Modifying
    @Query("update Board b set b.hit = b.hit + 1 where b.id = :id")
    int updateView(@Param("id") Long id);

}
