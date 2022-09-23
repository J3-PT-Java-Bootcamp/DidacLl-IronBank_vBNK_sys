package com.ironhack.vbnk_transactionservice.repositories;

import com.ironhack.vbnk_transactionservice.data.TransactionState;
import com.ironhack.vbnk_transactionservice.data.dao.VBTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<VBTransaction,String> {
    List<VBTransaction> findAllBySubjAccountOrderByModificationDesc(String accountId, Pageable pageable);
    List<VBTransaction> findAllBySubjAccountAndState(String accountId, TransactionState state);
}
