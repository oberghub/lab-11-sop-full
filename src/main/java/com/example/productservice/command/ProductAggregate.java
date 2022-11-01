package com.example.productservice.command;

import com.example.productservice.core.event.ProductCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Aggregate
public class ProductAggregate {

    //State
    @TargetAggregateIdentifier
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;

    //Handler
    @CommandHandler
    public ProductAggregate(CreateProductCommand command){
        //Business Logic
        if(command.getPrice().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Price <= 0 Eiei ^^.");
        }
        if(command.getTitle().isBlank() || command.getTitle() == null){
            throw new IllegalArgumentException("Title cannot be null.");
        }

        //Event
        ProductCreatedEvent event = new ProductCreatedEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);

    }
    @EventSourcingHandler
    public void on (ProductCreatedEvent event){
        this.productId = event.getProductId();
        this.title = event.getTitle();
        this.price = event.getPrice();
        this.quantity = event.getQuantity();
        System.out.println("ตอนนี้ method on ทำงานแล้ว อิอิ^^");
    }
}
