package com.ironhack.vbnk_antifraudservice.repositories;

import com.ironhack.vbnk_antifraudservice.model.AFTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface AntiFraudRepository extends JpaRepository<AFTransaction, String> {

    List<AFTransaction> findAllBySrcAccountNumberOrderByTransactionDateDesc(String accountId);

    List<AFTransaction> findAllBySenderIdOrderByTransactionDateDesc(String accountId);

    List<AFTransaction> findAllByTransactionDateBefore(Instant date);


}
