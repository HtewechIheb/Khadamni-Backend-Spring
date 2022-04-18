package tn.rnu.enicarthage.khadamni.services.interfaces;

import tn.rnu.enicarthage.khadamni.models.Candidate;

import java.util.List;

public interface CandidateService {
    List<Candidate> getCandidates();
    Candidate getCandidateById(Long id);
    boolean candidateExists(Long id);
    Candidate addCandidate(Candidate candidate);
    Candidate updateCandidate(Long id, Candidate updatedCandidate);
    void deleteCandidate(Long id);
}
