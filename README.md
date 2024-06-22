spring security form 기반 인증 + oauth2 클라이언트 인증 프로젝트

## form 로그인 방식
### 시나리오

1. /test/aaa 접속 (baseUrl = localhost:8080)
2. /form/login 302 redirect 확인
3. 아직 인증된 사용자 없으므로, 로그인 시도 시 /form/login?error 이동 확인
4. 회원가입용 API 호출
   - endpoint: POST /users
   - content-type: application/json
   - request body: {"username":"kim", "password":"1234"}
5. 사용자 회원가입 확인
   - endpoint: GET /users
   - response : {"username":"kim", "password":"1234"}
6. 1번 /test/aaa 접속하여, 등록된 정보로 로그인 시도
   - /home?loginSuccess redirect 확인
   - 1번 /test/aaa 접속 시 인증 통과 확인


### 참고사항
- 추후 oauth2 login 적용도 위해, 기본 form 로그인 url을 "/form/login" 으로 설정
    - ss6 config 설정의 formLogin.loginPage().loginProcessingUrl().failureUrl() 설정 변경 필요
    - login.html form action="/form/login" 변경 필요
    - 로그인 View 제공용 Controller @GetMapping(url) 변경
- h2 인메모리 사용 (재시작마다 데이터, 스키마 리셋)
- 사용자 등록을 위해 /users는 ss6 config 설정에서 permitAll() 설정
