package com.bnp.billing.service;

import java.util.List;
import java.util.Map;

public interface BillingService {
    Map<String, Double> deriveBill(Map<String, Integer> items);
    void validateItems(List<String> itemList);
}
