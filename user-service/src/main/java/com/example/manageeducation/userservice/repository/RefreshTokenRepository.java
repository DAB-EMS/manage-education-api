package com.example.manageeducation.userservice.repository;

import com.example.manageeducation.userservice.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    @Query(value = """
      select t from RefreshToken t inner join Customer u\s
      on t.customer.id = u.id\s
      where u.id = :id and (t.expiryDate = false or t.revoked = false)\s
      """)
    List<RefreshToken> findAllValidTokenByUser(UUID id);

    Optional<RefreshToken> findByToken(String token);
}
