package tn.rnu.enicarthage.khadamni.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.rnu.enicarthage.khadamni.models.Application;
import tn.rnu.enicarthage.khadamni.models.ApplicationId;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, ApplicationId> {
    List<Application> findByApplicationIdOfferId(Long id);
    List<Application> findByApplicationIdCandidateId(Long id);
}
