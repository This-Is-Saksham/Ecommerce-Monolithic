package com.saksham.Ecommerce.entity;

/*
* what are the categories we want to show on the home page come in this class
* and what are the images we of products we want to show in the home page
* */


import com.saksham.Ecommerce.domain.HomeCategorySection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class HomeCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String image;
    private String categoryId;
    private HomeCategorySection section;
}
