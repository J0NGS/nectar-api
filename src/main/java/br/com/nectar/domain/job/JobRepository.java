package br.com.nectar.domain.job;

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
public interface JobRepository extends JpaRepository<Job, UUID> {
    @Query("""
        SELECT j FROM Job j
        WHERE j.beekeeper.id = :beekeeperId
    """)
    Page<Job> findByBeekeeperName(@Param("beekeeperName") String beekeeperId, Pageable pageable);
    

    @Query("""
        SELECT j FROM Job j
        WHERE j.owner.id = :ownerId
    """)
    Page<Job> findByOwnerId(@Param("ownerId") UUID ownerId, Pageable pageable);
    

    @Query("""
        SELECT j FROM Job j
        WHERE j.org.id = :orgId
    """)
    Page<Job> findByOrgId(@Param("orgId") UUID orgId, Pageable pageable);
    

    @Query("""
        SELECT j FROM Job j
        WHERE j.id = :jobId
    """)
    Optional<Job> findById(@Param("jobId") UUID jobId);

    @Query("SELECT j FROM Job j where j.org.id = :userOrgId AND j.status IN :statuslist")
    Page<Job> getPageByStatus(
        @Param("userOrgId") UUID userOrgId,
        @Param("statuslist") List<JobsStatus> status,
        Pageable pageable
    );

    @Query("""
        SELECT j FROM Job j
        where j.org.id = :userOrgId
        AND j.status IN :statuslist
        AND j.createdAt >= :begin AND j.createdAt <= :end
    """)
    List<Job> getAllByStatus(
        @Param("begin") LocalDateTime begin,
        @Param("end") LocalDateTime end,
        @Param("userOrgId") UUID userOrgId,
        @Param("statuslist") List<JobsStatus> status
    );

    @Query("""
        SELECT j FROM Job j
        where j.org.id = :userOrgId
        AND j.status IN :statuslist
        AND j.createdAt >= :begin AND j.createdAt <= :end
    """)
    Page<Job> getPageByStatusAndDate(
        @Param("begin") LocalDateTime begin,
        @Param("end") LocalDateTime end,
        @Param("userOrgId") UUID userOrgId,
        @Param("statuslist") List<JobsStatus> status,
        Pageable pageable
    );

    @Query("""
        SELECT j FROM Job j
        WHERE j.org.id = :userOrgId
        AND j.beekeeper.id = :beekeeperId
        AND j.status IN :statuslist
    """)
    Page<Job> getPageByStatusAndBeekeeper(
        @Param("userOrgId") UUID userOrgId,
        @Param("beekeeperId") UUID beekeeperId,
        @Param("statuslist") List<JobsStatus> status,
        Pageable pageable
    );
}
