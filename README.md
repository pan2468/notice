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
- 로그인 화면에서 Email, Password 파라미터 값을 SecurityConfig 에서 가로채서 인증이 성공이 되면 .defaultSuccessUrl("/board/list") 공지사항 페이지 화면으로 넘어가지 않고  Spring Security 403 Forbidden Error가 발생되였습니다.
   


<details>
<summary><b>기존 코드</b></summary>
<div markdown="1">

~~~java
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
~~~
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
      layout:decorate="~{/layouts/layout}">

<head>
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
	integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
	<link href="layout1.css" th:href="@{/css/layout1.css}" rel="stylesheet">
</head>


<div class="container">
	<h3>로그인 페이지</h3>
	<a href="/members/project"><p>회원 가입 후 <br> 로그인 하시면 공지사항으로 이동</p></a>
	<form role="form" method="post" action="/members/login">
		<div class="mb-3">
			<input type="email" name="email" class="form-control" id="email" placeholder="이메일을 입력해주세요">
		</div>
		<div class="mb-3">
			<input type="password" name="password" id="password" class="form-control" placeholder="비밀번호 입력">
		</div>
		<p th:if="${loginErrorMsg}" class="error" th:text="${loginErrorMsg}"></p>
		<button class="btn btn-primary" id="login">로그인</button>
		<button type="button" class="btn btn-danger" onClick="location.href='/members/new'" id="login-sign">회원가입</button>
	</form>
</div>
</html>
~~~

</div>
</details>

<details>
<summary><b>개선된 코드</b></summary>
<div markdown="1">

~~~
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
      layout:decorate="~{/layouts/layout}">

<head>
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
	integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
	<link href="layout1.css" th:href="@{/css/layout1.css}" rel="stylesheet">
</head>


<div class="container">
	<h3>로그인 페이지</h3>
	<a href="/members/project"><p>회원 가입 후 <br> 로그인 하시면 공지사항으로 이동</p></a>
	<form role="form" method="post" action="/members/login">
		<div class="mb-3">
			<input type="email" name="email" class="form-control" id="email" placeholder="이메일을 입력해주세요">
		</div>
		<div class="mb-3">
			<input type="password" name="password" id="password" class="form-control" placeholder="비밀번호 입력">
		</div>
		<p th:if="${loginErrorMsg}" class="error" th:text="${loginErrorMsg}"></p>
		<button class="btn btn-primary" id="login">로그인</button>
		<button type="button" class="btn btn-danger" onClick="location.href='/members/new'" id="login-sign">회원가입</button>
		<input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}">
	</form>
</div>
</html>
~~~

</div>
</details>
 memberLoginForm.html 밑에 하단에 <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"> 코드를 추가했습니다. 
 로그인 버튼 클릭 후 담아져 있던 세션 정보 값을 SecurityConfig 에서 인증 후 공지사항 페이지 화면으로 잘 넘어가는걸 알 수 있었습니다. 

## 회고 / 느낀점
미작성

