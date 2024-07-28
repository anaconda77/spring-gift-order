package gift.controller.login;

import gift.auth.JwtService;
import gift.model.Member;
import gift.response.oauth2.OAuth2TokenResponse;
import gift.service.MemberService;
import gift.service.OAuth2LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OAuth2LoginController {

    private final OAuth2LoginService loginService;
    private final MemberService memberService;
    private final JwtService jwtService;

    public OAuth2LoginController(OAuth2LoginService loginService, MemberService memberService,
        JwtService jwtService) {
        this.loginService = loginService;
        this.memberService = memberService;
        this.jwtService = jwtService;
    }

    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @GetMapping("/kakao/login")
    public String login(Model model) {
        model.addAttribute("client_id", clientId);
        model.addAttribute("redirect_uri", redirectUri);
        return "kakaoLogin";
    }

    @GetMapping("/kakao/login/oauth2/code")
    @ResponseBody
    public ResponseEntity<Void> getToken(HttpServletRequest request,
        HttpServletResponse response) {
        loginService.checkRedirectUriParams(request);
        String code = request.getParameter("code");
        OAuth2TokenResponse dto = loginService.getToken(code);

        String kakaoId = loginService.getMemberInfo(dto.accessToken());
        Member member = memberService.loginByOAuth2(kakaoId + "@kakao.com");
        jwtService.addTokenInCookie(member, response);

        loginService.saveAccessToken(member.getId(), dto.accessToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
