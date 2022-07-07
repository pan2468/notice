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
- 로그인 화면에서 Email, Password 파라미터 값을 SecurityConfig 에서 가로채서 인증이 성공이 되면 .defaultSuccessUrl("/board/list") 공지사항 페이지 화면으로 넘어가서 Spring Security 403 Forbidden Error가 발생되였습니다.
   


<details>
<summary><b>기존 코드</b></summary>
<div markdown="1">

~~~java
/**
 * 게시물 Top10 (기준: 댓글 수 + 좋아요 수)
 * @return 인기순 상위 10개 게시물
 */
public Page<PostResponseDto> listTopTen() {

    PageRequest pageRequest = PageRequest.of(0, 10, Sort.Direction.DESC, "rankPoint", "likeCnt");
    return postRepository.findAll(pageRequest).map(PostResponseDto::new);
}

/**
 * 게시물 필터 (Tag Name)
 * @param tagName 게시물 박스에서 클릭한 태그 이름
 * @param pageable 페이징 처리를 위한 객체
 * @return 해당 태그가 포함된 게시물 목록
 */
public Page<PostResponseDto> listFilteredByTagName(String tagName, Pageable pageable) {

    return postRepository.findAllByTagName(tagName, pageable).map(PostResponseDto::new);
}

// ... 게시물 필터 (Member) 생략 

/**
 * 게시물 필터 (Date)
 * @param createdDate 게시물 박스에서 클릭한 날짜
 * @return 해당 날짜에 등록된 게시물 목록
 */
public List<PostResponseDto> listFilteredByDate(String createdDate) {

    // 등록일 00시부터 24시까지
    LocalDateTime start = LocalDateTime.of(LocalDate.parse(createdDate), LocalTime.MIN);
    LocalDateTime end = LocalDateTime.of(LocalDate.parse(createdDate), LocalTime.MAX);

    return postRepository
                    .findAllByCreatedAtBetween(start, end)
                    .stream()
                    .map(PostResponseDto::new)
                    .collect(Collectors.toList());
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

