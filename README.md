# 도서 대여 서비스 (BookRentalPjt)

### 프로젝트 소개
BookRentalPjt는 사용자와 관리자를 위한 도서 대여 서비스를 제공하는 웹 애플리케이션입니다. 사용자는 도서를 검색하고, 대출하며, 개인 책장에서 대출 내역을 관리할 수 있습니다. 관리자는 도서 및 회원을 관리하고, 희망 도서 요청을 처리할 수 있습니다.

### 주요 기능

* **사용자 기능**:
    * **회원 관리**: 회원가입, 로그인, 로그아웃, 비밀번호 찾기, 계정 정보 수정 기능.
    * **도서 검색**: 도서명으로 도서를 검색할 수 있습니다.
    * **도서 대출/반납**: 대출 가능한 도서를 대출할 수 있으며, 개인 책장(bookshelf)에서 대출 중인 도서 목록을 확인할 수 있습니다.
    * **대출 이력**: 대출 기록을 조회할 수 있습니다.
    * **희망 도서**: 원하는 도서를 요청하고, 요청한 도서의 처리 상태를 확인할 수 있습니다.

* **관리자 기능**:
    * **관리자 회원 관리**: 관리자 회원가입, 로그인, 로그아웃, 비밀번호 찾기, 계정 정보 수정 기능.
    * **도서 관리**: 도서 등록, 수정, 삭제 기능. 도서의 대출 가능 상태를 관리할 수 있습니다.
    * **회원 목록 관리**: 일반 사용자 및 관리자 목록을 조회하고, 관리자 승인 처리를 할 수 있습니다.
    * **대출 관리**: 현재 대출 중인 도서 목록을 확인하고, 도서 반납 처리를 할 수 있습니다.
    * **희망 도서 관리**: 사용자가 요청한 희망 도서 목록을 확인하고, 해당 도서를 입고 처리할 수 있습니다.

### 기술 스택
* **백엔드**: Java, Spring Boot, Spring Security
* **데이터베이스**: MariaDB
* **웹**: JSP, JSTL
* **프론트엔드**: HTML, CSS, JavaScript (jQuery)
* **빌드 도구**: Maven
* **기타**: 이메일 전송 (`spring-boot-starter-mail`), 파일 업로드 (`commons-fileupload`)

### 설치 및 실행 방법
1.  **프로젝트 클론**:
    ```bash
    git clone junhyeok020630/library_project.git
    cd library_project/BookRentalPjt
    ```
2.  **데이터베이스 설정**:
    * MariaDB 서버를 실행합니다.
    * `src/main/resources/application.properties` 파일을 열어 `bookrentalpjt` 데이터베이스를 생성하고, 데이터베이스 사용자 이름과 비밀번호를 환경에 맞게 수정합니다.
    * JPA 설정(`spring.jpa.hibernate.ddl-auto=update`)에 따라 애플리케이션 실행 시 테이블이 자동으로 생성됩니다.
3.  **이메일 설정**:
    * `src/main/resources/application.properties` 파일에 Gmail 계정 정보를 입력하여 비밀번호 찾기 기능을 활성화합니다.
        ```properties
        spring.mail.host=smtp.gmail.com
        spring.mail.port=587
        spring.mail.username=your-email@gmail.com
        spring.mail.password=your-password
        spring.mail.protocol=smtp
        spring.mail.properties.mail.smtp.auth=true
        spring.mail.properties.mail.smtp.starttls.enable=true
        ```
4.  **파일 업로드 경로 설정**:
    * `src/main/resources/application.properties` 파일에 도서 표지 이미지를 저장할 경로를 지정합니다.
        ```properties
        spring.web.resources.static-locations=file:///C:/library/upload/
        ```
    * `src/main/java/com/office/library/bookrentalpjt/book/admin/util/UploadFileService.java` 파일의 `uploadDir` 변수도 동일한 경로로 수정해야 합니다.
5.  **애플리케이션 실행**:
    * 터미널에서 다음 명령어를 실행하여 애플리케이션을 시작합니다.
        ```bash
        ./mvnw spring-boot:run
        ```
6.  **접속**:
    * 웹 브라우저에서 `http://localhost:8080/user` 또는 `http://localhost:8080/admin`으로 접속하여 서비스를 이용할 수 있습니다.

### 제작자
* 최준혁 (가천대학교 컴퓨터공학과 202136049)
