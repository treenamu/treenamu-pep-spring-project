package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.InvalidLoginException;
import com.example.exception.InvalidMessageException;
import com.example.exception.MessageNotFoundException;
import com.example.exception.UserNotFoundException;
import com.example.exception.UsernameOrPasswordInvalidException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    // Inject account and message service
    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    // #1: Our API should be able to process new User registrations
    @PostMapping("/register")
    public ResponseEntity<Account> newAccount(@RequestBody Account newAccount) {
        try {
            Account createdAccount = accountService.newAccount(newAccount);
            return ResponseEntity.status(HttpStatus.OK)
            .body(createdAccount);
        } catch (UsernameOrPasswordInvalidException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(null);
        } catch (DuplicateUsernameException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(null);
        }
    }

    // 2: Our API should be able to process User logins
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account loginInfo) {
        try {
            Account account = accountService.login(loginInfo);
            return ResponseEntity.status(HttpStatus.OK)
                .body(account);
        } catch (InvalidLoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(null);
        }
    }

    // 3: Our API should be able to process the creation of new messages
    @PostMapping("/messages")
    public ResponseEntity<Message> newMessage(@RequestBody Message newMessage) {
        try {
            Message message = messageService.newMessage(newMessage);
            return ResponseEntity.status(HttpStatus.OK)
                .body(message);
        } catch(InvalidMessageException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(null);
        } catch(UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(null);
        }
    }

    // 4: Our API should be able to retrieve all messages
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> allMessages() {
        List<Message> messages = messageService.allMessages();
        return ResponseEntity.status(HttpStatus.OK)
            .body(messages);
    }

    // 5: Our API should be able to retrieve a message by its ID
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable String messageId) {
        Message message = messageService.getMessageById(messageId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(message);
    }

    // 6: Our API should be able to delete a message identified by a message ID
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Long> deleteMessageById(@PathVariable String messageId) {
        try {
            Long numberOfRowsDeleted = messageService.deleteMessageById(messageId);
            return ResponseEntity.status(HttpStatus.OK)
                .body(numberOfRowsDeleted);
        } catch(MessageNotFoundException e) {
            return ResponseEntity.status(HttpStatus.OK)
                .body(null);
        }
    }

    // 7: Our API should be able to update a message text identified by a message ID
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Long> updateMessageById(@PathVariable String messageId, @RequestBody Message updatedMessage) {
        try {
            long numberOfRowsUpdated = messageService.updateMessageById(messageId, updatedMessage);
            return ResponseEntity.status(HttpStatus.OK)
                .body(numberOfRowsUpdated);
        } catch(InvalidMessageException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(null);
        } catch(MessageNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(null);
        }
    }

    // 8: Our API should be able to retrieve all messages written by a particular user
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessagesFromAccountId(@PathVariable String accountId) {
        try {
            List<Message> messages = messageService.getAllMessagesFromAccountId(accountId);
            return ResponseEntity.status(HttpStatus.OK)
                .body(messages);
        } catch(UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.OK)
                .body(null);
        }
    }
}
