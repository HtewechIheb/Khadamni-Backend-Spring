package tn.rnu.enicarthage.khadamni.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.rnu.enicarthage.khadamni.models.RefreshToken;

import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    List<RefreshToken> findAllByUserId(Long id);
}
