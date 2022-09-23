package com.ironhack.vbnk_dataservice.repositories.users;

import com.ironhack.vbnk_dataservice.data.dao.users.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountHolderRepository extends UserRepository, JpaRepository<AccountHolder, String> {
    boolean existsByUsername(String username);

    Optional<AccountHolder> findByUsername(String username);
}
