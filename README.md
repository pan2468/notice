# Spring Boot JPA 개인 프로젝트 
+ 개인 프로젝트 로그인, 회원가입, 게시판(공지사항) Java, SpringBoot, JPA, Gradle 제작으로 개발 구축

## :+1: Skills
+ Front-End: HTML5, CSS, JS, Thymeleaf
+ Back-End: Java, SpringBoot, JPA
+ BuildTool: Gradle
+ RDBMS: Mysql


## 💻 IDE & Tool 
+ Intelij
+ VS Code
</br>

## 🖥 프로젝트 사용 메뉴얼

### 회원가입
+ 기능 설명: 이름, 이메일, 비밀번호, 주소 입력후 회원가입 버튼 클릭
+ JpaRepository 인터페이스에서 save() 메소드를 활용하여 INSERT 등록
<img src = "https://user-images.githubusercontent.com/58936137/175238260-eddfd4bb-d6c2-49e5-915d-e69d6e013fd7.png" width="320px" height="320px">

### 로그인 페이지
+ 기능 설명: 이메일, 비밀번호 입력후 로그인 버튼 클릭후 검증하여 공지사항으로 이동 
+ SpringSecurity 보안프레임워크 활용하여 Email, Password 파라미터 값을 받아 검증하여 redirect 이동
<img src = "https://user-images.githubusercontent.com/58936137/175240393-73f1530a-a641-4ed9-851a-dfc13d64dc98.png" width="320px" height="320px">

### 게시판(글쓰기)
+ 기능 설명: 제목, 내용, 메뉴 입력후 등록버튼 클릭
+ JpaRepository 인터페이스 save() 메소드를 활용하여 INSERT 등록
<img src = "https://user-images.githubusercontent.com/58936137/175243599-6ab4f5b4-f8b4-47fe-98ed-8d27aa29308a.png" width="320px" height="320px">

### 게시판(공지사항)
+ 기능 설명: No, 제목, 내용, 작성자, 작성일, 조회수 목록 출력
+ JpaRepository 인터페이스에서 findAll() 메소드 활용하여 SELECT 목록 출력
<img src = "https://user-images.githubusercontent.com/58936137/175242728-865c4062-dfe9-4705-bca4-77a3e447dd64.png" width="650px" height="320px">

### 게시판(상세화면)
+ 공지사항 목록 제목 클릭후 -> 상세 목록 정보 출력
+ 목록: 목록 버튼 클릭후 redirect 공지사항으로 이동
+ 수정: 제목, 내용 부분 수정하여 수정버튼클릭 -> JpaRepository 인터페이스 save() 메소드 활용하여 INSERT 등록
+ 삭제: 삭제 버튼클릭 -> JpaRepository 인터페이스 delete() 메소드 활용   
<img src = "https://user-images.githubusercontent.com/58936137/175244692-92afcc29-2193-416e-a2e9-b550e8843229.png" width="320px" height="420px">


## 📆 기간
2022-06-03 ~ 2022-06-13 

