package com.mystore.orderservice.configuration;
import com.mystore.orderservice.model.Order;
import com.mystore.orderservice.model.OrderLineProducts;
import com.mystore.orderservice.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Configuration
public class OrderConfig {
    OrderLineProducts o1 = new OrderLineProducts("1111111",
            BigDecimal.valueOf(28.1),2 );

    OrderLineProducts o2 = new OrderLineProducts("2222222",
            BigDecimal.valueOf(283.3),2 );
    OrderLineProducts o3 = new OrderLineProducts("333333",
            BigDecimal.valueOf(90.1),1);
    OrderLineProducts o4 = new OrderLineProducts("4444444444",
            BigDecimal.valueOf(66.66),6 );
    OrderLineProducts o5 = new OrderLineProducts("5555555",
            BigDecimal.valueOf(33.6),8 );

    List<OrderLineProducts> order_list_1 = new ArrayList<>();

    List<OrderLineProducts> order_list_2 = new ArrayList<>();



//    @Bean
//    CommandLineRunner commandLineRunner(OrderRepository repositry){
//        order_list_1.add(o1);
//        order_list_1.add(o2);
//        order_list_1.add(o3);
//        order_list_2.add(o4);
//        order_list_2.add(o5);
//
//        return args -> {
//            Order order_1 = new Order( "8787878787",order_list_1);
//            Order order_2 = new Order( "19191991", order_list_2);
//            repositry.saveAll(
//                    List.of(order_1,order_2)
//            );
//
//        };
 //   }
}

