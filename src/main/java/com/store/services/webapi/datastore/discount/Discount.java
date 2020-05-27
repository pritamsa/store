package com.store.services.webapi.datastore.discount;

import lombok.Data;

@Data
public class Discount {
    private String discountCode;
    private String amount;
}
