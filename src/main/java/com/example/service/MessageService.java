package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    // Create a new message
    public Message createMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank() ||
            message.getMessageText().length() > 255) {
            throw new IllegalArgumentException("Message text must be between 1 and 255 characters.");
        }

        if (!accountRepository.existsById(message.getPostedBy())) {
            throw new IllegalArgumentException("User with the given ID does not exist.");
        }

        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(int id) {
        return messageRepository.findById(id);
    }

    public int deleteMessage(int id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return 1;
        }
        return 0;
    }

    public int updateMessage(int id, String newText) {
        if (newText == null || newText.isBlank() || newText.length() > 255) {
            throw new IllegalArgumentException("Invalid message text.");
        }

        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            message.setMessageText(newText);
            messageRepository.save(message);
            return 1;
        }

        throw new IllegalArgumentException("Message not found.");
    }

    public List<Message> getMessagesByUser(int accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
