# SPRING_BOOT

2026년 2학기 자바웹프로그래밍(2) · **중간 점검 (2~7주차)** · 20221400 이민주

스프링 부트를 이용해 개인 포트폴리오 웹사이트를 제작하는 프로젝트입니다.


| 구분 | 사용 기술 |
|---|---|
| 언어 · 프레임워크 | Java 25, Spring Boot 4.1, Spring Security 7 |
| 화면 | Thymeleaf, Bootstrap 5 (TemplateMo First Portfolio) |
| 데이터베이스 | MySQL 8.4 (Docker), Spring Data JPA (Hibernate) |
| 도구 | VS Code, Postman, GitHub |

---

## 주차별 진행 내용 (중간 점검 범위 : 2~7주차)

### 2주차 - 스프링 부트 개발환경, 테스트 완료
[index.html : 실행/수정 완료](https://github.com/minju806/SPRING_BOOT_20221400/blob/main/demo/src/main/resources/templates/index.html)


### 3주차 - 포트폴리오 작성하기 완료
[index.html : 실행/수정 완료](https://github.com/minju806/SPRING_BOOT_20221400/blob/main/demo/src/main/resources/templates/index.html)


### 4주차 - 데이터베이스 수정 완료
[testdb.html : 실행/수정 완료](https://github.com/minju806/SPRING_BOOT_20221400/blob/main/demo/src/main/resources/templates/testdb.html)


### 5주차 - 로그인/로그아웃, 암호화 완료
[SecurityConfig.java : 실행/수정 완료](https://github.com/minju806/SPRING_BOOT_20221400/blob/main/demo/src/main/java/com/example/demo/config/SecurityConfig.java)


### 6주차 - 권한(ROLE) 접근 제어, 관리자 페이지 분리 완료
[AdminController.java : 실행/수정 완료](https://github.com/minju806/SPRING_BOOT_20221400/blob/main/demo/src/main/java/com/example/demo/controller/AdminController.java)


### 7주차 - 회원 관리 REST API(JSON) 완료
[AdminApiController.java : 실행/수정 완료](https://github.com/minju806/SPRING_BOOT_20221400/blob/main/demo/src/main/java/com/example/demo/controller/AdminApiController.java)


---

## 시험 대비 핵심 정리

**스프링 부트 기본 (2주차)**
- 스프링 프레임워크(2004) : XML 설정이 많아 진입 장벽 높음 → 스프링 부트(2014) : 자동 설정 · 내장 톰캣 · 자동 리로드
- 국내 공공기관은 스프링 기반 **전자정부 표준 프레임워크(eGov)** 사용
- 톰캣 = 내장 **WAS**, 스프링 컨테이너 = 자바 객체(빈) 생성 · 관리, **의존성 주입(DI, `@Autowired`)**
- 요청 흐름 : URL 요청 → **DispatcherServlet**(프론트 컨트롤러) → Controller → Model → Thymeleaf 가 HTML 렌더링 → 응답
- **MVC** : Model(데이터) · View(화면, Thymeleaf) · Controller(흐름 제어)
- JVM : 바이트코드 실행 → 플랫폼 독립성, GC 로 메모리 자동 관리

**Thymeleaf 문법 (매주 업데이트)**
| 문법 | 의미 | 주차 |
|---|---|---|
| `xmlns:th="http://www.thymeleaf.org"` | 선언 | 2 |
| `th:text="${data}"` | 변수 출력 | 2 |
| `th:href="@{/css/...}"`, `th:src` | URL (컨텍스트 경로 자동 반영) | 3 |
| `th:each="user : ${users}"` | 반복 | 4 |
| `th:action="@{/login}"` | 폼 전송 + CSRF 토큰 자동 삽입 | 5 |
| `sec:authorize="isAuthenticated()"` | 로그인 상태에 따라 보이기 / 숨기기 | 5 |
| `sec:authentication="name"` | 로그인 아이디 출력 | 5 |
| `sec:authorize="hasRole('ADMIN')"` | 권한별 메뉴 | 6 |

**JPA (4주차)**
- ORM : 객체(클래스) ↔ 테이블 매핑, SQL 을 직접 쓰지 않고 메서드로 DB 제어
- JPA(표준) → **Hibernate**(대표 구현체), 엔티티 매니저가 영속성 컨텍스트 관리
- `@Entity`, `@Table`, `@Id`, `@GeneratedValue(IDENTITY)`, `@Column`, Lombok `@Data`
- 메서드 이름 규칙으로 SQL 자동 생성 : `findByUsername`, `existsByUsername`, `findBy…Containing…Or…Containing`
- `save()` : id 없으면 INSERT, 있으면 UPDATE

**보안 트렌드 (5~6주차)**
- OWASP Top 10 (2025) : **A01 접근 제어 실패**, A07 인증 실패
- 접근 제어 실패 예 : `/admin` 주소 노출, 번호만 바꿔 남의 정보 조회(**IDOR**), 요청 조작으로 **권한 상승**
- **화면에서 숨기는 것은 보안이 아니다** → 요청마다 서버에서 권한 검사
- 권한 모델 : **RBAC**(역할 기반, 실습) / ABAC(속성 기반), **최소 권한 원칙**
- 로그인 방식 : 세션(실습, `JSESSIONID`) → 토큰(JWT) → 소셜 로그인(OAuth2) → 패스키
- Spring Security 는 **필터 체인** : 요청이 컨트롤러에 도착하기 전에 검사 (CsrfFilter, UsernamePasswordAuthenticationFilter, LogoutFilter, AuthorizationFilter …)
- CSRF : 다른 사이트가 내 쿠키로 몰래 POST 하는 공격 → 폼에 토큰
- Basic 인증 = `아이디:비밀번호` 를 **Base64** 로 변환 (암호화 아님 → 실서비스는 HTTPS 필수)

**접근 제어 3단계 (6주차)**
| 단계 | 방법 | 역할 |
|---|---|---|
| ① 화면 | `sec:authorize` | 보이기 / 숨기기만 (보안 X) |
| ② URL | `.requestMatchers("/admin/**").hasRole("ADMIN")` | 1차 잠금, 컨트롤러 도착 전 차단 |
| ③ 메서드 | `@PreAuthorize("hasRole('ADMIN')")` + `@EnableMethodSecurity` | 2차 잠금, 서비스 메서드 실행 직전 차단 |

- ②가 있는데 ③도 쓰는 이유 : URL 패턴 오타, 규칙 순서 실수, 같은 기능을 새 URL(API)에서 호출하는 경우 대비 → **이중 잠금**


**웹 구조 (7주차)**
- SSR (지금까지) : 서버가 Thymeleaf 로 HTML 완성 → 브라우저는 출력만
- CSR (7주차) : 서버는 JSON 데이터만, 화면은 JavaScript(React · Vue 등)가 그림
- 계층 : Controller(요청/응답) → Service(로직) → Repository(DB 접근) → Entity(테이블)

**인증 · 인가**
| 구분 | 인증 (Authentication) | 인가 (Authorization) |
|---|---|---|
| 질문 | 너 누구야? | 너 이거 해도 돼? |
| 예 | 로그인 | ROLE 확인 |
| 실패 | 401 (API) / 로그인 화면 302 (화면) | 403 |

- `hasRole("ADMIN")` 은 내부적으로 `ROLE_ADMIN` 과 비교 (직접 `ROLE_` 붙이면 오류)
- URL 규칙은 **위에서부터 순서대로**, `anyRequest()` 는 항상 마지막
- 권한이 바뀐 회원은 **다시 로그인**해야 새 권한이 적용됨

**비밀번호 보안**
- 암호화(복호화 가능) vs 해시(되돌릴 수 없음) → 비밀번호는 해시
- BCrypt : 매번 랜덤 솔트 + 일부러 느린 연산 → 같은 비밀번호도 다른 값
- 비교는 `matches()` (시큐리티가 자동 처리)

**DTO 3종 비교**
| 구분 | Member (엔티티) | MemberForm (요청) | MemberResponse (응답) |
|---|---|---|---|
| 방향 | DB ↔ 서버 | 사용자 → 서버 | 서버 → 사용자 |
| 빠진 값 | 없음 | role | password |
| 막는 공격 | — | 권한 조작 | 정보 노출 |

**REST · 상태 코드**
| 할 일 | 메서드 · 주소 | 성공 코드 |
|---|---|---|
| 목록 | GET /api/admin/members | 200 |
| 1명 | GET /api/admin/members/{id} | 200 (없으면 404) |
| 등록 | POST /api/admin/members | 201 + Location |
| 권한 변경 | PATCH /api/admin/members/{id}/role | 200 |
| 삭제 | DELETE /api/admin/members/{id} | 204 |

- 400 : 요청 내용 잘못됨 / 401 : 누구인지 모름 / 403 : 권한 없음 / 404 : 대상 없음
- `@RequestBody` : JSON 본문 → 자바 객체, `@PathVariable` : 주소 속 값, `@RequestParam` : `?key=value`
- CSRF : 화면 폼은 `th:action` 으로 토큰 자동 삽입, JSON API(`/api/**`)는 검사 제외

---

## 문제 해결 기록

**1. 서버가 안 뜸 : `Communications link failure` / `Connection refused`**
- 원인 : MySQL 을 Docker 로 설치했는데 Docker Desktop 이 꺼져 있어서 DB 서버가 없었음
- 해결 : Docker Desktop 실행 → `docker start mysql` → `HikariPool-1 - Start completed` 확인
- 배운 점 : 로그의 맨 아래 `Caused by` 가 진짜 원인. `Unable to determine Dialect` 는 DB 연결 실패의 결과일 뿐

**2. `SecurityConfig` 컴파일 오류**
- 원인 : `.logout(...)` 끝의 `;` 를 지우지 않고 `.rememberMe(...)` 를 이어 붙임
- 해결 : 메서드 체인은 마지막 설정에만 `;`

**3. 파일을 넣었는데 화면이 안 나옴**
- 원인 : 같은 이름 파일이 있어서 `members-api_1.html` 처럼 이름이 자동 변경됨
- 해결 : 컨트롤러가 반환하는 이름과 템플릿 파일 이름을 정확히 일치시킴

**4. Postman 으로 `/api/me` 요청 시 JSON 대신 로그인 화면 HTML**
- 원인 : Postman 은 브라우저와 달리 세션 쿠키가 없어 로그인 안 한 요청으로 처리됨
- 해결 : `httpBasic()` 추가 + `/api/` 요청은 401 로 응답하도록 설정

**5. 자주 나는 시큐리티 오류 (수업 자료 정리)**
| 증상 | 원인 → 해결 |
|---|---|
| 메인 디자인 깨짐 | `/css/**` 등 `permitAll` 누락 |
| 로그인 화면 무한 이동 | `/login` `permitAll` 누락 |
| 가입 · 로그인 시 403 | `action=` → `th:action` (CSRF 토큰) |
| `ROLE_ADMIN should not start with ROLE_` | `hasRole("ROLE_ADMIN")` → `hasRole("ADMIN")` |
| `Can't configure requestMatchers after anyRequest` | `anyRequest()` 를 맨 아래로 |
| USER 도 `@PreAuthorize` 메서드 실행됨 | `@EnableMethodSecurity` 누락 |
| SQL 로 ADMIN 줬는데 메뉴 안 보임 | 다시 로그인 (세션 권한) |

**6. DB 비밀번호 GitHub 노출 위험**
- 해결 : `application-secret.properties` 로 분리 + `.gitignore` 등록, 커밋 전 변경 목록 확인 습관

---

## 느낀 점

4학년으로 졸업 작품(지원사업 공고를 분석하는 AI 플랫폼, CLUE)을 진행하고 전시하는 과정에서 이 수업을 듣다 보니, 매주 배운 내용이 졸작에서 실제로 고민하던 부분과 계속 겹쳤습니다. 특히 5~7주차의 로그인 · 권한 · REST API 는 졸작 서비스에도 그대로 필요한 기능이라, "일단 돌아가게"가 아니라 "왜 이렇게 막아야 하는지"를 이해하고 넘어갈 수 있었던 점이 가장 컸습니다.

가장 기억에 남는 건 6주차 보안 구멍 실험입니다. 관리자 메뉴만 숨겨 두면 일반 회원도 주소를 직접 입력해 자기 권한을 ADMIN 으로 올릴 수 있다는 걸 직접 해 보고 나서, 화면에서 숨기는 것은 보안이 아니라는 말이 잘 와닿았습니다. 졸업작품 API 를 수정할 때도 URL 잠금과 서비스 메서드 잠금을 같이 두고, 응답에는 필요한 값만 내보내는 원칙을 적용하려고 합니다.

또 하나는 문제를 푸는 습관입니다. 4주차 실습 때 서버가 계속 안 떠서 당황했는데, 로그 맨 아래 `Caused by` 를 따라가 보니 원인은 코드가 아니라 꺼져 있던 Docker 였습니다. 에러 메시지를 위에서부터 겁먹고 보는 대신, 진짜 원인을 끝까지 따라가는 게 중요하다는 걸 배웠습니다.

매주 GitHub 에 정리해 온 이 내용들을 졸작 개발 뿐만 아니라 앞으로 개인 활동 때도 참고 자료로 계속 꺼내 보면 좋을 것 같습니다.