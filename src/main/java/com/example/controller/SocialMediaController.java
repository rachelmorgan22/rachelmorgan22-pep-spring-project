package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account) {
       
        if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
            return ResponseEntity.badRequest().build(); // 400
        }

       
        if (accountService.getAccountByUsername(account.getUsername()).isPresent()) {
            return ResponseEntity.status(409).build(); // 409 Conflict
        }

        
        Optional<Account> created = accountService.register(account);
        return ResponseEntity.ok(created.get()); 
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account) {
        Optional<Account> result = accountService.login(account);
        if (result.isEmpty()) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(result.get());
    }

    @PostMapping("/messages")
    public ResponseEntity<?> postMessage(@RequestBody Message msg) {
        Optional<Message> result = messageService.createMessage(msg);
        if (result.isEmpty()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(result.get());
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<?> getMessageById(@PathVariable int id) {
        return ResponseEntity.ok(messageService.getMessageById(id).orElse(null));
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable int id) {
        int result = messageService.deleteMessageById(id);
        if (result == 1) return ResponseEntity.ok(result);
        return ResponseEntity.ok().build(); 
    }

    @PatchMapping("/messages/{id}")
    public ResponseEntity<?> updateMessage(@PathVariable int id, @RequestBody Message msg) {
        int result = messageService.updateMessage(id, msg.getMessageText());
        if (result == 1) return ResponseEntity.ok(result);
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/accounts/{id}/messages")
    public ResponseEntity<?> getMessagesByUser(@PathVariable int id) {
        return ResponseEntity.ok(messageService.getMessagesByAccountId(id));
    }
}
