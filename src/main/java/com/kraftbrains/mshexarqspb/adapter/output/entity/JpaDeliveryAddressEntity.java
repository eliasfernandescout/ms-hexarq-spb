package com.kraftbrains.mshexarqspb.adapter.output.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class JpaDeliveryAddressEntity {
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String zipCode;
    private String reference;
}
