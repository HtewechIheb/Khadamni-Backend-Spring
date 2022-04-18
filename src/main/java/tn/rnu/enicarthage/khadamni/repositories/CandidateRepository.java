package tn.rnu.enicarthage.khadamni.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.rnu.enicarthage.khadamni.models.Candidate;

public interface CandidateRepository extends JpaRepository<Candidate, Long> { }
