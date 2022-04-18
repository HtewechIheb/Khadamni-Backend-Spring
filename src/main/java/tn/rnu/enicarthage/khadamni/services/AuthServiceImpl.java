package tn.rnu.enicarthage.khadamni.services;

import org.springframework.stereotype.Service;
import tn.rnu.enicarthage.khadamni.dto.AuthResultDTO;
import tn.rnu.enicarthage.khadamni.models.Candidate;
import tn.rnu.enicarthage.khadamni.models.Company;
import tn.rnu.enicarthage.khadamni.services.interfaces.AuthService;

import javax.transaction.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    @Override
    public AuthResultDTO loginAsync(String email, String password) {
        return null;
    }

    @Override
    public AuthResultDTO registerCompanyAsync(String email, String password, Company company) {
        return null;
    }

    @Override
    public AuthResultDTO registerCandidateAsync(String email, String password, Candidate candidate) {
        return null;
    }

    @Override
    public AuthResultDTO refreshTokenAsync(String token, String refreshToken) {
        return null;
    }
}
