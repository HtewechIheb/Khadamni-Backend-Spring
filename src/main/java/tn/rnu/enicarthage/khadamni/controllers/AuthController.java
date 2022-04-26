package tn.rnu.enicarthage.khadamni.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.rnu.enicarthage.khadamni.configuration.JWTConfig;
import tn.rnu.enicarthage.khadamni.dto.AuthResultDTO;
import tn.rnu.enicarthage.khadamni.dto.requests.LoginDTO;
import tn.rnu.enicarthage.khadamni.dto.requests.RegisterCandidateDTO;
import tn.rnu.enicarthage.khadamni.dto.requests.RegisterCompanyDTO;
import tn.rnu.enicarthage.khadamni.dto.responses.AuthDTO;
import tn.rnu.enicarthage.khadamni.services.interfaces.AuthService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JWTConfig jwtConfig;

    @PostMapping(path = "login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse httpResponse){
        AuthResultDTO authResultDTO = authService.login(loginDTO.getEmail(), loginDTO.getPassword());

        if(!authResultDTO.isSucceeded()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(authResultDTO.getError());
        }

        setRefreshTokenCookie(httpResponse, authResultDTO.getRefreshToken());

        AuthDTO authDTO = new AuthDTO();
        authDTO.setToken(authResultDTO.getToken());

        return ResponseEntity.ok(authDTO);
    }

    @PostMapping(path = "registercompany", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Object> registerCompany(@Valid @ModelAttribute RegisterCompanyDTO registerCompanyDTO, HttpServletResponse httpResponse){
        try {
            AuthResultDTO authResultDTO = authService.registerCompany(registerCompanyDTO.getEmail(), registerCompanyDTO.getPassword(), registerCompanyDTO.getCompany());

            if(!authResultDTO.isSucceeded()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(authResultDTO.getError());
            }

            setRefreshTokenCookie(httpResponse, authResultDTO.getRefreshToken());

            AuthDTO authDTO = new AuthDTO();
            authDTO.setToken(authResultDTO.getToken());

            return ResponseEntity.ok(authDTO);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Error Occurred While Registering Company!");
        }
    }

    @PostMapping(path = "registercandidate", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Object> registerCandidate(@Valid @ModelAttribute RegisterCandidateDTO registerCandidateDTO, HttpServletResponse httpResponse){
        try {
            AuthResultDTO authResultDTO = authService.registerCandidate(registerCandidateDTO.getEmail(), registerCandidateDTO.getPassword(), registerCandidateDTO.getCandidate());

            if(!authResultDTO.isSucceeded()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(authResultDTO.getError());
            }

            setRefreshTokenCookie(httpResponse, authResultDTO.getRefreshToken());

            AuthDTO authDTO = new AuthDTO();
            authDTO.setToken(authResultDTO.getToken());

            return ResponseEntity.ok(authDTO);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Error Occurred While Registering Candidate!");
        }
    }

    @PostMapping(path = "refreshtoken")
    public ResponseEntity<Object> refreshToken(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        Optional<Cookie> refreshToken = Arrays.stream(httpRequest.getCookies()).filter(cookie -> cookie.getName().equals("refresh_token")).findFirst();

        if(refreshToken.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Refresh Token Cookie Is Not Set!");
        }

        AuthResultDTO authResultDTO = authService.refreshToken(refreshToken.get().getValue());

        if(!authResultDTO.isSucceeded()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(authResultDTO.getError());
        }

        setRefreshTokenCookie(httpResponse, authResultDTO.getRefreshToken());

        AuthDTO authDTO = new AuthDTO();
        authDTO.setToken(authResultDTO.getToken());

        return ResponseEntity.ok(authDTO);
    }

    private void setRefreshTokenCookie(HttpServletResponse httpResponse, String refreshToken){
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(jwtConfig.getRefreshTokenLifeTime() * 24 * 60 * 60);

        httpResponse.addCookie(refreshTokenCookie);
    }
}
