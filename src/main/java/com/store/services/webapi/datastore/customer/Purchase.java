package com.store.services.webapi.datastore.customer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Purchase {

  private String discountCode;
  private int discountAmountUsed;
  private int cost;

}
