package com.ironhack.vbnk_dataservice.repositories.users;

import com.ironhack.vbnk_dataservice.data.dao.users.VBAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends UserRepository, JpaRepository<VBAdmin, String> {
    boolean existsByUsername(String username);

    Optional<VBAdmin> findByUsername(String username);
}
