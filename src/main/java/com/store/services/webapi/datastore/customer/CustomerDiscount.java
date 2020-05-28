package com.store.services.webapi.datastore.customer;

import com.store.services.webapi.datastore.discount.Discount;
import com.store.services.webapi.datastore.discount.DiscountSetting;
import com.store.services.webapi.models.Purchase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * This class embeds discount data for all the customers.
 */
@Data
public class CustomerDiscount {

  private Map<String, Map<String, Discount>> discounts = new HashMap<>();
  private Map<String, Long> totalDiscounts = new HashMap<>();


  /**
   * This method validates the discount.
   * @param customerId id of customer.
   * @param discountCode discount code.
   * @param discountAmount discount amount
   */
  public boolean isDiscountValid(final String customerId, final String discountCode, int
      discountAmount) {

    Map<String, Discount> settings = discounts.get(customerId);
    if (settings != null) {
      return settings.get(discountCode) != null && settings.get(discountCode).getDiscountAmount()
          >= discountAmount;
    }
    return false;
  }

  /**
   * This method adjusts customer's discount after a purchase.
   * @param purchase purchase made by customer.

   */
  public void adjustCustomerDiscount(Purchase purchase) {

    if (purchase.getDiscountAmountUsed() == 0) {
      return;
    }

    synchronized (discounts) {
      Map<String, Discount> disc = discounts.get(purchase.getCustomerId());

      if (disc != null) {

        Discount discount = disc.get(purchase.getDiscountCode());

        if (discount != null) {
          int amountUsed = purchase.getDiscountAmountUsed();
          if (amountUsed > discount.getDiscountAmount()) {
            amountUsed = discount.getDiscountAmount();
          }
          discount.setDiscountAmount(discount.getDiscountAmount() - amountUsed);
          disc.put(purchase.getDiscountCode(), discount);
          discounts.put(purchase.getCustomerId(), disc);
        }
      }
    }

  }

  /**
   * This method adds or updates new discount.
   * @param customerId id of a customer.
   * @param discountSetting active discount setting
   * @param multiplier for multiple of discounts

   */
  public void addUpdateNewDiscount(String customerId, DiscountSetting discountSetting, int
      multiplier) {
    synchronized (discounts) {
      Map<String, Discount> discs = discounts.get(customerId);
      int existingAmountOfSameCode = 0;

      if (customerId != null && discountSetting != null) {
        if (discs == null) {
          discs = new HashMap<>();
        } else {
          Discount discount = discs.get(discountSetting.getDiscountCode());
          existingAmountOfSameCode = discount != null ? discount.getDiscountAmount() : 0;
        }

        discs.put(
            discountSetting.getDiscountCode(),
            new Discount(
                discountSetting.getDiscountCode(),
                existingAmountOfSameCode + multiplier * discountSetting.getDiscountAmount()));

        discounts.put(customerId, discs);

        synchronized (totalDiscounts) {
          Long amountOfDiscount = totalDiscounts.get(discountSetting.getDiscountCode());
          if (amountOfDiscount == null) {
            amountOfDiscount = 0L;
          }
          amountOfDiscount += discountSetting.getDiscountAmount();
          totalDiscounts.put(discountSetting.getDiscountCode(), amountOfDiscount);
        }
      }
    }
  }

  /**
   * This method returns total discount for a given customer
   * @param customerId Id of customer
   * @return List of discounts
   */
  public List<Discount> getDiscounts(String customerId) {
    List<Discount> discountList = new ArrayList<>();
    if (customerId != null) {
      Map<String, Discount> discs = discounts.get(customerId);

      if (discs == null) {
        return discountList;
      }

      for (Map.Entry<String,Discount> entry : discs.entrySet()) {
        if (entry.getValue() != null && entry.getValue().getDiscountAmount() > 0) {
          discountList.add(entry.getValue());
        }
      }
    }
    return discountList;
  }

}
