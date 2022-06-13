package com.food.entity;

import com.food.dto.MemberFormDto;
import com.food.repository.BoardRepository;
import com.food.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class BoardTest {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto,passwordEncoder);
    }

    @Test
    @DisplayName("게시판 회원 엔티티 매핑 조회 테스트")
    public void findBoardAndMemberTest(){
        Member member = createMember();
        memberRepository.save(member);

        Board board = new Board();
        board.setMember(member);
        boardRepository.save(board);

        em.flush();
        em.clear();

        Board savedBoard = boardRepository.findById(board.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(savedBoard.getMember().getId(),member.getId());
    }
}