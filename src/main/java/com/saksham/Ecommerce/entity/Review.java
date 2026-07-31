package com.saksham.Ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String reviewText;

    @Column(nullable = false)
    private double rating;

    @ElementCollection
    private List<String> productImages;

    @ManyToOne
    @JsonIgnore
//    @Column(nullable = false)
    @NotNull
    private Product product;

    @ManyToOne
//    @Column(nullable = false)
    @NotNull
    private User user;

    @Column(nullable = false)
    private LocalDateTime createdAt= LocalDateTime.now();

}
