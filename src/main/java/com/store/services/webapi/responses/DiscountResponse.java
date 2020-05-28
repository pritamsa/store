package com.store.services.webapi.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.store.services.webapi.datastore.discount.Discount;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DiscountResponse  {

  @JsonProperty("customerId")
  private String customerId;

  @JsonProperty("discountList")
  private List<Discount> discountList;


}

