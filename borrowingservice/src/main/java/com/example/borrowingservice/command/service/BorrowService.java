package com.example.borrowingservice.command.service;

import com.example.borrowingservice.command.data.BorrowRepository;
import com.example.borrowingservice.command.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(Source.class)
public class BorrowService {
    @Autowired
    private BorrowRepository repository;

    @Autowired
    private MessageChannel output;

    public void sendMessage(Message message) {
        try {

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(message);
            output.send(MessageBuilder.withPayload(json).build());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public String findIdBorrowing(String employeeId, String bookId) {

        return repository.findByEmployeeIdAndBookIdAndReturnDateIsNull(employeeId, bookId).getId();
    }
}