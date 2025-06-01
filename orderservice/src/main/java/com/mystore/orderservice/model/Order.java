package com.mystore.orderservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name="order_t")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class Order {
    @Id
//    @SequenceGenerator(
//            name = "order_sequence",
//            sequenceName = "order_sequence",
//            allocationSize = 1
//    )
    @GeneratedValue(
            strategy = GenerationType.AUTO
           // generator = "order_sequence"
    )
    private String id;
    private String orderNumber;
    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderLineProducts> orderLineItemsList;


    public Order(String orderNumber) {
        this.id= orderNumber;
        this.orderNumber = orderNumber;
    }

}
