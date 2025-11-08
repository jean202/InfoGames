# InfoGames 프로젝트 현대화 마이그레이션 계획

## 📋 프로젝트 개요
- **원본**: JSP/Servlet + Oracle DB (레거시 구조)
- **목표**: Spring Boot + Thymeleaf + jQuery + MySQL + WAR 배포
- **목적**: 실무 환경과 동일한 아키텍처로 전환 (입사 준비)

---

## 🎯 마이그레이션 목표

### 기술 스택 변경
| 항목 | Before | After |
|------|--------|-------|
| Backend Framework | Servlet/JSP | Spring Boot 3.x |
| ORM | JDBC (직접 쿼리) | Spring Data JPA |
| Template Engine | JSP | Thymeleaf |
| Frontend Library | 없음 | jQuery 3.6+ |
| Database | Oracle | MySQL 8.x |
| Build Tool | Eclipse | Gradle |
| API Style | Page-based | MVC + REST API 혼합 |
| Authentication | Session | Spring Security + Session |
| Package Type | - | WAR |
| Deployment | Local Tomcat | AWS EC2 + Tomcat 9/10 |

---

## 📊 현재 프로젝트 구조 분석

### 주요 기능
1. **회원 관리**
    - 회원가입 (ID/닉네임 중복 체크)
    - 로그인/로그아웃
    - 아이디/비밀번호 찾기
    - 회원정보 수정/탈퇴

2. **게시판 시스템**
    - 자유게시판 (FreeBoard)
    - 팁게시판 (TipBoard)
    - 리뷰게시판 (ReviewBoard - 별점 포함)
    - CRUD 기능 (작성, 조회, 수정, 삭제)
    - 파일 업로드 (CKEditor)
    - 조회수 카운팅

### 데이터베이스 테이블
- `ACCOUNT`: 사용자 정보
- `WRITEID`: 게시글 공통 정보
- `FREEBOARD`, `TIPBOARD`, `REVIEWBOARD`: 게시판별 연결 테이블
- `BOARDID`: 게시판 구분
- `FILE`: 첨부파일

---

## 🗺️ 마이그레이션 로드맵

### Phase 1: 프로젝트 설정 및 환경 구축 (1일)
- [ ] Spring Boot 프로젝트 생성
    - [ ] Spring Initializr로 WAR 패키징 프로젝트 생성
    - [ ] 필요한 의존성 추가 (Web, JPA, Security, Thymeleaf, MySQL, Lombok, Validation)
    - [ ] build.gradle 설정 (WAR 패키징)
- [ ] MySQL 데이터베이스 설정
    - [ ] MySQL 8.x 설치 (로컬)
    - [ ] 데이터베이스 생성 (`infogames_db`)
    - [ ] application.yml 설정
- [ ] 프로젝트 구조 설정
    - [ ] 패키지 구조 생성 (domain 기반)
    - [ ] Thymeleaf 템플릿 디렉토리 설정
    - [ ] Static 리소스 디렉토리 설정

### Phase 2: Backend 개발 (3-4일)
- [ ] Entity 클래스 설계 및 생성
- [ ] Repository 계층
- [ ] Service 계층
- [ ] Controller 계층 (MVC + REST API)
- [ ] Security 설정
- [ ] 예외 처리 및 검증
- [ ] 파일 업로드 처리

### Phase 3: Frontend 개발 (Thymeleaf + jQuery) (3-4일)
- [ ] Thymeleaf 템플릿 구조
- [ ] 회원 관리 페이지
- [ ] 게시판 페이지
- [ ] JavaScript 개발 (jQuery)
- [ ] CSS 스타일링

### Phase 4: 통합 및 테스트 (2일)
- [ ] 기능 테스트
- [ ] 보안 테스트
- [ ] 성능 테스트
- [ ] 버그 수정

### Phase 5: WAR 패키징 및 Tomcat 배포 (2-3일)
- [ ] WAR 패키징 준비
- [ ] 로컬 Tomcat 배포 테스트
- [ ] AWS 환경 준비
- [ ] EC2 서버 설정
- [ ] 데이터베이스 마이그레이션
- [ ] 애플리케이션 배포

---

## 🔧 기술 스택 상세

### Backend
```yaml
Framework: Spring Boot 3.2.x (WAR packaging)
Language: Java 17
Database: MySQL 8.x
ORM: Spring Data JPA (Hibernate)
Security: Spring Security (Session 기반)
Build Tool: Gradle 8.x
Package Type: WAR
```

### Frontend (동일 프로젝트 내)
```yaml
Template Engine: Thymeleaf 3.x
JavaScript Library: jQuery 3.6+
CSS Framework: Bootstrap 5 (선택사항)
WYSIWYG Editor: CKEditor 5 또는 Summernote
AJAX: jQuery.ajax()
Validation: jQuery Validation Plugin
```

### DevOps
```yaml
Local WAS: Apache Tomcat 9/10
Cloud Provider: AWS
Compute: EC2 (t2.micro)
Database: RDS MySQL (db.t3.micro)
OS: Ubuntu 22.04 LTS
```

---

## 🗃️ 프로젝트 디렉토리 구조

```
infogames/
├── src/
│   ├── main/
│   │   ├── java/com/infogames/
│   │   │   ├── InfoGamesApplication.java
│   │   │   ├── domain/
│   │   │   │   ├── user/
│   │   │   │   │   ├── entity/User.java
│   │   │   │   │   ├── repository/UserRepository.java
│   │   │   │   │   ├── service/UserService.java
│   │   │   │   │   ├── controller/UserController.java (페이지)
│   │   │   │   │   ├── controller/UserApiController.java (AJAX)
│   │   │   │   │   └── dto/
│   │   │   │   ├── board/
│   │   │   │   │   ├── entity/Board.java, Post.java
│   │   │   │   │   ├── repository/
│   │   │   │   │   ├── service/PostService.java
│   │   │   │   │   ├── controller/
│   │   │   │   │   └── dto/
│   │   │   │   └── file/
│   │   │   └── global/
│   │   │       ├── config/
│   │   │       ├── security/
│   │   │       ├── exception/
│   │   │       └── util/
│   │   ├── resources/
│   │   │   ├── templates/
│   │   │   │   ├── fragments/ (header, footer)
│   │   │   │   ├── layouts/
│   │   │   │   ├── user/ (login, signup, mypage)
│   │   │   │   ├── board/ (list, write, view, edit)
│   │   │   │   ├── error/
│   │   │   │   └── index.html
│   │   │   ├── static/
│   │   │   │   ├── css/
│   │   │   │   ├── js/ (jQuery, common, user, board)
│   │   │   │   ├── images/
│   │   │   │   └── uploads/
│   │   │   ├── application.yml
│   │   │   ├── application-dev.yml
│   │   │   └── application-prod.yml
│   │   └── webapp/
│   │       └── WEB-INF/
│   └── test/
└── build.gradle
```

---

## 💡 주요 변경 사항

### 1. 아키텍처
- **Before**: JSP/Servlet (Front Controller 패턴)
- **After**: Spring Boot MVC + Thymeleaf + jQuery

### 2. 템플릿 엔진
- **Before**: JSP
- **After**: Thymeleaf (더 현대적, Spring Boot와 잘 통합)

### 3. 데이터베이스
- **Before**: Oracle (JDBC 직접 연결)
- **After**: MySQL (JPA/Hibernate)

### 4. 인증
- **Before**: 수동 세션 관리
- **After**: Spring Security (세션 기반)

### 5. 프론트엔드
- **Before**: 순수 JavaScript
- **After**: jQuery (DOM 조작, AJAX)

### 6. 빌드 및 배포
- **Before**: Eclipse, 수동 배포
- **After**: Gradle, WAR 패키징, Tomcat 배포

---

## ⚠️ 주요 설정 포인트

### 1. WAR 패키징 필수 설정
```gradle
// build.gradle
plugins {
    id 'war'
    id 'org.springframework.boot' version '3.2.0'
}

configurations {
    providedRuntime
}

dependencies {
    providedRuntime 'org.springframework.boot:spring-boot-starter-tomcat'
}
```

```java
// Application.java
@SpringBootApplication
public class InfoGamesApplication extends SpringBootServletInitializer {
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(InfoGamesApplication.class);
    }
    
    public static void main(String[] args) {
        SpringApplication.run(InfoGamesApplication.class, args);
    }
}
```

### 2. MySQL 한글 설정
```yaml
# application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/infogames_db?useUnicode=true&characterEncoding=utf8mb4&serverTimezone=Asia/Seoul
    username: infogames
    password: your_password
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
```

### 3. jQuery AJAX + CSRF
```javascript
// common.js - Spring Security CSRF 토큰 자동 추가
$(function() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});
```

---

## 📅 예상 일정
- **총 소요 기간**: 약 2주
- **1주차**: Backend 개발 + Frontend 기본
- **2주차**: Frontend 완성 + 테스트 + 배포

---
## 🌳 Git 브랜치 전략

### 브랜치 구조
- `master`: 기존 JSP/Servlet 프로젝트 (보존)
- **`refactor/v2.0`**: 리팩토링 작업 브랜치
    - `feature/project-setup`
    - `feature/entity-design`
    - `feature/repository-service`
    - `feature/controller-layer`

### 커밋 규칙
- feat: 새 기능
- fix: 버그 수정
- refactor: 코드 개선
- chore: 설정/빌드
- docs: 문서
- deploy: 배포

### 주요 마일스톤 커밋
- [x] Initial Spring Boot project setup
- [x] Complete Entity layer
- [x] Complete Service layer
- [x] Complete Controller layer
- [ ] Complete Frontend (Thymeleaf + jQuery)
- [ ] First WAR deployment
- [ ] AWS deployment success
---

## 🚀 시작 명령어

### 1. MySQL 데이터베이스 생성
```sql
CREATE DATABASE infogames_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'infogames'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON infogames_db.* TO 'infogames'@'localhost';
FLUSH PRIVILEGES;
```

### 2. 프로젝트 실행 (개발)
```bash
./gradlew bootRun
# http://localhost:8080
```

### 3. WAR 파일 빌드
```bash
./gradlew clean bootWar
# build/libs/infogames.war 생성
```

---

## 📝 진행 상황

### 완료된 작업
- [x] 기존 프로젝트 분석
- [x] 마이그레이션 계획 수립 (회사 환경에 맞춤)
- [x] Spring Boot WAR 프로젝트 생성
- [x] 엔티티 생성
- [x] 레포지토리 + 서비스 레이어
- [x] 컨트롤러 레이어

### 다음 작업
1. 프론트엔드 작업
2. 배포
3. MySQL 로컬 설치 및 데이터베이스 생성
4. 테스트 코드 작성
5. 코드 리뷰
6. 개선

