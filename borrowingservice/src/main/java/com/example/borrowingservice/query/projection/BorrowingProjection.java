package com.example.borrowingservice.query.projection;

import com.example.borrowingservice.command.data.BorrowRepository;
import com.example.borrowingservice.command.data.Borrowing;
import com.example.borrowingservice.query.model.BorrowingResponseModel;
import com.example.borrowingservice.query.queries.GetAllBorrowing;
import com.example.borrowingservice.query.queries.GetListBorrowingByEmployeeQuery;
import com.example.commonservice.model.BookResponseCommonModel;
import com.example.commonservice.model.EmployeeResponseCommonModel;
import com.example.commonservice.query.GetDetailsBookQuery;
import com.example.commonservice.query.GetDetailsEmployeeQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BorrowingProjection {
    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private QueryGateway queryGateway;

    @QueryHandler
    public List<BorrowingResponseModel> handle(GetListBorrowingByEmployeeQuery query) {
        List<BorrowingResponseModel> list = new ArrayList<>();
        List<Borrowing> listEntity = borrowRepository.findByEmployeeIdAndReturnDateIsNull(query.getEmployeeId());
        listEntity.forEach(s -> {
            BorrowingResponseModel model = new BorrowingResponseModel();
            BeanUtils.copyProperties(s, model);
            model.setNameBook(queryGateway.query(new GetDetailsBookQuery(model.getBookId()), ResponseTypes.instanceOf(BookResponseCommonModel.class)).join().getName());
            EmployeeResponseCommonModel employee = queryGateway.query(new GetDetailsEmployeeQuery(model.getEmployeeId()), ResponseTypes.instanceOf(EmployeeResponseCommonModel.class)).join();
            model.setNameEmployee(employee.getFirstName() + " " + employee.getLastName());

            list.add(model);
        });
        return list;
    }

    @QueryHandler
    public List<BorrowingResponseModel> handle(GetAllBorrowing query) {
        List<BorrowingResponseModel> list = new ArrayList<>();
        List<Borrowing> listEntity = borrowRepository.findAll();
        listEntity.forEach(s -> {
            BorrowingResponseModel model = new BorrowingResponseModel();
            BeanUtils.copyProperties(s, model);
            model.setNameBook(queryGateway.query(new GetDetailsBookQuery(model.getBookId()), ResponseTypes.instanceOf(BookResponseCommonModel.class)).join().getName());
            EmployeeResponseCommonModel employee = queryGateway.query(new GetDetailsEmployeeQuery(model.getEmployeeId()), ResponseTypes.instanceOf(EmployeeResponseCommonModel.class)).join();
            model.setNameEmployee(employee.getFirstName() + " " + employee.getLastName());
            list.add(model);
        });
        return list;
    }
}