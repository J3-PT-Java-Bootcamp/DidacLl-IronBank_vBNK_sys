package com.ironhack.vbnk_dataservice.repositories.accounts;

import com.ironhack.vbnk_dataservice.data.dao.accounts.VBAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VBAccountRepository extends JpaRepository<VBAccount, UUID> {

    List<VBAccount> findAllByPrimaryOwnerId(String id);
    List<VBAccount> findAllBySecondaryOwnerId(String id);

}
