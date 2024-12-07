package br.com.nectar.domain.manager;

import br.com.nectar.domain.user.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ManagerRepository extends JpaRepository<Manager, UUID> {

    @Query("SELECT m FROM Manager m WHERE m.user.id = :userId")
    Manager getByUserId (
        @Param("userId") UUID userId
    );

    @Query("SELECT m FROM Manager m WHERE m.org.id = :orgId AND m.user.status = :status")
    Page<Manager> getPageByStatus(
        @Param("userOrgId") UUID userOrgId,
        @Param("status") UserStatus status,
        Pageable pageable
    );

    @Query("SELECT m FROM Manager m WHERE m.org.id = :orgId AND m.user.status = :status")
    List<Manager> getAllByStatus(
        @Param("userOrgId") UUID userOrgId,
        @Param("status") UserStatus status
    );
}
