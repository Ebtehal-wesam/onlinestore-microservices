package com.mystore.productservice.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity(name="product")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class Product {
    @Id
    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
    private int id;
    private String name;
    private String description ;
    private BigDecimal price ;

}
