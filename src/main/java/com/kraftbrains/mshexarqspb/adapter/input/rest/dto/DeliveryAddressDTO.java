package com.kraftbrains.mshexarqspb.adapter.input.rest.dto;

import lombok.Data;

@Data
public class DeliveryAddressDTO {
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String zipCode;
    private String reference;
}
