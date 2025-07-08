package com.jpmc.midascore;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void processTransaction(Transaction transaction) {
        // Validate sender exists
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        if (sender == null) {
            System.out.println("Invalid sender ID: " + transaction.getSenderId());
            return;
        }

        // Validate recipient exists
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());
        if (recipient == null) {
            System.out.println("Invalid recipient ID: " + transaction.getRecipientId());
            return;
        }

        // Validate sender has sufficient balance
        if (sender.getBalance() < transaction.getAmount()) {
            System.out.println("Insufficient balance for sender: " + sender.getName() +
                    " (balance: " + sender.getBalance() + ", required: " + transaction.getAmount() + ")");
            return;
        }

        // Process the transaction
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount());

        // Save updated user records
        userRepository.save(sender);
        userRepository.save(recipient);

        // Create and save transaction record
        TransactionRecord transactionRecord = new TransactionRecord(sender, recipient, transaction.getAmount());
        transactionRepository.save(transactionRecord);

        System.out.println("Transaction processed: " + transactionRecord);
    }
}