package com.example.borrowingservice.command.controller;

import com.example.borrowingservice.command.command.CreateBorrowCommand;
import com.example.borrowingservice.command.command.UpdateBookReturnCommand;
import com.example.borrowingservice.command.model.BorrowRequestModel;
import com.example.borrowingservice.command.service.BorrowService;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/borrowing")
public class BorrowCommandController {
    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private BorrowService borrowService;

    @PostMapping
    public String addBookBorrowing(@RequestBody BorrowRequestModel model) {
        try {
            CreateBorrowCommand command =
                    new CreateBorrowCommand(model.getBookId(), model.getEmployeeId(), new Date(), UUID.randomUUID().toString());
            commandGateway.sendAndWait(command);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        return "Book borrowing added";
    }

    @PutMapping
    public String updateBookReturn(@RequestBody BorrowRequestModel model) {
        UpdateBookReturnCommand command = new UpdateBookReturnCommand(borrowService.findIdBorrowing(model.getEmployeeId(), model.getBookId()), model.getBookId(), model.getEmployeeId(), new Date());
        commandGateway.sendAndWait(command);
        return "Book returned";
    }
}