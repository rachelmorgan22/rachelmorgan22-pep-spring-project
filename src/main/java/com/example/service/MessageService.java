package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepo;

    @Autowired
    private AccountRepository accountRepo;

    public Optional<Message> createMessage(Message msg) {
        if (msg.getMessageText() == null || msg.getMessageText().isBlank() || 
            msg.getMessageText().length() > 255 || 
            !accountRepo.existsById(msg.getPostedBy())) {
            return Optional.empty();
        }

        return Optional.of(messageRepo.save(msg));
    }

    public List<Message> getAllMessages() {
        return messageRepo.findAll();
    }

    public Optional<Message> getMessageById(int id) {
        return messageRepo.findById(id);
    }

    public int deleteMessageById(int id) {
        boolean exists = messageRepo.existsById(id);
        if (exists) {
            messageRepo.deleteById(id);
            return 1;
        }
        return 0;
    }

    public int updateMessage(int id, String newText) {
        Optional<Message> msgOpt = messageRepo.findById(id);
        if (msgOpt.isPresent() && newText != null && !newText.isBlank() && newText.length() <= 255) {
            Message msg = msgOpt.get();
            msg.setMessageText(newText);
            messageRepo.save(msg);
            return 1;
        }
        return 0;
    }

    public List<Message> getMessagesByAccountId(int accountId) {
        return messageRepo.findByPostedBy(accountId);
    }
}
