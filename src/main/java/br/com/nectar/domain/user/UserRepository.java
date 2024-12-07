package br.com.nectar.domain.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, UUID>{
    @Query("SELECT u FROM User u WHERE u.auth.username =:username")
    Optional<User> findByAuthUsername(String username);

    @Query("SELECT u FROM User u WHERE u.auth.username =:username")
    Optional<User> getByUsername(String username);
}
