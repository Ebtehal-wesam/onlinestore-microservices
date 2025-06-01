package com.mystore.notificationservice.model;

import lombok.*;

import jakarta.persistence.*;

@Entity(name="inventory")
@Table
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String skuCode;
    private Integer quantity;
}
