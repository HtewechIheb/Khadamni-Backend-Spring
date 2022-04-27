package tn.rnu.enicarthage.khadamni.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.rnu.enicarthage.khadamni.models.Candidate;
import tn.rnu.enicarthage.khadamni.repositories.CandidateRepository;
import tn.rnu.enicarthage.khadamni.services.interfaces.CandidateService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CandidateServiceImplTest {
    @Mock
    private CandidateRepository candidateRepository;

    @InjectMocks
    private CandidateService candidateService = new CandidateServiceImpl();

    @Test
    @DisplayName("getCandidates(): Should return all candidates.")
    void whenGetCandidates_thenReturnAllCandidates() {
        ArrayList<Candidate> candidates = new ArrayList<>();
        candidates.add(new Candidate(1L, "First Name", "Last Name", "Gender", "Address", null, null, "Resume File Name", null, "Photo File Name", null, null));
        candidates.add(new Candidate(2L, "First Name", "Last Name", "Gender", "Address", null, null, "Resume File Name", null, "Photo File Name", null, null));

        Mockito.when(candidateRepository.findAll()).thenReturn(candidates);

        List<Candidate> candidatesToTest = candidateService.getCandidates();

        assertIterableEquals(candidatesToTest, candidates);
    }

    @Test
    @DisplayName("getCandidateById(): Should return the correct candidate when a valid id is provided.")
    void whenGetCandidateById_withValidId_thenReturnCandidate() {
        Candidate candidate = new Candidate(1L, "First Name", "Last Name", "Gender", "Address", null, null, "Resume File Name", null, "Photo File Name", null, null);

        Mockito.when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));

        Candidate candidateToTest = candidateService.getCandidateById(1L);

        assertEquals(candidateToTest, candidate);
    }

    @Test
    @DisplayName("getCandidateById(): Should return null when an invalid id is provided.")
    void whenGetCandidateById_withInvalidId_thenReturnNull() {
        Mockito.when(candidateRepository.findById(1L)).thenReturn(Optional.empty());

        Candidate candidateToTest = candidateService.getCandidateById(1L);

        assertNull(candidateToTest);
    }

    @Test
    @DisplayName("candidateExists(): Should return true when a valid candidate's id is provided.")
    void whenCandidateExists_withValidCandidateId_thenReturnTrue() {
        Mockito.when(candidateRepository.existsById(1L)).thenReturn(true);

        boolean result = candidateService.candidateExists(1L);

        assertTrue(result);
    }

    @Test
    @DisplayName("candidateExists(): Should return false when an invalid candidate's id is provided.")
    void whenCandidateExists_withInvalidCandidateId_thenReturnFalse() {
        Mockito.when(candidateRepository.existsById(1L)).thenReturn(false);

        boolean result = candidateService.candidateExists(1L);

        assertFalse(result);
    }

    @Test
    @DisplayName("addCandidate(): Should return added candidate.")
    void whenAddCandidate_thenReturnAddedCandidate() {
        Candidate candidate = new Candidate(1L, "First Name", "Last Name", "Gender", "Address", null, null, "Resume File Name", null, "Photo File Name", null, null);

        Mockito.when(candidateRepository.save(candidate)).thenReturn(candidate);

        Candidate candidateToTest = candidateService.addCandidate(candidate);

        Mockito.verify(candidateRepository, Mockito.times(1)).save(candidate);
        assertEquals(candidateToTest, candidate);
    }

    @Test
    @DisplayName("addCandidate(): Should return updated candidate when a valid id is provided.")
    void whenUpdateCandidate_withValidId_thenReturnUpdatedCandidate() {
        Candidate candidate = new Candidate(1L, "First Name", "Last Name", "Gender", "Address", null, null, "Resume File Name", null, "Photo File Name", null, null);
        Candidate updatedCandidate = new Candidate(1L, "Updated First Name", "Updated Last Name", "Updated Gender", "Updated Address", null, null, "Updated Resume File Name", null, "Updated Photo File Name", null, null);

        Mockito.when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));

        Candidate candidateToTest = candidateService.updateCandidate(1L, updatedCandidate);

        Mockito.verify(candidateRepository, Mockito.times(1)).save(candidate);
        assertEquals(candidateToTest, updatedCandidate);
    }

    @Test
    @DisplayName("addCandidate(): Should return null when an invalid id is provided.")
    void whenUpdateCandidate_withInvalidId_thenReturnNull() {
        Mockito.when(candidateRepository.findById(1L)).thenReturn(Optional.empty());

        Candidate candidateToTest = candidateService.updateCandidate(1L, new Candidate());

        assertNull(candidateToTest);
    }

    @Test
    void deleteCandidate() {
        candidateService.deleteCandidate(1L);

        Mockito.verify(candidateRepository, Mockito.times(1)).deleteById(1L);
    }
}