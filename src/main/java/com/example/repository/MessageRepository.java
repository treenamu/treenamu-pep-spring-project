package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.entity.Message;

public interface MessageRepository extends CrudRepository<Message, Integer> {
    Optional<List<Message>> findByPostedBy(int userId);
}
