## 음식점 회원 및 공지사항
+ 기능설명: 로그인, 회원가입, 목록, 글쓰기, 상세화면, 수정, 삭제, 페이징, 검색활용
+ 프로젝트 개발과정: https://pan2468.tistory.com/category/Toy%20Project
### 제작기간, 참여인원
+ 제작기간: 2022.06.03 ~ 2022.06.13
+ 참여인원: 개인프로젝트
### 사용기술 (기술스택)
+ Java 11
+ SpringBoot
+ Spring MVC
+ Spring Security
+ Spring Data JPA
+ QueryDsl
+ Gradle
+ MySQL
## ERD 
<img src="https://user-images.githubusercontent.com/58936137/177688167-f66e89b9-3d7c-4398-992b-6df26bbe253e.png" width="600px" height="350px">

## 핵심 기능
핵심 기능 작성

## 핵심 트러블슈팅 경험 

- 가장 기억이 남았던 Error는 Spring Security 로그인 인증 이였습니다.  
- 로그인 화면에서 Email, Password 파라미터 값을 SecurityConfig 에서 가로채서 인증이 성공이 되면 .defaultSuccessUrl("/board/list") 공지사항 페이지 화면으로 넘어가면 Spring Security 403 Forbidden Error가 발생되였습니다.
   


<details>
<summary><b>기존 코드</b></summary>
<div markdown="1">

~~~java
   SecurityConfig.java
package com.food.config;

import com.food.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired MemberService memberService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        
        http
                .formLogin()
                .loginPage("/members/login")
                .defaultSuccessUrl("/board/list")
                .usernameParameter("email")
                .failureUrl("/members/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/members/login");

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }
}

~~~

</div>
</details>

- 이 때 카테고리(tag)로 게시물을 필터링 하는 경우,  
각 게시물은 최대 3개까지의 카테고리(tag)를 가질 수 있어 해당 카테고리를 포함하는 모든 게시물을 질의해야 했기 때문에  
- 아래 **개선된 코드**와 같이 QueryDSL을 사용하여 다소 복잡한 Query를 작성하면서도 페이징 처리를 할 수 있었습니다.

<details>
<summary><b>개선된 코드</b></summary>
<div markdown="1">

~~~java
/**
 * 게시물 필터 (Tag Name)
 */
@Override
public Page<Post> findAllByTagName(String tagName, Pageable pageable) {

    QueryResults<Post> results = queryFactory
            .selectFrom(post)
            .innerJoin(postTag)
                .on(post.idx.eq(postTag.post.idx))
            .innerJoin(tag)
                .on(tag.idx.eq(postTag.tag.idx))
            .where(tag.name.eq(tagName))
            .orderBy(post.idx.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
            .fetchResults();

    return new PageImpl<>(results.getResults(), pageable, results.getTotal());
}
~~~

</div>
</details>

## 회고 / 느낀점
미작성

