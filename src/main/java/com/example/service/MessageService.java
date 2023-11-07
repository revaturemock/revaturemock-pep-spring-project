package com.example.service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;


@Service
public class MessageService {

    private AccountRepository accountRepository;
    private MessageRepository messageRepository;

    public MessageService(AccountRepository accountRepository, MessageRepository messageRepository) {
        this.accountRepository = accountRepository;
        this.messageRepository = messageRepository;
    }

    public Optional<Message> addMessage(Message message) {

        // check if posted_by refers to real user
        Optional<Account> possibleAccount = accountRepository.findById(message.getPosted_by());
        boolean realUser = false;

        if (possibleAccount.isPresent()) {
            realUser = true;
        }
        if (!message.getMessage_text().isBlank() && message.getMessage_text().length() < 255 && realUser) {
            return Optional.of(messageRepository.save(message));
        }

        return Optional.empty();
    }

    public Optional<List<Message>> getAllMessages() {
        return Optional.of(messageRepository.findAll());
    }

    public Optional<List<Message>> getAllMessagesByUser(Integer id) {
        return messageRepository.findAllByUser(id);
    }

    public Optional<Message> getMessageById(Integer message_id) {
        return messageRepository.findById(message_id);
    }

    public Optional<Integer> deleteMessageById(Integer message_id) {

        Optional<Message> possibleMessage = messageRepository.findById(message_id);

        if (possibleMessage.isPresent()) {
            messageRepository.deleteById(message_id);
            return Optional.of(1);
        }
        
        return Optional.empty();

    }

    public Optional<Integer> updateMessageById(Integer message_id, String message_text) {

        Optional<Message> possibleMessage = messageRepository.findById(message_id);
        if (possibleMessage.isPresent() && 
        !message_text.isBlank() && message_text.length() <= 255){
            possibleMessage.get().setMessage_text(message_text);
            messageRepository.save(possibleMessage.get());
            return Optional.of(1);
        }

        return Optional.empty();
    }
}
