package com.store.services.webapi.datastore.customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.Data;

/**
 * Embeds data about customer purchases and the purchases that are eligible for earning discounts.
 */
@Data
public class CustomerPurchase {
    private HashMap<String, List<Purchase>> allPurchases = new HashMap<>();
    private HashMap<String, Integer> discountEligiblePurchasesCt = new HashMap<>();

  /**
   * This method adds new purchase transaction
   * @param purchase purchase transaction
   * @param customerId id of customer.
   */
    public void add(final Purchase purchase, final String customerId) {
        synchronized (allPurchases) {
            List<Purchase> currentPurchases = allPurchases.get(customerId);
            if (currentPurchases == null) {
                currentPurchases = new ArrayList<>(1);
            }
            currentPurchases.add(purchase);

            allPurchases.put(customerId, currentPurchases);
        }
        synchronized (discountEligiblePurchasesCt) {
            Integer discountEligiblePurchases = discountEligiblePurchasesCt.get(customerId);
            if (discountEligiblePurchases == null || discountEligiblePurchases.intValue() == 0) {
                discountEligiblePurchases = 1;
            } else {
                discountEligiblePurchases++;
            }
            discountEligiblePurchasesCt.put(customerId, discountEligiblePurchases);
        }
    }

  /**
   * This method adjusts ndiscount eligible purchase counts.
   * @param count count of discount eligible purchase transaction
   * @param customerId id of customer.
   */
    public void adjustDiscountEligiblePurchaseCt(final String customerId, final int count) {
    synchronized (discountEligiblePurchasesCt) {
      Integer ct = discountEligiblePurchasesCt.get(customerId);
      if (ct != null && ct >= count ) {
        ct = ct - count;
        discountEligiblePurchasesCt.put(customerId, ct);
      }
      }
    }

  /**
   * This method reads discount eligible purchase count
   * @param customerId id of customer.
   */
    public  int getDiscountEligiblePurchasesCount(final String customerId) {
      return discountEligiblePurchasesCt.get(customerId) == null ? 0 : discountEligiblePurchasesCt.get(customerId);
    }

}
