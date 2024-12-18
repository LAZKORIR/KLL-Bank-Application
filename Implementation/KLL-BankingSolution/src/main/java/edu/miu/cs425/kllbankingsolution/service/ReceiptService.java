package edu.miu.cs425.kllbankingsolution.service;

import edu.miu.cs425.kllbankingsolution.entities.Transaction;
import org.springframework.stereotype.Service;

@Service
public class ReceiptService {
    public String generateReceipt(Transaction transaction) {
        return String.format("Receipt: \nTransaction ID: %d\nType: %s\nAmount: %.2f\nDate: %s",
                transaction.getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getDateTime());
    }
}


