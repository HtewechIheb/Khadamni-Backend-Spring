package tn.rnu.enicarthage.khadamni.services.interfaces;

import tn.rnu.enicarthage.khadamni.dto.AuthResultDTO;
import tn.rnu.enicarthage.khadamni.models.Candidate;
import tn.rnu.enicarthage.khadamni.models.Company;

public interface AuthService {
    AuthResultDTO login(String email, String password);
    AuthResultDTO registerCompany(String email, String password, Company company);
    AuthResultDTO registerCandidate(String email, String password, Candidate candidate);
    AuthResultDTO refreshToken(String token, String refreshToken);
}
