package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.InvalidMessageException;
import com.example.exception.MessageNotFoundException;
import com.example.exception.UserNotFoundException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    // Inject message repository
    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message newMessage(Message newMessage) throws InvalidMessageException, UserNotFoundException {
        // Invalid Message Exception
        // - Message must not be blank
        // - Message must not be over 255 characters
        if (newMessage.getMessageText() == null || 
        newMessage.getMessageText() == "" ||
        newMessage.getMessageText().length() > 255) {
            throw new InvalidMessageException();
        }

        // User Not Found Exception
        // - User must exist in database
        if (!accountRepository.findById(newMessage.getPostedBy()).isPresent()) {
            throw new UserNotFoundException();
        }

        return messageRepository.save(newMessage);
    }

    public List<Message> allMessages() {
        return (List<Message>) messageRepository.findAll();
    }

    public Message getMessageById(String messageId) {
        return messageRepository.findById(Integer.parseInt(messageId)).orElse(null);
    }

    public Long deleteMessageById(String messageId) throws MessageNotFoundException {
        if (!messageRepository.findById(Integer.parseInt(messageId)).isPresent()) {
            throw new MessageNotFoundException();
        }

        Long beforeDeletion = messageRepository.count();
        messageRepository.deleteById(Integer.parseInt(messageId));
        Long afterDeletion = messageRepository.count();

        return beforeDeletion - afterDeletion;
    }

    public Long updateMessageById(String messageId, Message updatedMessage) throws MessageNotFoundException, InvalidMessageException {
        // Message Not Found Exception
        Message message = messageRepository.findById(Integer.parseInt(messageId)).orElseThrow(() -> new MessageNotFoundException());

        // Invalid Message Exception
        // - Message must not be blank
        // - Message must not be over 255 characters
        if (updatedMessage.getMessageText() == "" || 
        updatedMessage.getMessageText() == null || 
        updatedMessage.getMessageText().length() > 255) {
            throw new InvalidMessageException();
        }

        message.setMessageText(updatedMessage.getMessageText());
        messageRepository.save(message);
        return (long) 1;
    }

    public List<Message> getAllMessagesFromAccountId(String accountId) throws UserNotFoundException {
        if (!accountRepository.findById(Integer.parseInt(accountId)).isPresent()) {
            throw new UserNotFoundException();
        }
        return messageRepository.findByPostedBy(Integer.parseInt(accountId)).orElse(null);
    }
}
