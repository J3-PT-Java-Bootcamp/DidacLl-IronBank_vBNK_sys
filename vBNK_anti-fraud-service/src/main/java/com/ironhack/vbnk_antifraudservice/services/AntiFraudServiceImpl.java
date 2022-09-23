package com.ironhack.vbnk_antifraudservice.services;

import com.ironhack.vbnk_antifraudservice.model.AFRequest;
import com.ironhack.vbnk_antifraudservice.model.AFResponse;
import com.ironhack.vbnk_antifraudservice.model.AFTransaction;
import com.ironhack.vbnk_antifraudservice.repositories.AntiFraudRepository;
import com.ironhack.vbnk_antifraudservice.utils.VBNKConfig;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AntiFraudServiceImpl implements AntiFraudService {
    final
    AntiFraudRepository afRepository;

    public AntiFraudServiceImpl(AntiFraudRepository afRepository) {
        this.afRepository = afRepository;
    }

    @Override
    public AFResponse registerTransaction(AFRequest request, AFResponse res) {
        afRepository.save(
                new AFTransaction()
                        .setResult(res)
                        .setSenderId(request.getSenderId())
                        .setAmount(request.getAmount())
                        .setSrcAccountNumber(request.getSrcAccountNumber())
        );
        return res;
    }

    @Override
    public AFResponse validateByAccount(AFRequest request) {
        return mainValidation(request, request.getSrcAccountNumber());
    }

    @Override
    public AFResponse validateByUser(AFRequest request) {
        return mainValidation(request, request.getSenderId());
    }

    @Override
    public AFResponse mainValidation(AFRequest request, String ref) {
        return new AFResponse()
                .setValidationLegalReq(validateLegalRequirements(request))
                .setValidationSpamBot(validateSpamBot(request, ref))
                .setValidationSpamHuman(validateSpamHuman(request, ref))
                .setValidationReiterateTrans(validateReiterateTransactions(request, ref))
                .setValidationAmountAVG(validateAmountAVG(request, ref))
                .setAllValidated(true);
    }

    private int validateAmountAVG(AFRequest request, String ref) {
        // TODO: 18/09/2022  
        return 0;
    }

    private int validateReiterateTransactions(AFRequest request, String ref) {
        List<AFTransaction> list = getAfTransactions(ref);
        if (list.isEmpty()) return 0;
        int val = 0;
        for (int i = 0; i < Math.min(list.size(), 3); i++) {
            if (list.get(i).compare(request)) val++;
        }
        if (val >= 2) return 1;
        return 0;
    }

    private int validateSpamHuman(AFRequest request, String ref) {
        List<AFTransaction> list = getAfTransactions(ref);
        int val = 0;
        for (int i = 0; i < Math.min(list.size(), 3); i++) {
            if (list.get(i).compareSimilarity(request) > 80) val++;
        }
        if (val >= 2) return 1;
        return 0;
    }

    private int validateSpamBot(AFRequest request, String ref) {
        List<AFTransaction> list = getAfTransactions(ref);
        if (!list.isEmpty() && !(list.get(0).getTransactionDate().plus(1, ChronoUnit.SECONDS).isBefore(Instant.now())))
            return 3;
        return 0;
    }

    private List<AFTransaction> getAfTransactions(String ref) {
        List<AFTransaction> list = afRepository.findAllBySrcAccountNumberOrderByTransactionDateDesc(ref);
        list.addAll(afRepository.findAllBySenderIdOrderByTransactionDateDesc(ref));
        list.sort((o1, o2) -> {
            if (o1.getTransactionDate().isBefore(o2.getTransactionDate())) return -1;
            return o1.getTransactionDate().isAfter(o2.getTransactionDate()) ? 1 : 0;
        });
        return list;
    }

    private int validateLegalRequirements(AFRequest request) {
        if (request.getAmount().compareTo(VBNKConfig.VBNK_LEGAL_MAX_TRANSFER_AMOUNT) > 0) return 1;
        var list = afRepository.findAllBySrcAccountNumberOrderByTransactionDateDesc(request.getSrcAccountNumber());
        if (list.isEmpty()) list = afRepository.findAllBySenderIdOrderByTransactionDateDesc(request.getSenderId());
        if (list.size() > VBNKConfig.VBNK_LEGAL_MAX_TRANSACTIONS) return 2;
        return 0;
    }

    @Scheduled(fixedRate = 6,timeUnit = TimeUnit.HOURS)
    private void update(){
        afRepository.deleteAll( afRepository.findAllByTransactionDateBefore(Instant.now().minus(2,ChronoUnit.DAYS)));

    }

}
