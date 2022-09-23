package com.ironhack.vbnk_dataservice.repositories.accounts;

import com.ironhack.vbnk_dataservice.data.dao.accounts.CheckingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CheckingAccountRepository extends JpaRepository<CheckingAccount, String> {

    List<CheckingAccount> findAllByPrimaryOwnerId(String userId);

    List<CheckingAccount> findAllBySecondaryOwnerId(String userId);

    boolean existsByAccountNumber(String accountNumber);

    Optional<CheckingAccount> findByAccountNumber(String ref);
}