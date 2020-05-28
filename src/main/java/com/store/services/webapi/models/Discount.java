package com.store.services.webapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.store.services.webapi.utils.Constants;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Discount {
    @NotNull
    @Size(max = Constants.MAX_LEN_DISCOUNT_CODE)
    private String discountCode;
    @NotNull
    private Integer discountAmount;
    @NotNull
    private Integer minTransactionsRequired;

    public boolean isValid() {
      return minTransactionsRequired != null && minTransactionsRequired > 0 && StringUtils
          .isNotBlank(discountCode) && discountAmount != null && discountAmount > 0;
    }
}
