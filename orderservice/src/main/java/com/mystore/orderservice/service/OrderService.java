package com.mystore.orderservice.service;
import com.mystore.orderservice.dto.InventoryResponse;
import com.mystore.orderservice.dto.OrderLineProductsDto;
import com.mystore.orderservice.dto.OrderRequest;
//import com.mystore.orderservice.event.OrderPlacedEvent;
import com.mystore.orderservice.event.OrderPlacedEvent;
import com.mystore.orderservice.model.Order;
import com.mystore.orderservice.model.OrderLineProducts;
import com.mystore.orderservice.repository.OrderRepository;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
//@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private ObservationRegistry observationRegistry;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final KafkaTemplate<String,OrderPlacedEvent> kafkatemplate;
    private Tracer tracer ;
    @Autowired
    public OrderService(OrderRepository r, ApplicationEventPublisher aep, WebClient.Builder wcb, KafkaTemplate kafkatemplate) {
        this.orderRepository = r;
        this.applicationEventPublisher = aep ;
        this.webClientBuilder = wcb ;
        this.kafkatemplate = kafkatemplate;
    }

    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order(UUID.randomUUID().toString());
        List<OrderLineProducts> orderLineProducts = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineProducts);


        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineProducts::getSkuCode)
                .toList();
        // getting every skucode of every product from the list of products in the order
        // Call Inventory Service, and place order if product is in
        Observation inventoryServiceObservation = Observation.createNotStarted("inventory-service-lookup",
                this.observationRegistry);
        inventoryServiceObservation.lowCardinalityKeyValue("call", "inventoryservice");
        Span inventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");
        try (Tracer.SpanInScope spanInScope = tracer.withSpan(inventoryServiceLookup.start())){


        return inventoryServiceObservation.observe(() -> {
            InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                    .uri("http://localhost:8083/api/inventory/isinstock",
                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build()) // this line will return skuCode = value1, value2, value3,....
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                    .allMatch(InventoryResponse::isInStock);

            if (allProductsInStock) {
                orderRepository.save(order);
                kafkatemplate.send("notificationTopic",new OrderPlacedEvent(order.getOrderNumber()));

                // publish Order Placed Event
                //applicationEventPublisher.publishEvent(new OrderPlacedEvent(this, order.getOrderNumber()));
                return "Order Placed";
            } else {
                throw new IllegalArgumentException("Product is not in stock, please try again later");
            }

        }
        );}finally {
            inventoryServiceLookup.end();;
        }

    }

    private OrderLineProducts mapToDto(OrderLineProductsDto orderLineProductsDto) {
        OrderLineProducts orderLineprods = new OrderLineProducts();
        //orderLineprods.setPrice(orderLineProductsDto.getPrice());
        orderLineprods.setQuantity(orderLineProductsDto.getQuantity());
        orderLineprods.setSkuCode(orderLineProductsDto.getSkuCode());
        orderLineprods.setId(orderLineProductsDto.getId());
        orderLineprods.setPrice(orderLineProductsDto.getPrice());
        return orderLineprods;
    }
}