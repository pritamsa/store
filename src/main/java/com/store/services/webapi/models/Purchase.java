package com.store.services.webapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Purchase {

  private String customerId;
  private String discountCode;
  private int cost;
  private int discountAmountUsed;

  public boolean isValid() {
    return StringUtils.isNotBlank(customerId) && cost > 0;
  }

}
