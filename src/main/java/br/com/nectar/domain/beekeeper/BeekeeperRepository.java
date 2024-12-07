package br.com.nectar.domain.beekeeper;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeekeeperRepository extends JpaRepository<Beekeeper, UUID> {
}
