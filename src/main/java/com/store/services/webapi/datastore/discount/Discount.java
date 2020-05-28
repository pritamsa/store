package com.store.services.webapi.datastore.discount;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Discount {
  private String discountCode;
  private int discountAmount;

}
