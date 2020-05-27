package com.store.services.webapi.datastore.customer;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class CustomerPurchase {
    private HashMap<String, List<String>> allPurchases = new HashMap<>();
    private HashMap<String, Integer> discountEligiblePurchasesCt = new HashMap<>();

    public void add(final String customerId, String productCode) {
        synchronized (allPurchases) {
            List<String> currentPurchases = allPurchases.get(customerId);
            if (currentPurchases == null) {
                currentPurchases = new ArrayList<>(1);
                currentPurchases.add(productCode);
            }
            allPurchases.put(customerId, currentPurchases);
        }
        synchronized (discountEligiblePurchasesCt) {
            Integer discountEligiblePurchases = discountEligiblePurchasesCt.get(customerId);
            if (discountEligiblePurchases == null) {
                discountEligiblePurchases = 1;
            } else {
                discountEligiblePurchases++;
            }
            discountEligiblePurchasesCt.put(customerId, discountEligiblePurchases);
        }
    }

    public Integer getDiscountEligiblePurchases(String customerId) {
        synchronized (discountEligiblePurchasesCt) {
            Integer discountEligiblePurchases = discountEligiblePurchasesCt.get(customerId);
            if (discountEligiblePurchases == null) {
                return 0;
            }
            return discountEligiblePurchases;

        }
    }

}
