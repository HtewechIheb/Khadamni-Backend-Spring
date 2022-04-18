package tn.rnu.enicarthage.khadamni.services;

import org.hibernate.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.rnu.enicarthage.khadamni.models.Candidate;
import tn.rnu.enicarthage.khadamni.repositories.CandidateRepository;
import tn.rnu.enicarthage.khadamni.services.interfaces.CandidateService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class CandidateServiceImpl implements CandidateService {
    @Autowired
    private CandidateRepository candidateRepository;

    @Override
    public List<Candidate> getCandidates() {
        return candidateRepository.findAll();
    }

    @Override
    public Candidate getCandidateById(Long id) {
        return candidateRepository.findById(id).orElse(null);
    }

    @Override
    public boolean candidateExists(Long id) {
        return candidateRepository.existsById(id);
    }

    @Override
    public Candidate addCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    @Override
    public Candidate updateCandidate(Long id, Candidate updatedCandidate) {
        Candidate candidateToUpdate = candidateRepository.findById(id).orElse(null);

        if(Objects.nonNull(candidateToUpdate)){
            if(!StringHelper.isBlank(updatedCandidate.getFirstName())){
                candidateToUpdate.setFirstName(updatedCandidate.getFirstName());
            }
            if(!StringHelper.isBlank(updatedCandidate.getLastName())){
                candidateToUpdate.setLastName(updatedCandidate.getLastName());
            }
            if(!StringHelper.isBlank(updatedCandidate.getAddress())){
                candidateToUpdate.setAddress(updatedCandidate.getAddress());
            }
            if(!StringHelper.isBlank(updatedCandidate.getGender())){
                candidateToUpdate.setGender(updatedCandidate.getGender());
            }
            if(!StringHelper.isBlank(updatedCandidate.getAddress())){
                candidateToUpdate.setAddress(updatedCandidate.getAddress());
            }
            if(Objects.nonNull(updatedCandidate.getBirthdate())){
                candidateToUpdate.setBirthdate(updatedCandidate.getBirthdate());
            }
            if(!StringHelper.isBlank(updatedCandidate.getResumeFileName())){
                candidateToUpdate.setResumeFileName(updatedCandidate.getResumeFileName());
            }
            if(Objects.nonNull(updatedCandidate.getResumeFile()) && updatedCandidate.getResumeFile().length > 0){
                candidateToUpdate.setResumeFile(updatedCandidate.getResumeFile());
            }
            if(!StringHelper.isBlank(updatedCandidate.getPhotoFileName())){
                candidateToUpdate.setPhotoFileName(updatedCandidate.getPhotoFileName());
            }
            if(Objects.nonNull(updatedCandidate.getPhotoFile()) && updatedCandidate.getPhotoFile().length > 0){
                candidateToUpdate.setPhotoFile(updatedCandidate.getPhotoFile());
            }

            candidateRepository.save(candidateToUpdate);
        }

        return candidateToUpdate;
    }

    @Override
    public void deleteCandidate(Long id) {
        candidateRepository.deleteById(id);
    }
}
