package br.com.nectar.domain.work;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkRepository extends JpaRepository<Work, UUID> {
    @Query("""
        SELECT w FROM Work w
        WHERE w.beekeeper.id = :beekeeperId
    """)
    Page<Work> findByBeekeeperName(@Param("beekeeperName") String beekeeperId, Pageable pageable);
    

    @Query("""
        SELECT w FROM Work w
        WHERE w.owner.id = :ownerId
    """)
    Page<Work> findByOwnerId(@Param("ownerId") UUID ownerId, Pageable pageable);
    

    @Query("""
        SELECT w FROM Work w
        WHERE w.org.id = :orgId
    """)
    Page<Work> findByOrgId(@Param("orgId") UUID orgId, Pageable pageable);
    

    @Query("""
        SELECT j FROM Work j
        WHERE j.id = :jobId
    """)
    Optional<Work> findById(@Param("jobId") UUID jobId);

    @Query("SELECT j FROM Work j where j.org.id = :userOrgId AND j.status IN :statuslist")
    Page<Work> getPageByStatus(
        @Param("userOrgId") UUID userOrgId,
        @Param("statuslist") List<WorkStatus> status,
        Pageable pageable
    );

    @Query("""
        SELECT j FROM Work j
        where j.org.id = :userOrgId
        AND j.status IN :statuslist
        AND j.createdAt >= :begin AND j.createdAt <= :end
    """)
    List<Work> getAllByStatus(
        @Param("begin") LocalDateTime begin,
        @Param("end") LocalDateTime end,
        @Param("userOrgId") UUID userOrgId,
        @Param("statuslist") List<WorkStatus> status
    );

    @Query("""
        SELECT j FROM Work j
        where j.org.id = :userOrgId
        AND j.status IN :statuslist
        AND j.createdAt >= :begin AND j.createdAt <= :end
    """)
    Page<Work> getPageByStatusAndDate(
        @Param("begin") LocalDateTime begin,
        @Param("end") LocalDateTime end,
        @Param("userOrgId") UUID userOrgId,
        @Param("statuslist") List<WorkStatus> status,
        Pageable pageable
    );

    @Query("""
        SELECT j FROM Work j
        WHERE j.org.id = :userOrgId
        AND j.beekeeper.id = :beekeeperId
        AND j.status IN :statuslist
    """)
    Page<Work> getPageByStatusAndBeekeeper(
        @Param("userOrgId") UUID userOrgId,
        @Param("beekeeperId") UUID beekeeperId,
        @Param("statuslist") List<WorkStatus> status,
        Pageable pageable
    );
}
