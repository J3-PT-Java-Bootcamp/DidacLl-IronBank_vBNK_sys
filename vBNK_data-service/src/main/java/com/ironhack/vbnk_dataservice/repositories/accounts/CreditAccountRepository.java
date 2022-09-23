package com.ironhack.vbnk_dataservice.repositories.accounts;

import com.ironhack.vbnk_dataservice.data.dao.accounts.CreditAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditAccountRepository extends JpaRepository<CreditAccount, String> {

    List<CreditAccount> findAllByPrimaryOwnerId(String userId);

    List<CreditAccount> findAllBySecondaryOwnerId(String userId);

    boolean existsByAccountNumber(String destinationAccountRef);

    Optional<CreditAccount> findByAccountNumber(String ref);
}