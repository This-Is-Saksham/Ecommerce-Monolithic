package com.saksham.Ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Cart cart;

    @ManyToOne // many cart item will have same product
    private Product product;

    private String size; // like medium, large, small etc.

    private int quantity=1;

    private Integer mrpPrice;

    private Integer sellingPrice;

    private Long userId;


}
