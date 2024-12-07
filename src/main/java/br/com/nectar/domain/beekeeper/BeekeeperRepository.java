package br.com.nectar.domain.beekeeper;

import br.com.nectar.domain.user.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BeekeeperRepository extends JpaRepository<Beekeeper, UUID> {

    @Query("SELECT bkp FROM Beekeeper bkp WHERE bkp.org.id = :userOrgId AND bkp.status = :status")
    Page<Beekeeper> getPageByStatus(
        @Param("userOrgId") UUID userOrgId,
        @Param("status")UserStatus status,
        Pageable pageable
    );

    @Query("SELECT bkp FROM Beekeeper bkp WHERE bkp.org.id = :userOrgId AND bkp.status = :status")
    List<Beekeeper> getAllByStatus(
        @Param("userOrgId") UUID userOrgId,
        @Param("status")UserStatus status
    );
}
