package com.ironhack.vbnk_dataservice.repositories.accounts;

import com.ironhack.vbnk_dataservice.data.dao.accounts.StudentCheckingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentCheckingAccountRepository extends JpaRepository<StudentCheckingAccount, String> {

    List<StudentCheckingAccount> findAllByPrimaryOwnerId(String userId);

    List<StudentCheckingAccount> findAllBySecondaryOwnerId(String userId);

    boolean existsByAccountNumber(String destinationAccountRef);

    Optional<StudentCheckingAccount> findByAccountNumber(String ref);
}