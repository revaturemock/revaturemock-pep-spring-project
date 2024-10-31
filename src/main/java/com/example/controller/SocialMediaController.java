package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 LOCAL CHANGE!!!
 */

 @RestController
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;
    
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<Account> addAccount(@RequestBody Account account) {

        Optional<Account> possibleAccount = accountService.addAccount(account);

        if (possibleAccount.isPresent() && possibleAccount.get().getUsername() != null) {
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else if (possibleAccount.isPresent() && possibleAccount.get().getUsername() == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }        
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {

        Optional<Account> possibleAccount = accountService.login(account);
        
        if (possibleAccount.isPresent()) {
            return new ResponseEntity<>(possibleAccount.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<Account>(HttpStatus.UNAUTHORIZED);
        }
    }
 
    @PostMapping("/messages")
    public ResponseEntity<Message> addMessage(@RequestBody Message message) {

        Optional<Message> possibleMessage = messageService.addMessage(message);

        if (possibleMessage.isPresent()) {
            return new ResponseEntity<Message>(possibleMessage.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<Message>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {

        Optional<List<Message>> possibleMessages = messageService.getAllMessages();

        List<Message> messages = possibleMessages.get();

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getAllMessagesByUser(@PathVariable Integer account_id) {

        Optional<List<Message>> possibleMessages = messageService.getAllMessagesByUser(account_id);

        return new ResponseEntity<>(possibleMessages.get(), HttpStatus.OK);
    }

    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer message_id) {
        Optional<Message> possibleMessage = messageService.getMessageById(message_id);

        if (possibleMessage.isPresent()) {
            return new ResponseEntity<>(possibleMessage.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        
    }

    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer message_id) {
        Optional<Integer> possibleDeletions = messageService.deleteMessageById(message_id);

        if (possibleDeletions.isPresent()) {
            return new ResponseEntity<>(possibleDeletions.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Integer> updateMessageById(@PathVariable Integer message_id, @RequestBody Message message) {
        Optional<Integer> possibleUpdates = messageService.updateMessageById(message_id, message.getMessage_text());

        if (possibleUpdates.isPresent()) {
            return new ResponseEntity<>(possibleUpdates.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
    }
}
