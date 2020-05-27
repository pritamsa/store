package com.store.services.webapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Discount {
    private String discountCode;
    private int discountAmount;
    private int minTransactionsRequired;
}
