package com.rabbit.consumidor.service.Impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbit.consumidor.model.Message;
import com.rabbit.consumidor.repository.MessageRepository;
import com.rabbit.consumidor.service.ConsumidorService;

@Service
public class ConsumidorServiceImpl implements ConsumidorService{

    @Autowired
    private MessageRepository messageRepository;

    @Override
    @RabbitListener(queues = "myQueue")
    public void receiveMessage(String message) {
        System.out.println("Mensaje Recibido: "+message);
        Message m = new Message();
        m.setMessage(message);

        messageRepository.save(m);
    }
    
}
