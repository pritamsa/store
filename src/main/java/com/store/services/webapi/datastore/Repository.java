package com.store.services.webapi.datastore;

import com.store.services.webapi.datastore.customer.CustomerDiscount;
import com.store.services.webapi.datastore.customer.CustomerPurchase;
import com.store.services.webapi.datastore.discount.DiscountSetting;
import com.store.services.webapi.models.Discount;
import com.store.services.webapi.models.Purchase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Repository for all the data.
 */
@Data
@Component
public class Repository {
    public int val;

    private DiscountSetting currentDiscountSetting;
    private List<DiscountSetting> discountSettingArchives;
    private CustomerPurchase customerPurchase;
    private CustomerDiscount customerDiscount;

    public Repository() {}
    private static class SingletonClassHolder {
        static final Repository SINGLE_INSTANCE = new Repository();
    }
    public static Repository getInstance() {
        return SingletonClassHolder.SINGLE_INSTANCE;
    }

  /**
   * This method adds new discount setting.
   * After admin adds a new setting, all the existing customers with eligible purchases get the
   * discounts.
   * @param discount Discount set by admin
   */
    public void addNewDiscountSetting(final Discount discount) {
      if (currentDiscountSetting == null) {
        currentDiscountSetting = new DiscountSetting(discount.getDiscountCode(), discount
            .getMinTransactionsRequired(), discount.getDiscountAmount());


      } else {
        DiscountSetting copySetting  = new DiscountSetting(currentDiscountSetting.getDiscountCode
            (), currentDiscountSetting.getMinTransactionsRequired(), currentDiscountSetting
            .getDiscountAmount());
        if (discountSettingArchives == null) {
          discountSettingArchives = new ArrayList<>();
        }
        discountSettingArchives.add(copySetting);
        currentDiscountSetting.setDiscountAmount(discount.getDiscountAmount());
        currentDiscountSetting.setMinTransactionsRequired(discount.getMinTransactionsRequired());
        currentDiscountSetting.setDiscountCode(discount.getDiscountCode());
      }

      applyCurrentDiscountSettingToCustomers();

    }

  /**
   * This method adds new customer purchase.
   * After customer makes a purchase with or without discount, the code checks if this
   * transaction makes customer eligible for more discount.
   * The method also adjusts existing discount based on the discount amount customer uses.
   * purchases get the
   * discounts.
   * @param purchase purchase by customer
   */
    public void addCustomerPurchase(final Purchase purchase) throws CloneNotSupportedException {
      if (customerPurchase == null) {
        customerPurchase = new CustomerPurchase();
      }

      com.store.services.webapi.datastore.customer.Purchase purchaseData = new
          com.store.services.webapi.datastore.customer.Purchase(purchase.getDiscountCode(),
          purchase.getDiscountAmountUsed(), purchase.getCost());
      customerPurchase.add(purchaseData, purchase.getCustomerId());

      if (customerDiscount == null) {
        customerDiscount = new CustomerDiscount();
      }

      customerDiscount.adjustCustomerDiscount(purchase);

      if (currentDiscountSetting != null) {
        final int eligPurchaseCt = customerPurchase.getDiscountEligiblePurchasesCount(purchase
            .getCustomerId());
        if (eligPurchaseCt >= currentDiscountSetting.getMinTransactionsRequired()) {

          customerDiscount.addUpdateNewDiscount(purchase.getCustomerId(), currentDiscountSetting,
              eligPurchaseCt/currentDiscountSetting.getMinTransactionsRequired());
          customerPurchase.adjustDiscountEligiblePurchaseCt(purchase.getCustomerId(),
              currentDiscountSetting.getMinTransactionsRequired());
        }
      }
    }

  /**
   * This method gets all the discounts earned by a customer.
   * @param customerId id of customer.
   */
    public List<com.store.services.webapi.datastore.discount.Discount> getDiscounts(final String
        customerId) {
      if (customerDiscount != null) {
        return customerDiscount.getDiscounts(customerId);

      }
      return null;
    }

    public boolean isDiscountValid(final Purchase purchase) {

      if ((purchase.getDiscountCode() == null || purchase.getDiscountCode().trim().length()  ==
          0) && purchase.getDiscountAmountUsed() == 0) {
        return true;
      }

      return customerDiscount != null && customerDiscount.isDiscountValid(purchase.getCustomerId(),
          purchase.getDiscountCode
          (), purchase.getDiscountAmountUsed());
    }

  /**
   * This method applies current discount settings to discount eligible customers.
   *
   */
    private void applyCurrentDiscountSettingToCustomers() {
      if (customerPurchase != null) {
        HashMap<String, Integer> discountEligibleCustomers = customerPurchase
        .getDiscountEligiblePurchasesCt();

        if (discountEligibleCustomers != null) {
          for(Map.Entry<String, Integer> entry: discountEligibleCustomers.entrySet()) {

            if(entry != null && entry.getValue() != null && entry.getValue() >=
                currentDiscountSetting.getMinTransactionsRequired()) {
            if (customerDiscount != null) {
                customerDiscount.addUpdateNewDiscount(entry.getKey(), currentDiscountSetting,
                    entry.getValue()/currentDiscountSetting.getMinTransactionsRequired());
                customerPurchase.adjustDiscountEligiblePurchaseCt(entry.getKey(),
                    currentDiscountSetting.getMinTransactionsRequired());
              }

            }
          }
        }

      }
    }

    /**
    * This method gives total discount offered based on discount code.
    *
    */
    public Long getTotalDiscountsOffered(String discountCode) {
      if(customerDiscount != null) {

        if (discountCode != null && discountCode.trim().length() > 0) {
          final Long disc = customerDiscount.getTotalDiscounts().get(discountCode);
          return disc == null ? 0L : disc;
        } else {
          return customerDiscount.getTotalDiscounts().values().stream().reduce(0L, Long::sum);
        }
      }
      return 0L;
    }

}
