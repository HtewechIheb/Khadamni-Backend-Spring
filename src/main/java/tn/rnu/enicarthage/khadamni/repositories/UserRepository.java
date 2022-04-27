package tn.rnu.enicarthage.khadamni.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.rnu.enicarthage.khadamni.models.AppUser;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUserName(String userName);
    Optional<AppUser> findByEmail(String email);
}
