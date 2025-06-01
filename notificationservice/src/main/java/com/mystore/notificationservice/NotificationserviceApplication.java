package com.mystore.notificationservice;
import com.mystore.notificationservice.event.OrderPlacedEvent;
import com.mystore.notificationservice.event.ProductPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Slf4j
public class NotificationserviceApplication {
    public static void main (String[] args){
        SpringApplication.run(NotificationserviceApplication.class, args);
    }

//    @KafkaListener(topics = "notificationTopic")
//    public void handleOrderNotification(OrderPlacedEvent orderplacedevent){
//        log.info("Recived notfification for the created order "+orderplacedevent);
//    }

    @KafkaListener(topics = "notifTopic")
    public void handleProductNotification(ProductPlacedEvent productplacedevent){
        log.info("Recived notfification for the created product, "+productplacedevent+" is saved successfully");
    }

}
