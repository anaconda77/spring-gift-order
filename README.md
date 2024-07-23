# spring-gift-order

## 카카오 로그인 후, 토큰 발급 요청 후 토큰을 받아오는 API를 구현한다.

### 첫번째 작업: 로그인 html 작성
- 해당 페이지에서 클라이언트가 카카오 로그인 버튼을 클릭
- 우리 서버가 로그인 요청을 카카오 서버에게 전송
- GET /oauth/authorize
- 클라이언트는 카카오계정 로그인 요청 화면을 반환받게 됨
#### 두번째 작업: 클라이언트가 카카오 로그인 요청
- 우리 서버를 거치지 않고 카카오 서버 내에서 로그인 검증
- 동의 항목 출력 및 클라이언트의 동의 항목 체크
- 기존에 설정해둔 redirect-uri로 인가코드를 우리 서버에 응답 (상태 코드 302)
#### 세번째 작업: 카카오 서버로부터 얻어온 인가코드와 클라이언트 정보 등을 기반으로 카카오 서버에 토큰 발급을 요청하여 토큰 받아오기
- redirect_uri에서 requestMapping
  - redirect_uri: http://localhost:8080/kakao/login/oauth2/code
  - 카카오 서버로부터 오는 것이기 때문에, 별도의 행위 메서드 표기 없이 requestMapping으로 처리
- POST /oauth/token
- 최종적으로 토큰 반환 (상태 코드 200)
