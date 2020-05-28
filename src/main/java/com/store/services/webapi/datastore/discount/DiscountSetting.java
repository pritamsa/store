package com.store.services.webapi.datastore.discount;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
public class DiscountSetting {
    private String discountCode;
    private int minTransactionsRequired;
    private int discountAmount;


}
