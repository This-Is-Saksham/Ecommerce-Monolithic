package com.saksham.Ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @NotNull
    @Column(unique = true)
    private String categoryId;

    @ManyToOne
    private Category parentCategory;

    @NotNull
    private Integer level;

    // distributed the category into 3 levels like mens, women and electronic etc. something like this
    // 2nd level is like topwear, bottom wear like this
    // 3rd level is main category like if it is topwear then shirt, t-shirt, kurta. if it is bottom wear then it will pant, trouser etc.
}
