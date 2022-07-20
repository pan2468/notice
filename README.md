## 📌 회원 및 공지사항
+ 기능설명: 로그인, 회원가입, 목록, 글쓰기, 상세화면, 수정, 삭제, 페이징, 검색활용
### 제작기간, 참여인원
+ 제작기간: 2022.06.03 ~ 2022.06.13
+ 참여인원: 개인프로젝트
### 사용기술 (기술스택)
+ Java 11
+ SpringBoot 2.7.0
+ Spring Security
+ Spring Data JPA
+ QueryDSL 5.0.0
+ Gradle
+ MySQL
## 시퀀스 다이어그램


## 핵심 기능
- QueryDsl 활용하여 여러 검색 조건을 통해서 간단한 공지사항 조회 서비스 입니다.</br>
- JpaRepository 인터페이스 findAll() 메소드는 쿼리가 복잡해질 경우 QueryDsl 활용하여 JPQL 객체지향 쿼리를 통해서 원하는 조건문을 출력할 수 있습니다.
<details>
<summary><b>QueryDsl 설정</b></summary>
<div markdown="1">
	
~~~java
buildscript {
	ext {
		queryDslVersion = "5.0.0"
	}
}

plugins {
	id 'org.springframework.boot' version '2.7.0'
	id 'io.spring.dependency-management' version '1.0.11.RELEASE'
	id 'java'
	id "com.ewerk.gradle.plugins.querydsl" version "1.0.10"
}
~~~
- queryDslVersion = "5.0.0"
- id "com.ewerk.gradle.plugins.querydsl" version "1.0.10"	
	
~~~java
dependencies {
	implementation 'org.springframework.security:spring-security-test'
	implementation 'org.springframework.boot:spring-boot-starter-validation'
	implementation 'org.springframework.boot:spring-boot-starter-security'
	implementation 'org.springframework.boot:spring-boot-devtools'
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity5'
	compileOnly 'org.projectlombok:lombok'
	runtimeOnly 'com.h2database:h2-mvstore:1.4.199'
	runtimeOnly 'mysql:mysql-connector-java'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	implementation "com.querydsl:querydsl-jpa:${queryDslVersion}"
	implementation "com.querydsl:querydsl-apt:${queryDslVersion}"
	implementation 'org.springframework.boot:spring-boot-starter-oauth2-client'
}
~~~	
- implementation "com.querydsl:querydsl-jpa:${queryDslVersion}"
- implementation "com.querydsl:querydsl-apt:${queryDslVersion}"	
	
~~~java
// querydsl 추가 시작
def querydslDir = "$buildDir/generated/querydsl"

querydsl {
	jpa = true
	querydslSourcesDir = querydslDir
}

sourceSets {
	main.java.srcDir querydslDir
}

configurations {
	compileOnly {
		extendsFrom annotationProcessor
	}
	querydsl.extendsFrom compileClasspath
}

compileQuerydsl {
	options.annotationProcessorPath = configurations.querydsl
}
// querydsl 추가 끝
~~~
</div>
</details>

<details>
<summary><b>QueryDsl 사용</b></summary>
<div markdown="1">
	
~~~java	
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
~~~
- BoardRepositoryCustom 인터페이스를 상속을 받으면 JpaRepository 인터페이스를 사용할 수 있습니다.  

~~~java
import com.food.dto.BoardSearchDto;
import com.food.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<Board> findAll(Pageable pageable, BoardSearchDto boardSearchDto);
}
~~~
- findAll() 메소드 매개변수에 BoardSearchDto 추가합니다. 
~~~java

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

~~~
- findAll() 메소드 구현부에 QueryDsl 객체지향적인 쿼리를 통해서 여러 조건값을 넣어 조회할수 있습니다.	
	
</div>
</details>




## 핵심 트러블슈팅 경험 

- 가장 기억이 남았던 Error는 Spring Security 로그인 인증 이였습니다.  
- 로그인 화면에서 Email, Password 파라미터 값을 SecurityConfig 에서 가로채서 인증이 성공이 되면 .defaultSuccessUrl("/board/list") 공지사항 페이지 화면으로 넘어가지 않고  Spring Security 403 Forbidden Error가 발생되였습니다.
   
<details>
<summary><b>기존 코드</b></summary>
<div markdown="1">

~~~java

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
### Error 해결방법
- 밑에 하단에 input 태그에 csrf.parameterName, csrf.token 값을 넣어 추가했습니다. 
- 로그인 버튼 클릭 후 담아져 있던 세션 정보 값을 SecurityConfig 에서 인증 후 공지사항 페이지 화면으로 잘 넘어가는걸 알 수 있었습니다. 
</div>
</details>

### 👉 개인 프로젝트 블로그
+ <a href="https://pan2468.tistory.com/category/Toy%20Project/%ED%9A%8C%EC%9B%90%20%EB%B0%8F%20%EA%B3%B5%EC%A7%80%EC%82%AC%ED%95%AD">개인 프로젝트 설명</a>


