package com.store.services.webapi.datastore.discount;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class DiscountSetting {
    private String discountCode;
    private int minTransactionsRequired;
    private int discountAmount;
}
