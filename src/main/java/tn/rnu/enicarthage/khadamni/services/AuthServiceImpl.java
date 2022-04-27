package tn.rnu.enicarthage.khadamni.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserService userService;

    @Autowired
    private JWTConfig jwtConfig;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CandidateService candidateService;

    @Override
    public AuthResultDTO login(String email, String password) {
        AppUser existingUser = userService.findByEmail(email);

        if (existingUser == null)
        {
            AuthResultDTO authResultDTO = new AuthResultDTO();
            authResultDTO.setSucceeded(false);
            authResultDTO.setError(String.format("User With Email %s Does Not Exist!", email));
            return authResultDTO;
        }

        boolean isValid = userService.checkPassword(existingUser, password);

        if (!isValid)
        {
            AuthResultDTO authResultDTO = new AuthResultDTO();
            authResultDTO.setSucceeded(false);
            authResultDTO.setError("Login Failed, Please Verify Your Credentials!");
            return authResultDTO;
        }

        return generateAuthResult(existingUser, null);
    }

    @Override
    public AuthResultDTO registerCompany(String email, String password, Company company) {
        AppUser existingUser = userService.findByEmail(email);

        if (existingUser != null)
        {
           AuthResultDTO authResultDTO = new AuthResultDTO();
           authResultDTO.setSucceeded(false);
           authResultDTO.setError(String.format("User With Email %s Already Exists!", email));
           return authResultDTO;
        }

        AppUser user = new AppUser();
        user.setUserName(email);
        user.setEmail(email);
        user.setType(UserType.Company);

        userService.addUser(user, password);
        company.setAccount(user);

        companyService.addCompany(company);
        return generateAuthResult(user, company);
    }

    @Override
    public AuthResultDTO registerCandidate(String email, String password, Candidate candidate) {
        AppUser existingUser = userService.findByEmail(email);

        if (existingUser != null)
        {
            AuthResultDTO authResultDTO = new AuthResultDTO();
            authResultDTO.setSucceeded(false);
            authResultDTO.setError(String.format("User With Email %s Already Exists!", email));
            return authResultDTO;
        }

        AppUser user = new AppUser();
        user.setUserName(email);
        user.setEmail(email);
        user.setType(UserType.Candidate);

        userService.addUser(user, password);
        candidate.setAccount(user);

        candidateService.addCandidate(candidate);
        return generateAuthResult(user, candidate);
    }

    @Override
    public AuthResultDTO refreshToken(String refreshToken) {
        RefreshToken storedRefreshToken = refreshTokenRepository.findById(refreshToken).orElse(null);

        if(storedRefreshToken == null) {
            AuthResultDTO authResultDTO = new AuthResultDTO();
            authResultDTO.setSucceeded(false);
            authResultDTO.setError("Refresh Token Does Not Exist!");
            return authResultDTO;
        }

        if (storedRefreshToken.getExpiryDate().compareTo(LocalDateTime.now()) <= 0) {
            AuthResultDTO authResultDTO = new AuthResultDTO();
            authResultDTO.setSucceeded(false);
            authResultDTO.setError("Refresh Token Has Expired!");
            return authResultDTO;
        }

        if (storedRefreshToken.getIsRevoked()) {
            revokeAllUserRefreshTokens(storedRefreshToken.getUser());

            AuthResultDTO authResultDTO = new AuthResultDTO();
            authResultDTO.setSucceeded(false);
            authResultDTO.setError("Refresh Token Has Been Revoked!");
            return authResultDTO;
        }

        revokeRefreshToken(storedRefreshToken);

        return generateAuthResult(storedRefreshToken.getUser(), null);
    }

    private void revokeRefreshToken(RefreshToken refreshToken)
    {
        refreshToken.setIsRevoked(true);

        refreshTokenRepository.save(refreshToken);
    }

    private void revokeAllUserRefreshTokens(AppUser user)
    {
        List<RefreshToken> refreshTokens = refreshTokenRepository.findAllByUserId(user.getId());

        refreshTokens.forEach(refreshToken -> refreshToken.setIsRevoked(true));

        refreshTokenRepository.saveAll(refreshTokens);
    }

    private AuthResultDTO generateAuthResult(AppUser user, Object userInfo)
    {
        try {
            String info = null;

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            // TODO: Update this hacky method of extracting user info
            if(user.getType() == UserType.Company)
            {
                if(Objects.nonNull(userInfo)){
                    info = objectMapper.writeValueAsString((Company)userInfo);
                }
                else {
                    info = objectMapper.writeValueAsString(user.getCompany());
                }
            }
            else if(user.getType() == UserType.Candidate)
            {
                if(Objects.nonNull(userInfo)){
                    info = objectMapper.writeValueAsString((Candidate)userInfo);
                }
                else {
                    info = objectMapper.writeValueAsString(user.getCandidate());
                }
            }

            UUID jwtId = UUID.randomUUID();
            Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
            String token = JWT.create()
                    .withSubject(user.getUserName())
                    .withExpiresAt(new Date(System.currentTimeMillis() + jwtConfig.getTokenLifeTime() * 60 * 1000))
                    .withJWTId(jwtId.toString())
                    .withClaim("id", user.getId())
                    .withClaim("email", user.getEmail())
                    .withClaim("type", user.getType().toString().toLowerCase())
                    .withClaim("info", info)
                    .sign(algorithm);

            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setToken(generateRefreshToken(jwtConfig.getRefreshTokenLength()));
            refreshToken.setJwtId(jwtId.toString());
            refreshToken.setIsRevoked(false);
            refreshToken.setAddedDate(LocalDateTime.now());
            refreshToken.setExpiryDate(LocalDateTime.now().plusDays(jwtConfig.getRefreshTokenLifeTime()));
            refreshToken.setUser(user);

            refreshTokenRepository.save(refreshToken);

            AuthResultDTO authResultDTO = new AuthResultDTO();
            authResultDTO.setSucceeded(true);
            authResultDTO.setUser(user);
            authResultDTO.setToken(token);
            authResultDTO.setRefreshToken(refreshToken.getToken());

            return authResultDTO;
        }
        catch (JsonProcessingException exception){
            AuthResultDTO authResultDTO = new AuthResultDTO();
            authResultDTO.setSucceeded(false);
            authResultDTO.setError("Internal Error Occurred!");
            return authResultDTO;
        }
    }

    private String generateRefreshToken(int length)
    {
        Random random = new Random();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < length; i++){
            builder.append(chars.charAt(random.nextInt(length)));
        }

        return builder.toString();
    }
}
