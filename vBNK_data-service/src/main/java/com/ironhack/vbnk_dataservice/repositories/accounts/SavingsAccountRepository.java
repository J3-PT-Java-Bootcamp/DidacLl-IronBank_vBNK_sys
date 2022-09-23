package com.ironhack.vbnk_dataservice.repositories.accounts;

import com.ironhack.vbnk_dataservice.data.dao.accounts.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, String> {

    List<SavingsAccount> findAllByPrimaryOwnerId(String userId);

    List<SavingsAccount> findAllBySecondaryOwnerId(String userId);

    boolean existsByAccountNumber(String destinationAccountRef);

    Optional<SavingsAccount> findByAccountNumber(String ref);
}