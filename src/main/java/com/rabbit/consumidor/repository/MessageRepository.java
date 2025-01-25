package com.rabbit.consumidor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rabbit.consumidor.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long>{
    
}
