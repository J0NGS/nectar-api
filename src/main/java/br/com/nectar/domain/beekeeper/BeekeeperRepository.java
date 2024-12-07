package br.com.nectar.domain.beekeeper;

import br.com.nectar.domain.user.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface BeekeeperRepository extends JpaRepository<Beekeeper, UUID> {

    @Query("SELECT bkp FROM Beekeeper bkp WHERE bkp.status = :status")
    Page<Beekeeper> getPageByStatus(
        @Param("status")UserStatus status,
        Pageable pageable
    );
}
