package tn.rnu.enicarthage.khadamni.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import tn.rnu.enicarthage.khadamni.configuration.JWTConfig;
import tn.rnu.enicarthage.khadamni.dto.AuthResultDTO;
import tn.rnu.enicarthage.khadamni.models.AppUser;
import tn.rnu.enicarthage.khadamni.models.Candidate;
import tn.rnu.enicarthage.khadamni.models.Company;
import tn.rnu.enicarthage.khadamni.models.RefreshToken;
import tn.rnu.enicarthage.khadamni.repositories.RefreshTokenRepository;
import tn.rnu.enicarthage.khadamni.services.interfaces.AuthService;
import tn.rnu.enicarthage.khadamni.services.interfaces.CandidateService;
import tn.rnu.enicarthage.khadamni.services.interfaces.CompanyService;
import tn.rnu.enicarthage.khadamni.services.interfaces.UserService;
import tn.rnu.enicarthage.khadamni.shared.enumerations.UserType;

import javax.validation.constraints.AssertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private JWTConfig jwtConfig;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private CompanyService companyService;

    @Mock
    private CandidateService candidateService;

    @InjectMocks
    private AuthService authService = new AuthServiceImpl();

    // TODO: Include token validation in test cases (Refresh + Access)

    private void setUpJWTConfig() {
        Mockito.when(jwtConfig.getSecret()).thenReturn("SECRET");
        Mockito.when(jwtConfig.getTokenLifeTime()).thenReturn(Short.parseShort("1"));
        Mockito.when(jwtConfig.getRefreshTokenLifeTime()).thenReturn(Short.parseShort("1"));
        Mockito.when(jwtConfig.getRefreshTokenLength()).thenReturn(Short.parseShort("1"));
    }

    @Test
    @DisplayName("login(): Should return successful auth result when a valid email and password combination is provided.")
    void whenLogin_withValidEmailAndPasswordCombination_thenReturnSuccessfulAuthResult() {
        String email = "test@test.com";
        String password = "PASSWORD_HASH";
        AppUser appUser = new AppUser(1L, "test@test.com", "test@test.com", UserType.Company, "PASSWORD_HASH", null, null);

        Mockito.when(userService.findByEmail(email)).thenReturn(appUser);
        Mockito.when(userService.checkPassword(appUser, password)).thenReturn(true);
        setUpJWTConfig();

        AuthResultDTO authResultDTO = authService.login(email, password);

        assertTrue(authResultDTO.isSucceeded());
    }

    @Test
    @DisplayName("login(): Should return failed auth result when an invalid email is provided.")
    void whenLogin_withInvalidEmail_thenReturnFailedAuthResult() {
        String email = "test@test.com";
        String password = "PASSWORD_HASH";

        Mockito.when(userService.findByEmail(email)).thenReturn(null);

        AuthResultDTO authResultDTO = authService.login(email, password);

        assertFalse(authResultDTO.isSucceeded());
    }

    @Test
    @DisplayName("login(): Should return failed auth result when an invalid email and password combination is provided.")
    void whenLogin_withInvalidEmailAndPasswordCombination_thenReturnFailedAuthResult() {
        String email = "test@test.com";
        String password = "PASSWORD_HASH";
        AppUser appUser = new AppUser(1L, "test@test.com", "test@test.com", UserType.Company, "PASSWORD_HASH", null, null);

        Mockito.when(userService.findByEmail(email)).thenReturn(appUser);
        Mockito.when(userService.checkPassword(appUser, password)).thenReturn(false);

        AuthResultDTO authResultDTO = authService.login(email, password);

        assertFalse(authResultDTO.isSucceeded());
    }

    @Test
    @DisplayName("registerCompany(): Should link company and return successful auth result when a valid email is provided.")
    void whenRegisterCompany_withValidEmail_thenLinkCompanyAndReturnSuccessfulAuthResult() {
        String email = "test@test.com";
        String password = "PASSWORD_HASH";
        Company company = new Company(2L, "Name", "Address", "Description", "Contact Number", "Category", null, "Logo File Name", null, null);

        Mockito.when(userService.findByEmail(email)).thenReturn(null);
        setUpJWTConfig();

        AuthResultDTO authResultDTO = authService.registerCompany(email, password, company);

        Mockito.verify(userService, Mockito.times(1)).addUser(authResultDTO.getUser(), password);
        Mockito.verify(companyService, Mockito.times(1)).addCompany(company);
        assertTrue(authResultDTO.isSucceeded());
    }

    @Test
    @DisplayName("registerCompany(): Should return failed auth result when an existing email is provided.")
    void whenRegisterCompany_withExistingEmail_thenReturnFailedAuthResult() {
        String email = "test@test.com";
        String password = "PASSWORD_HASH";

        Mockito.when(userService.findByEmail(email)).thenReturn(new AppUser());

        AuthResultDTO authResultDTO = authService.registerCompany(email, password, null);

        assertFalse(authResultDTO.isSucceeded());
    }

    @Test
    @DisplayName("registerCandidate(): Should link candidate and return successful auth result when a valid email is provided.")
    void whenRegisterCandidate_withValidEmail_thenLinkCandidateAndReturnSuccessfulAuthResult() {
        String email = "test@test.com";
        String password = "PASSWORD_HASH";
        Candidate candidate = new Candidate(2L, "First Name", "Last Name", "Gender", "Address", null, null, "Resume File Name", null, "Photo File Name", null, null);

        Mockito.when(userService.findByEmail(email)).thenReturn(null);
        setUpJWTConfig();

        AuthResultDTO authResultDTO = authService.registerCandidate(email, password, candidate);

        Mockito.verify(userService, Mockito.times(1)).addUser(authResultDTO.getUser(), password);
        Mockito.verify(candidateService, Mockito.times(1)).addCandidate(candidate);
        assertTrue(authResultDTO.isSucceeded());
    }

    @Test
    @DisplayName("registerCandidate(): Should return failed auth result when an existing email is provided.")
    void whenRegisterCandidate_withExistingEmail_thenReturnFailedAuthResult() {
        String email = "test@test.com";
        String password = "PASSWORD_HASH";

        Mockito.when(userService.findByEmail(email)).thenReturn(new AppUser());

        AuthResultDTO authResultDTO = authService.registerCompany(email, password, null);

        assertFalse(authResultDTO.isSucceeded());
    }

    @Test
    @DisplayName("refreshToken(): Should revoke token and return successful auth result when a valid refresh token is provided.")
    void whenRefreshToken_withValidRefreshToken_thenRevokeTokenAndReturnSuccessfulAuthResult() {
        String refreshToken = "REFRESH_TOKEN";
        AppUser appUser = new AppUser(1L, "test@test.com", "test@test.com", UserType.Company, "PASSWORD_HASH", null, null);
        RefreshToken storedRefreshToken = new RefreshToken("REFRESH_TOKEN", null, false, null, LocalDateTime.now().plusDays(1), appUser);

        Mockito.when(refreshTokenRepository.findById(refreshToken)).thenReturn(Optional.of(storedRefreshToken));
        setUpJWTConfig();

        AuthResultDTO authResultDTO = authService.refreshToken(refreshToken);

        assertTrue(storedRefreshToken.getIsRevoked());
        Mockito.verify(refreshTokenRepository, Mockito.times(1)).save(storedRefreshToken);
        assertTrue(authResultDTO.isSucceeded());
    }

    @Test
    @DisplayName("refreshToken(): Should return failed auth result when a non existing refresh token is provided.")
    void whenRefreshToken_withNonExistingRefreshToken_thenReturnFailedAuthResult() {
        String refreshToken = "REFRESH_TOKEN";

        Mockito.when(refreshTokenRepository.findById(refreshToken)).thenReturn(Optional.empty());

        AuthResultDTO authResultDTO = authService.refreshToken(refreshToken);

        assertFalse(authResultDTO.isSucceeded());
    }

    @Test
    @DisplayName("refreshToken(): Should return failed auth result when an expired refresh token is provided.")
    void whenRefreshToken_withExpiredRefreshToken_thenReturnFailedAuthResult() {
        String refreshToken = "REFRESH_TOKEN";
        RefreshToken storedRefreshToken = new RefreshToken("REFRESH_TOKEN", null, false, null, LocalDateTime.now().minusDays(1), null);

        Mockito.when(refreshTokenRepository.findById(refreshToken)).thenReturn(Optional.of(storedRefreshToken));

        AuthResultDTO authResultDTO = authService.refreshToken(refreshToken);

        assertFalse(authResultDTO.isSucceeded());
    }

    @Test
    @DisplayName("refreshToken(): Should revoke all user refresh tokens and return failed auth result when a revoked refresh token is provided.")
    void whenRefreshToken_withRevokedRefreshToken_thenRevokeAllUserRefreshTokensAndReturnFailedAuthResult() {
        String refreshToken = "REFRESH_TOKEN";
        AppUser appUser = new AppUser(1L, "test@test.com", "test@test.com", UserType.Company, "PASSWORD_HASH", null, null);
        RefreshToken storedRefreshToken = new RefreshToken("REFRESH_TOKEN", null, true, null, LocalDateTime.now().plusDays(1), appUser);
        ArrayList<RefreshToken> userRefreshTokens = new ArrayList<>();
        userRefreshTokens.add(new RefreshToken("ANOTHER_REFRESH_TOKEN", null, true, null, LocalDateTime.now().plusDays(1), appUser));

        Mockito.when(refreshTokenRepository.findById(refreshToken)).thenReturn(Optional.of(storedRefreshToken));
        Mockito.when(refreshTokenRepository.findAllByUserId(storedRefreshToken.getUser().getId())).thenReturn(userRefreshTokens);

        AuthResultDTO authResultDTO = authService.refreshToken(refreshToken);

        for(RefreshToken userRefreshToken: userRefreshTokens){
            assertTrue(userRefreshToken.getIsRevoked());
        }
        Mockito.verify(refreshTokenRepository, Mockito.times(1)).saveAll(userRefreshTokens);
        assertFalse(authResultDTO.isSucceeded());
    }
}