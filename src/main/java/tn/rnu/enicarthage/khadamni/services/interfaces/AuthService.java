package tn.rnu.enicarthage.khadamni.services.interfaces;

import tn.rnu.enicarthage.khadamni.dto.AuthResultDTO;
import tn.rnu.enicarthage.khadamni.models.Candidate;
import tn.rnu.enicarthage.khadamni.models.Company;

public interface AuthService {
    AuthResultDTO loginAsync(String email, String password);
    AuthResultDTO registerCompanyAsync(String email, String password, Company company);
    AuthResultDTO registerCandidateAsync(String email, String password, Candidate candidate);
    AuthResultDTO refreshTokenAsync(String token, String refreshToken);
}
