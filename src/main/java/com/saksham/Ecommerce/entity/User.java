package com.saksham.Ecommerce.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.saksham.Ecommerce.domain.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String fullName;
    private String mobileNumber;
    private UserRole role = UserRole.ROLE_CUSTOMER;

    @OneToMany(cascade = {CascadeType.ALL}, fetch=FetchType.EAGER)
    private Set<Address> addresses = new HashSet<>();

    @ManyToMany
    @JsonIgnore
    private Set<Coupon> usedCoupons = new HashSet<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

//    public Set<Coupon> getUsedCoupons() {
//        return usedCoupons;
//    }

}
