package com.mystore.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
//@NoArgsConstructor
public class OrderRequest {
    private List<OrderLineProductsDto> orderLineItemsDtoList;
    public OrderRequest() {
        this.orderLineItemsDtoList = new ArrayList<>();
    }
    public List<OrderLineProductsDto> getOrderLineItemsDtoList() {
        return orderLineItemsDtoList;
    }

    public void setOrderLineItemsDtoList(List<OrderLineProductsDto> orderLineItemsDtoList) {
        this.orderLineItemsDtoList = orderLineItemsDtoList;
    }
}